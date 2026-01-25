package mypoio.validations;

import mypoio.ExcelReader;
import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelRegex;
import mypoio.annotations.constraints.ExcelRequired;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelResult;
import mypoio.helper.HelperTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateRegexAnnotationConstraintsTest {

    private static final String SOURCE = "src/test/java/resources/excel_validation_tests_por_coluna.xlsx";


    @Test
    void shouldValidateRegex() {
        ExcelReader<RegexValidate> excelReader = new ExcelReader<>(RegexValidate.class);
        ExcelResult<RegexValidate> result = excelReader.initRead(SOURCE);

        assertNotNull(result);
        assertEquals(4, result.getNumberOfRows());
        assertEquals(2, result.getErrorCount());
        assertEquals(2, result.getValidData().size());

        var errorIsNotPattern = HelperTest.getExcelError(result, "I3");
        var errorIsRequired = HelperTest.getExcelError(result, "I4");

        assertNotNull(errorIsNotPattern);
        assertNotNull(errorIsRequired);

        assertEquals(ErrorCode.REGEX.getCode(), errorIsNotPattern.getErrorCode());
        assertEquals(ErrorCode.REQUIRED.getCode(), errorIsRequired.getErrorCode());

        assertTrue(errorIsNotPattern.getMessage().contains("The value does not match the expected pattern"));
        assertTrue(errorIsRequired.getMessage().contains("The field is required"));
    }


    @ExcelModel
    public static class RegexValidate {
        @ExcelRegex(value = "^[A-Za-z0-9]+$")
        @ExcelRequired
        @ExcelColumn(index = 8)
        public String value;
    }
}
