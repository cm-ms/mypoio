package dtos;

import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelRequired;

@ExcelModel(index = 0)
public class PersonRequiredTestModel {

    @ExcelRequired
    @ExcelColumn(index = 0)
    private String name;

    @ExcelRequired(message = "The value is required.")
    @ExcelColumn(index = 1)
    private String number;
}
