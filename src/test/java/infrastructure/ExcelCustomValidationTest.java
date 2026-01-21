package infrastructure;

import core.ExcelReader;
import custom.DocumentoBRValidator;
import custom.ExcelDocumentoBR;
import dtos.PersonCustomValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExcelCustomValidationTest {
    private static final String SOURCE = "src/test/java/resources/validacoes.xlsx";


    @Test
    void shouldReturnErrorsWhenCustomValidationFails() {
        var reader = new ExcelReader<>(PersonCustomValidation.class, 1);
        reader.registerRules(ExcelDocumentoBR.class, new DocumentoBRValidator());

        var response = reader.initRead(SOURCE);

        Assertions.assertTrue(response.hasErrors());
        Assertions.assertEquals(5, response.getErrorCount());
        Assertions.assertTrue(response.isPartiallySuccess());
    }

    @Test
    void shouldNotValidateWhenSkipValidationIsEnabled() {
        var reader = new ExcelReader<>(PersonCustomValidation.class, 1)
                .skipValidation();

        var response = reader.initRead(SOURCE);

        Assertions.assertFalse(response.hasErrors());
    }
}
