package infrastructure;

import mypoio.ExcelReader;
import mypoio.domain.ErrorCode;
import dtos.PersonEmailTestModel;
import helper.HelperTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidationEmailTest {
    private static final String SOURCE = "src/test/java/resources/validacoes.xlsx";

    @Test
    void shouldReturnErrorForInvalidEmail() {
        var result = new ExcelReader<>(PersonEmailTestModel.class, 1)
                .initRead(SOURCE);

        var error = HelperTest.getExcelError(result, "E7");

        Assertions.assertEquals(ErrorCode.EMAIL.getCode(), error.getErrorCode());
    }
}
