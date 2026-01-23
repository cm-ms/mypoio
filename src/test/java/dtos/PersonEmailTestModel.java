package dtos;

import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelEmail;

@ExcelModel(index = 0)
public class PersonEmailTestModel {

    @ExcelColumn(index = 4)
    @ExcelEmail
    private String email;
}
