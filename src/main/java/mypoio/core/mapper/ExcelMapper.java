package mypoio.core.mapper;

import mypoio.core.ExcelMappingContext;
import mypoio.domain.ExcelResultItem;

import java.util.List;
import java.util.function.Consumer;

public interface ExcelMapper {
    <T> void mapperData(ExcelMappingContext<T> context, Consumer<List<ExcelResultItem<T>>> chunkConsumer);
}
