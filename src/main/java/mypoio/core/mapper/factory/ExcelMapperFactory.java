package mypoio.core.mapper.factory;

import mypoio.core.ExcelMappingContext;
import mypoio.core.mapper.ExcelMapper;
import mypoio.core.reader.ExcelSource;

public interface ExcelMapperFactory {
     ExcelMapper create();
}
