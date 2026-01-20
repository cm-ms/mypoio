package core.mapper;

import domain.ExcelResult;

public interface ExcelMapper<T> {
    ExcelResult<T> mapperData();
}
