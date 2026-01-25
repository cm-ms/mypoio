package mypoio.validations;

import mypoio.ExcelReader;
import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.domain.ExcelResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateIsNotRequiredAnnotationConstraintsTest {

    private static final String SOURCE = "src/test/java/resources/excel_validation_tests_por_coluna.xlsx";


    @Test
    void shouldValidateItNotRequired() {
        ExcelReader<IsNotRequiredValidate> excelReader = new ExcelReader<>(IsNotRequiredValidate.class);
        ExcelResult<IsNotRequiredValidate> result = excelReader.initRead(SOURCE);

        assertNotNull(result);
        assertEquals(4, result.getNumberOfRows());
        assertEquals(0, result.getErrorCount());
        assertEquals(4, result.getValidData().size());
        assertTrue(result.isSuccess());
        assertFalse(result.isPartiallySuccess());
        assertFalse(result.hasErrors());
    }


    @ExcelModel
    public static class IsNotRequiredValidate {
        @ExcelColumn(index = 10)
        public String value;
    }
}
