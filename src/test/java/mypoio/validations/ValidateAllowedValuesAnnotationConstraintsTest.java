package mypoio.validations;

import mypoio.ExcelReader;
import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelAllowedValues;
import mypoio.annotations.constraints.ExcelRequired;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelResult;
import static org.junit.jupiter.api.Assertions.*;

import mypoio.helper.HelperTest;
import org.junit.jupiter.api.Test;

public class ValidateAllowedValuesAnnotationConstraintsTest {

    private static final String SOURCE = "src/test/java/resources/excel_validation_tests_por_coluna.xlsx";


    @Test
    void shouldValidateAllowedValues() {
        ExcelReader<AllowedValuesValidate> excelReader = new ExcelReader<>(AllowedValuesValidate.class);
        ExcelResult<AllowedValuesValidate> result = excelReader.initRead(SOURCE);

        assertNotNull(result);
        assertEquals(4, result.getNumberOfRows());
        assertEquals(2, result.getErrorCount());
        assertEquals(2, result.getValidData().size());

        var errorNotAllowed = HelperTest.getExcelError(result, "A3");
        var errorIsRequired = HelperTest.getExcelError(result, "A4");

        assertNotNull(errorNotAllowed);
        assertNotNull(errorIsRequired);

        assertEquals(ErrorCode.ALLOWED_VALUE.getCode(), errorNotAllowed.getErrorCode());
        assertEquals(ErrorCode.REQUIRED.getCode(), errorIsRequired.getErrorCode());

        assertTrue(errorNotAllowed.getMessage().contains("Value not allowed. Use:"));
        assertTrue(errorIsRequired.getMessage().contains("field is required"));
    }


    @ExcelModel
    public static class AllowedValuesValidate {
        @ExcelColumn(index = 0)
        @ExcelRequired(message = "field is required")
        @ExcelAllowedValues(value = {"A", "B"})
        public String value;
    }
}
