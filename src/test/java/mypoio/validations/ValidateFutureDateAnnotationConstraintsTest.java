package mypoio.validations;

import mypoio.ExcelReader;
import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelFuture;
import mypoio.annotations.constraints.ExcelPatternDate;
import mypoio.annotations.constraints.ExcelRequired;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelResult;
import mypoio.helper.HelperTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateFutureDateAnnotationConstraintsTest {

    private static final String SOURCE = "src/test/java/resources/excel_validation_tests_por_coluna.xlsx";


    @Test
    void shouldValidateFutureDate() {
        ExcelReader<FutureDateValidate> excelReader = new ExcelReader<>(FutureDateValidate.class);
        ExcelResult<FutureDateValidate> result = excelReader.initRead(SOURCE);

        assertNotNull(result);
        assertEquals(4, result.getNumberOfRows());
        assertEquals(3, result.getErrorCount());
        assertEquals(1, result.getValidData().size());

        var errorIsNotFuture = HelperTest.getExcelError(result, "D3");
        var errorIsRequired = HelperTest.getExcelError(result, "D4");
        var errorIsNotPattern = HelperTest.getExcelError(result, "D5");

        assertNotNull(errorIsNotFuture);
        assertNotNull(errorIsRequired);
        assertNotNull(errorIsNotPattern);

        assertEquals(ErrorCode.DATE_PATTERN_FUTURE.getCode(), errorIsNotFuture.getErrorCode());
        assertEquals(ErrorCode.REQUIRED.getCode(), errorIsRequired.getErrorCode());
        assertEquals(ErrorCode.DATE_PATTERN.getCode(), errorIsNotPattern.getErrorCode());

        assertTrue(errorIsNotFuture.getMessage().contains("The date must be in the future."));
        assertTrue(errorIsRequired.getMessage().contains("The field is required"));
        assertTrue(errorIsNotPattern.getMessage().contains("The date must be in the"));
    }


    @ExcelModel
    public static class FutureDateValidate {
        @ExcelFuture
        @ExcelPatternDate
        @ExcelRequired
        @ExcelColumn(index = 3)
        public String value;
    }
}
