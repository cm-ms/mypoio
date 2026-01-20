package dtos;

import annotations.ExcelColumn;
import annotations.ExcelModel;
import annotations.validators.ExcelRequired;
import custom.ExcelDocumentoBR;

@ExcelModel(index = 0)
public class PersonCustomValidation {
    @ExcelColumn(index = 6)
    @ExcelDocumentoBR
    @ExcelRequired
    private String document;
}
