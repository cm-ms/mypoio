package infrastructure;

import annotations.ExcelColumn;
import annotations.ExcelModel;
import annotations.validators.ExcelPatternDate;
import core.ExcelReader;
import domain.ErrorCode;
import dtos.PersonFutureDateTestModel;
import dtos.PersonPatternDateTestModel;
import helper.HelperTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidationPatternDateTest {
    private static final String SOURCE = "src/test/java/resources/validacoes.xlsx";


    @Test
    void shouldReturnErrorWhenDateIsNotValid() {
        var result = new ExcelReader<>(PersonPatternDateTestModel.class, 1)
                .initRead(SOURCE);

        var error = HelperTest.getExcelError(result, "F6");

        Assertions.assertEquals(ErrorCode.DATE_PATTERN.getCode(), error.getErrorCode());
    }

}
