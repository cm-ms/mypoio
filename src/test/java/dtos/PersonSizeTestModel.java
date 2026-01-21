package dtos;

import annotations.ExcelColumn;
import annotations.ExcelModel;
import annotations.validators.ExcelSize;

@ExcelModel(index = 0)
public class PersonSizeTestModel {
    @ExcelColumn(index = 2)
    @ExcelSize(max = 5)
    private String address;

    public String getAddress() {
        return address;
    }
}
