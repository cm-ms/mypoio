package mypoio.core.mapper;

import mypoio.domain.ExcelResult;

public interface ExcelMapper<T> {
    ExcelResult<T> mapperData();
}
