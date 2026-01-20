package dtos;

import annotations.ExcelColumn;
import annotations.ExcelModel;
import annotations.validators.ExcelPatternDate;

@ExcelModel(index = 0)
public class PersonPatternDateTestModel {
    @ExcelColumn(index = 5)
    @ExcelPatternDate
    private String date;
}
