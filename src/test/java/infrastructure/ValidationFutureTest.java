package infrastructure;

import mypoio.ExcelReader;
import mypoio.domain.ErrorCode;
import dtos.PersonFutureDateTestModel;
import helper.HelperTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidationFutureTest {
    private static final String SOURCE = "src/test/java/resources/validacoes.xlsx";


    @Test
    void shouldReturnErrorWhenDateIsInThePast() {
        var result = new ExcelReader<>(PersonFutureDateTestModel.class)
                .initRead(SOURCE);

        var error = HelperTest.getExcelError(result, "F2");

        Assertions.assertEquals(ErrorCode.DATE_PATTERN_FUTURE.getCode(), error.getErrorCode());
    }
}
