package infrastructure;

import dtos.PersonCustomValidation;
import mypoio.ExcelReader;
import mypoio.domain.ExcelResultItem;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Collectors;

public class TestCustomConstructor {

    private static final String SOURCE = "src/test/java/resources/validacoes.xlsx";


    @Test
    void testCustomConstructor() {
        var personRepository = Mockito.mock(PersonRepository.class);

        // ler de 2 em 2 para 6 linhas dá 3 chamadas

        new ExcelReader<>(PersonCustomValidation.class)
                .offsetRow(2)
                .withChunkSize(2)
                .skipValidation()
                .initRead(SOURCE, chunk -> {
                    personRepository.saveAll(
                            chunk.stream()
                                    .filter(ExcelResultItem::isValid)
                                    .map(ExcelResultItem::getData)
                                    .collect(Collectors.toList())
                    );
                });

        Mockito.verify(personRepository, Mockito.times(3))
                .saveAll(Mockito.anyList());
    }

    static class PersonRepository {
        public void saveAll(List<PersonCustomValidation> list) {}
    }
}
