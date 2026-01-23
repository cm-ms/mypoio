package dtos;

import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelSize;

@ExcelModel(index = 0)
public class PersonSizeTestModel {
    @ExcelColumn(index = 2)
    @ExcelSize(max = 5)
    private String address;

    public String getAddress() {
        return address;
    }
}
