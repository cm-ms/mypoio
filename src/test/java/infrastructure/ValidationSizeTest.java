package infrastructure;

import core.ExcelReader;
import domain.ErrorCode;
import dtos.PersonSizeTestModel;
import helper.HelperTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidationSizeTest {
    private static final String SOURCE = "src/test/java/resources/validacoes.xlsx";

    @Test
    void shouldReturnErrorWhenValueIsNotInAllowedValues() {
        var result = new ExcelReader<>(PersonSizeTestModel.class, 1)
                .initRead(SOURCE);

        var error = HelperTest.getExcelError(result, "C7");

        Assertions.assertEquals(ErrorCode.SIZE.getCode(), error.getErrorCode());
        Assertions.assertTrue(error.getValue().contains("Av Central"));
    }
}
