package dtos;

import annotations.ExcelColumn;
import annotations.ExcelModel;
import annotations.validators.ExcelFuture;

@ExcelModel(index = 0)
public class PersonFutureDateTestModel {

    @ExcelColumn(index = 5)
    @ExcelFuture
    private String date;
}
