package dtos;

import annotations.ExcelColumn;
import annotations.ExcelModel;
import annotations.validators.ExcelRequired;

@ExcelModel(index = 0)
public class PersonRequiredTestModel {

    @ExcelRequired
    @ExcelColumn(index = 0)
    private String name;

    @ExcelRequired(message = "The value is required.")
    @ExcelColumn(index = 1)
    private String number;
}
