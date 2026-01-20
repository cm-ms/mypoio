package dtos;

import annotations.ExcelColumn;
import annotations.ExcelModel;
import annotations.validators.ExcelNumber;
import annotations.validators.ExcelRequired;

@ExcelModel(index = 0)
public class PersonNumberValidationScenariosTestModel {
    @ExcelColumn(index = 1)
    @ExcelRequired
    @ExcelNumber(min = 350, message = "{address} - o mínimo permitido é 350")
    private String number;

    @ExcelColumn(index = 7)
    @ExcelRequired(message = "valor deve ser informado")
    @ExcelNumber(max = 400, message = "{address} - o máximo permitido é 400")
    private String code;

}
