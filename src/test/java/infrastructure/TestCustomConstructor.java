package infrastructure;

import dtos.PersonCustomValidation;
import mypoio.ExcelReader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class TestCustomConstructor {

    private static final String SOURCE = "src/test/java/resources/validacoes.xlsx";


    @Test
    void testCustomConstructor() {
        var personRepository = Mockito.mock(PersonRepository.class);

        new ExcelReader<>(PersonCustomValidation.class)
                .offsetRow(2)
                .withChunkSize(2)
                .skipValidation()
                .map(Person::of)
                .forEachChunk(personRepository::saveAll)
                .read(SOURCE);

        Mockito.verify(personRepository, Mockito.times(3))
                .saveAll(Mockito.anyList());
    }

    static class PersonRepository {
        public void saveAll(List<Person> list) {
        }

        public void save(Person person) {
        }
    }

    static class Person {
        static Person of(PersonCustomValidation customValidation) {
            return null;
        }
    }
}
