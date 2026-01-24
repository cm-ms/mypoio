package infrastructure;

import dtos.PersonCustomValidation;
import mypoio.ExcelReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExcelCustomValidationTest {
    private static final String SOURCE = "src/test/java/resources/validacoes.xlsx";


    @Test
    void shouldReturnErrorsWhenCustomValidationFails() {
        var reader = new ExcelReader<>(PersonCustomValidation.class);

        var response = reader.initRead(SOURCE);

        Assertions.assertTrue(response.hasErrors());
        Assertions.assertEquals(5, response.getErrorCount());
        Assertions.assertTrue(response.isPartiallySuccess());
    }

    @Test
    void shouldNotValidateWhenSkipValidationIsEnabled() {
        var reader = new ExcelReader<>(PersonCustomValidation.class)
                .skipValidation();

        var response = reader.initRead(SOURCE);

        Assertions.assertFalse(response.hasErrors());
    }
}
