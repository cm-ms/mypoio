package mypoio.core.mapper;

import mypoio.domain.ExcelResultItem;

import java.util.List;
import java.util.function.Consumer;

public interface ExcelMapper<T> {
    void mapperData(int chunkSize, Consumer<List<ExcelResultItem<T>>> chunkConsumer);

    default void mapperData(Consumer<ExcelResultItem<T>> rowConsumer) {
        mapperData(1, list -> rowConsumer.accept(list.get(0)));
    }
}
