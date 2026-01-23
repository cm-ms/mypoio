package dtos;

import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelAllowedValues;

@ExcelModel(index = 0)
public class PersonAllowedValuesTestModel {
    @ExcelColumn(index = 3)
    @ExcelAllowedValues(value = {"PJ", "PF"})
    private String personType;
}
