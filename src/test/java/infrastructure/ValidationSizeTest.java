package infrastructure;

import mypoio.ExcelReader;
import mypoio.domain.ErrorCode;
import dtos.PersonSizeTestModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidationSizeTest {
    private static final String SOURCE = "src/test/java/resources/validacoes.xlsx";

    @Test
    void shouldReturnErrorWhenValueIsNotInAllowedValues() {
        var result = new ExcelReader<>(PersonSizeTestModel.class)
                .initRead(SOURCE);

        Assertions.assertFalse(result.getRows().isEmpty());
        Assertions.assertEquals(7, result.getNumberOfRows());

        var item = result.getRows().get(5);

        Assertions.assertFalse(item.getErrors().isEmpty());
        var error = item.getErrors().get(0);

        Assertions.assertEquals(ErrorCode.SIZE.getCode(), error.getErrorCode());
        Assertions.assertEquals("C7", error.getAddress());

        PersonSizeTestModel data = item.getData();
        Assertions.assertTrue(data.getAddress().contains("Av Central"));


    }
}
