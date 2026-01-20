package infrastructure;

import core.ExcelReader;
import domain.ErrorCode;
import dtos.PersonAllowedValuesTestModel;
import helper.HelperTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidationAllowedValuesTest {
    private static final String SOURCE = "src/test/java/resources/validacoes.xlsx";

    @Test
    void shouldReturnErrorWhenValueIsNotInAllowedValues() {
        var result = new ExcelReader<>(PersonAllowedValuesTestModel.class, 1)
                .initRead(SOURCE);

        var error = HelperTest.getExcelError(result, "D6");

        Assertions.assertEquals(ErrorCode.ALLOWED_VALUE.getCode(), error.getErrorCode());
        Assertions.assertTrue(error.getValue().contains("XX"));
    }
}
