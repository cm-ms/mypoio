package dtos;

import annotations.ExcelColumn;
import annotations.ExcelModel;
import annotations.validators.ExcelNumber;
import annotations.validators.ExcelRequired;

@ExcelModel(index = 0)
public class PersonRequiredHasPriorityTestModel {
    @ExcelColumn(index = 1)
    @ExcelRequired // Required must be validated first, regardless of annotation declaration order
    @ExcelNumber(min = 100, max = 400, message = "The value is not valid")
    private String number;

    @ExcelColumn(index = 2)// Optional field: no validation error if the cell is empty
    private String address;

    @ExcelColumn(index = 7)
    // If the value is blank, @ExcelRequired is required because there is a null check in the
    // @ExcelNumber check, even though @ExcelNumber is declared first
    @ExcelNumber
    @ExcelRequired
    private String code;

}
