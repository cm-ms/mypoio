package dtos;

import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelFuture;

@ExcelModel(index = 0)
public class PersonFutureDateTestModel {

    @ExcelColumn(index = 5)
    @ExcelFuture
    private String date;
}
