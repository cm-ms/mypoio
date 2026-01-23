package mypoio.core.mapper.factory;

import mypoio.core.mapper.ExcelMapper;
import mypoio.core.reader.ExcelSource;

public interface ExcelMapperFactory {
    <T> ExcelMapper<T> create(int startRow, ExcelSource source, Class<T> clazz, boolean skipValidation);
}
