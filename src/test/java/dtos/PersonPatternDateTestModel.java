package dtos;

import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelPatternDate;

@ExcelModel(index = 0)
public class PersonPatternDateTestModel {
    @ExcelColumn(index = 5)
    @ExcelPatternDate
    private String date;
}
