package mypoio.core;

import mypoio.domain.ExcelResultItem;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ExcelPipeline<T> {
    ExcelPipeline<T> onlyValid();

    <R> ExcelPipeline<R> map(Function<T, R> mapper);

    ExcelPipeline<T> forEachChunk(Consumer<List<T>> consumer);

    ExcelPipeline<T> forEachItemChunk(Consumer<List<ExcelResultItem<T>>> consumer);

    void read(InputStream is);

    void read(String filePath);
}
