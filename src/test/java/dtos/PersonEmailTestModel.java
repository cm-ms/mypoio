package dtos;

import annotations.ExcelColumn;
import annotations.ExcelModel;
import annotations.validators.ExcelEmail;

@ExcelModel(index = 0)
public class PersonEmailTestModel {

    @ExcelColumn(index = 4)
    @ExcelEmail
    private String email;
}
