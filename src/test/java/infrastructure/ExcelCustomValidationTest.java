package infrastructure;

import mypoio.ExcelReader;
import dtos.PersonCustomValidation;
import mypoio.domain.ExcelResultItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExcelCustomValidationTest {
    private static final String SOURCE = "src/test/java/resources/validacoes.xlsx";


    @Test
    void shouldReturnErrorsWhenCustomValidationFails() {


        new ExcelReader<>(PersonCustomValidation.class)
                .fromRow(2) // define a linha inicial de leitura
                .withChunkSize(500) // Processa de 500 em 500
                .skipValidation()   // Caso queira skipar as validações
                .initRead(SOURCE, chunk -> {
                    // a cada 500 linhas é chamado o
                    personRepository.saveAll(chunk.stream().flatMap(ExcelResultItem::getData));
                });

        var reader = new ExcelReader<>(PersonCustomValidation.class, 1);

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
