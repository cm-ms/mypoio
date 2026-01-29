package mypoio;

import mypoio.annotations.ExcelColumn;
import mypoio.annotations.constraints.ExcelRequired;
import mypoio.domain.ExcelResultItem;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;

public class CustomizedConstructorTest {
    private static final String SOURCE = "src/test/java/resources/person_data.xlsx";


    @Test
    void testWhenNotSkipValidation() {
        var response = new ExcelReader<PersonDtoTest>(PersonDtoTest.class)
                .offsetRow(1)
                .initRead(SOURCE);

        assertNotNull(response);
        assertTrue(response.hasErrors());
        assertFalse(response.isSuccess());
    }

    @Test
    void testWhenSkipValidation() {
        var response = new ExcelReader<PersonDtoTest>(PersonDtoTest.class)
                .offsetRow(1)
                .skipValidation()
                .initRead(SOURCE);

        assertNotNull(response);
        assertFalse(response.hasErrors());
        assertTrue(response.isSuccess());
    }

    @Test
    void testWhenUsingExcelPipelineForChunk() {
        PersonDtoRepository personDtoRepository = Mockito.mock(PersonDtoRepository.class);

        new ExcelReader<PersonDtoTest>(PersonDtoTest.class)
                .offsetRow(1)
                .withChunkSize(2)
                .pipeline()
                .onlyValid()
                .forEachChunk(personDtoRepository::saveAll)
                .read(SOURCE);

        Mockito.verify(personDtoRepository, Mockito.times(5)).saveAll(anyList());
    }

    @Test
    void testWhenUsingExcelPipelineForChunkItem() {
        PersonDtoRepository personDtoRepository = Mockito.mock(PersonDtoRepository.class);

        new ExcelReader<PersonDtoTest>(PersonDtoTest.class)
                .offsetRow(1)
                .withChunkSize(5)
                .pipeline()
                .onlyValid()
                .map(PersonTest::of)
                .forEachChunk(personDtoRepository::saveAllPerson)
                .read(SOURCE);

        Mockito.verify(personDtoRepository, Mockito.times(2)).saveAllPerson(anyList());
    }

    @Test
    void testWhenUsingExcelPipelineForEachItemChunk() {
        PersonDtoRepository personDtoRepository = Mockito.mock(PersonDtoRepository.class);

        new ExcelReader<PersonDtoTest>(PersonDtoTest.class)
                .offsetRow(1)
                .withChunkSize(5)
                .pipeline()
                .forEachItemChunk(items -> {
                    List<PersonDtoTest> dtoTests = new ArrayList<>();
                    items.forEach(item -> {
                        if (item.isValid()) {
                            dtoTests.add(PersonDtoTest.of(item));
                        } else {
                            System.out.println("Test - Invalid line: " + item.getErrors().toString());
                        }
                    });
                    personDtoRepository.saveAll(dtoTests);
                })
                .read(SOURCE);

        Mockito.verify(personDtoRepository, Mockito.times(2)).saveAll(anyList());
    }


    public static class PersonDtoTest {
        @ExcelRequired
        @ExcelColumn(index = 0)
        private String id;
        @ExcelColumn(index = 1)
        private String name;
        @ExcelColumn(index = 2)
        private String status;

        public static PersonDtoTest of(ExcelResultItem<PersonDtoTest> item) {
            return new PersonDtoTest();
        }
    }

    public static class PersonTest {
        private Long id;
        private String name;
        private Code code;

        public static PersonTest of(PersonDtoTest test) {
            return new PersonTest();
        }

        static enum Code {
            ATIVO,
            INATIVO
        }
    }

    public static class PersonDtoRepository {
        public void saveAll(List<PersonDtoTest> dtoTests) {
        }

        public void saveAllPerson(List<PersonTest> dtoTests) {
        }

    }
}
