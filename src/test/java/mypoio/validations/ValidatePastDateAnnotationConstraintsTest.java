package mypoio.validations;

import mypoio.ExcelReader;
import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelPast;
import mypoio.annotations.constraints.ExcelPatternDate;
import mypoio.annotations.constraints.ExcelRequired;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelResult;
import mypoio.helper.HelperTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatePastDateAnnotationConstraintsTest {

    private static final String SOURCE = "src/test/java/resources/excel_validation_tests_por_coluna.xlsx";


    @Test
    void shouldValidatePastDate() {
        ExcelReader<PastDateValidate> excelReader = new ExcelReader<>(PastDateValidate.class);
        ExcelResult<PastDateValidate> result = excelReader.initRead(SOURCE);

        assertNotNull(result);
        assertEquals(4, result.getNumberOfRows());
        assertEquals(3, result.getErrorCount());
        assertEquals(1, result.getValidData().size());

        var errorIsNotInPast = HelperTest.getExcelError(result, "F3");
        var errorIsRequired = HelperTest.getExcelError(result, "F4");
        var errorIsNotPattern = HelperTest.getExcelError(result, "F5");

        assertNotNull(errorIsNotInPast);
        assertNotNull(errorIsRequired);
        assertNotNull(errorIsNotPattern);

        assertEquals(ErrorCode.DATE_PATTERN_PAST.getCode(), errorIsNotInPast.getErrorCode());
        assertEquals(ErrorCode.REQUIRED.getCode(), errorIsRequired.getErrorCode());
        assertEquals(ErrorCode.DATE_PATTERN.getCode(), errorIsNotPattern.getErrorCode());

        assertTrue(errorIsNotInPast.getMessage().contains("The date must be in the past."));
        assertTrue(errorIsRequired.getMessage().contains("The field is required"));
        assertTrue(errorIsNotPattern.getMessage().contains("The date must be in the"));
    }


    @ExcelModel
    public static class PastDateValidate {
        @ExcelPast
        @ExcelPatternDate
        @ExcelRequired
        @ExcelColumn(index = 5)
        public String value;
    }
}
