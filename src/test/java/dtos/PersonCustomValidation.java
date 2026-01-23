package dtos;

import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelRequired;
import custom.ExcelDocumentoBR;

@ExcelModel(index = 0)
public class PersonCustomValidation {
    @ExcelColumn(index = 6)
    @ExcelDocumentoBR
    @ExcelRequired
    private String document;
}
