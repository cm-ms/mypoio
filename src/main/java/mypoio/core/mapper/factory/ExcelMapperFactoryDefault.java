package mypoio.core.mapper.factory;

import mypoio.core.mapper.ExcelMapper;
import mypoio.core.mapper.ExcelMapperImpl;

public class ExcelMapperFactoryDefault implements ExcelMapperFactory {
    @Override
    public ExcelMapper create() {
        return new ExcelMapperImpl();
    }
}
