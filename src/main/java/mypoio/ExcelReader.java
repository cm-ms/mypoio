package mypoio;

import mypoio.core.ExcelMappingContext;
import mypoio.core.ExcelPipeline;
import mypoio.core.ExcelReaderExecutor;
import mypoio.core.mapper.factory.ExcelMapperFactory;
import mypoio.core.mapper.factory.ExcelMapperFactoryDefault;
import mypoio.core.reader.ExcelSourceFactory;
import mypoio.domain.ExcelResult;
import mypoio.domain.ExcelResultItem;
import mypoio.infrastructure.poi.ExcelSourceFactoryPoiDefault;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExcelReader<T> implements ExcelPipeline<T> {
    private final ExcelMappingContext<T> context;
    private ExcelSourceFactory sourceFactory;
    private final ExcelMapperFactory mapperFactory;
    private final ExcelReaderExecutor<T> executor;

    private boolean onlyValid = false;
    private Function<T, ?> mapperFunction = (item) -> item;
    private Consumer<List<ExcelResultItem<?>>> chunkConsumer;

    public ExcelReader(Class<T> clazz) {
        this.context = new ExcelMappingContext<>(clazz);
        this.executor = new ExcelReaderExecutor<>();
        this.sourceFactory = new ExcelSourceFactoryPoiDefault();
        this.mapperFactory = new ExcelMapperFactoryDefault();
    }

    public ExcelReader<T> offsetRow(int offsetRow) {
        this.context.setOffsetRow(offsetRow);
        return this;
    }

    public ExcelReader<T> limit(int limit) {
        this.context.setLimit(limit);
        return this;
    }

    public ExcelReader<T> withChunkSize(int chunkSize) {
        this.context.setChunkSize(chunkSize);
        return this;
    }

    public ExcelReader<T> skipValidation() {
        this.context.setSkipValidation(true);
        return this;
    }

    public ExcelReader<T> withSource(ExcelSourceFactory factory) {
        this.sourceFactory = factory;
        return this;
    }

    public ExcelPipeline<T> pipeline() {
        return this; // A partir daqui, o IDE tratará como ExcelPipeline
    }

    @Override
    public ExcelPipeline<T> onlyValid() {
        this.onlyValid = true;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> ExcelPipeline<R> map(Function<T, R> mapper) {
        this.mapperFunction = (Function<T, ?>) mapper;
        return (ExcelPipeline<R>) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExcelPipeline<T> forEachChunk(Consumer<List<T>> consumer) {
        this.chunkConsumer = (List<ExcelResultItem<?>> items) -> {
            List<T> dataOnly = items.stream()
                    .map(item -> (T) item.getData())
                    .collect(Collectors.toList());

            if (!dataOnly.isEmpty()) {
                ((Consumer<List<T>>) (Consumer<?>) consumer).accept(dataOnly);
            }
        };
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExcelPipeline<T> forEachItemChunk(Consumer<List<ExcelResultItem<T>>> consumer) {
        this.chunkConsumer = (Consumer<List<ExcelResultItem<?>>>) (Consumer<?>) consumer;
        return this;
    }

    public ExcelResult<T> initRead(InputStream source) {
        ExcelResult<T> result = new ExcelResult<>();
        executor.execute(
                source,
                context,
                sourceFactory,
                mapperFactory,
                chunk -> chunk.forEach(result::addRow)
        );
        return result;
    }

    public ExcelResult<T> initRead(String source) {
        ExcelResult<T> result = new ExcelResult<>();
        executor.execute(
                source,
                context,
                sourceFactory,
                mapperFactory,
                chunk -> chunk.forEach(result::addRow)
        );
        return result;
    }

    @Override
    public void read(InputStream source) {
        executor.execute(source, context, sourceFactory, mapperFactory, this::pipelineConsumer);
    }

    @Override
    public void read(String source) {
        executor.execute(source, context, sourceFactory, mapperFactory, this::pipelineConsumer);
    }


    private void pipelineConsumer(List<ExcelResultItem<T>> rawChunk) {
        var stream = rawChunk.stream();

        if (onlyValid) {
            stream = stream.filter(ExcelResultItem::isValid);
        }

        List<ExcelResultItem<?>> processed = stream
                .map(item -> {
                    Object mappedData = mapperFunction.apply(item.getData());
                    return new ExcelResultItem<>(mappedData, item.getRowNumber(), item.getErrors());
                })
                .collect(Collectors.toList());

        if (chunkConsumer != null && !processed.isEmpty()) {
            chunkConsumer.accept(processed);
        }
    }
}
