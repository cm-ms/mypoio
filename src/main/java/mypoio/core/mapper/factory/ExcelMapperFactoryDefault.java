package mypoio.core.mapper.factory;

import mypoio.core.mapper.ExcelMapper;
import mypoio.core.mapper.ExcelMapperImpl;
import mypoio.core.reader.ExcelSource;

public class ExcelMapperFactoryDefault implements ExcelMapperFactory{
    @Override
    public <T> ExcelMapper<T> create(int startRow, ExcelSource source, Class<T> clazz, boolean skipValidation) {
        return new ExcelMapperImpl<>(startRow, source, clazz, skipValidation);
    }
}
