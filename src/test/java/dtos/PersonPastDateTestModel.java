package dtos;

import annotations.ExcelColumn;
import annotations.ExcelModel;
import annotations.validators.ExcelFuture;
import annotations.validators.ExcelPast;

@ExcelModel(index = 0)
public class PersonPastDateTestModel {

    @ExcelColumn(index = 5)
    @ExcelPast
    private String date;
}
