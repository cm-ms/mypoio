package dtos;

import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelPast;

@ExcelModel(index = 0)
public class PersonPastDateTestModel {

    @ExcelColumn(index = 5)
    @ExcelPast
    private String date;
}
