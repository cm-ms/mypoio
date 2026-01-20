package infrastructure;

import core.ExcelReader;
import domain.ErrorCode;
import dtos.PersonFutureDateTestModel;
import dtos.PersonPastDateTestModel;
import helper.HelperTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidatorPastTest {
    private static final String SOURCE = "src/test/java/resources/validacoes.xlsx";


    @Test
    void shouldReturnErrorWhenDateIsInTheFuture() {
        var result = new ExcelReader<>(PersonPastDateTestModel.class, 1)
                .initRead(SOURCE);

        var error = HelperTest.getExcelError(result, "F4");

        Assertions.assertEquals(ErrorCode.DATE_PATTERN_PAST.getCode(), error.getErrorCode());
    }
}
