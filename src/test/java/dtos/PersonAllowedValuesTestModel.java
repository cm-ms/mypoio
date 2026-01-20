package dtos;

import annotations.ExcelColumn;
import annotations.ExcelModel;
import annotations.validators.ExcelAllowedValues;

@ExcelModel(index = 0)
public class PersonAllowedValuesTestModel {
    @ExcelColumn(index = 3)
    @ExcelAllowedValues(value = {"PJ", "PF"})
    private String personType;
}
