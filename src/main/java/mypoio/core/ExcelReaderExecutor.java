package mypoio.core;

import mypoio.core.mapper.factory.ExcelMapperFactory;
import mypoio.core.reader.ExcelSource;
import mypoio.core.reader.ExcelSourceFactory;
import mypoio.domain.ExcelResultItem;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

public class ExcelReaderExecutor<T> {
    public void execute(InputStream is,
                        ExcelMappingContext<T> context,
                        ExcelSourceFactory sourceFactory,
                        ExcelMapperFactory mapperFactory,
                        Consumer<List<ExcelResultItem<T>>> consumer) {

        try (ExcelSource source = sourceFactory.create(is)) {
            run(source, context, mapperFactory, consumer);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar InputStream: " + e.getMessage(), e);
        }
    }

    public void execute(String filePath,
                        ExcelMappingContext<T> context,
                        ExcelSourceFactory sourceFactory,
                        ExcelMapperFactory mapperFactory,
                        Consumer<List<ExcelResultItem<T>>> consumer) {

        try (ExcelSource source = sourceFactory.create(filePath)) {
            run(source, context, mapperFactory, consumer);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar arquivo " + filePath + ": " + e.getMessage(), e);
        }
    }

    private void run(ExcelSource source,
                     ExcelMappingContext<T> context,
                     ExcelMapperFactory mapperFactory,
                     Consumer<List<ExcelResultItem<T>>> consumer) {

        context.setSource(source);
        mapperFactory.create().mapperData(context, consumer);
    }
}
