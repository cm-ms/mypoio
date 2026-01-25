package mypoio.validations;

import mypoio.ExcelReader;
import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelPatternDate;
import mypoio.annotations.constraints.ExcelRequired;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelResult;
import mypoio.helper.HelperTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatePatternDateAnnotationConstraintsTest {

    private static final String SOURCE = "src/test/java/resources/excel_validation_tests_por_coluna.xlsx";


    @Test
    void shouldValidatePatternDate() {
        ExcelReader<PastDateValidate> excelReader = new ExcelReader<>(PastDateValidate.class);
        ExcelResult<PastDateValidate> result = excelReader.initRead(SOURCE);

        assertNotNull(result);
        assertEquals(4, result.getNumberOfRows());
        assertEquals(3, result.getErrorCount());
        assertEquals(1, result.getValidData().size());

        var errorIsNotPatternG3 = HelperTest.getExcelError(result, "G3");
        var errorIsRequired = HelperTest.getExcelError(result, "G4");
        var errorIsNotPatternG5 = HelperTest.getExcelError(result, "G5");

        assertNotNull(errorIsNotPatternG3);
        assertNotNull(errorIsRequired);
        assertNotNull(errorIsNotPatternG5);

        assertEquals(ErrorCode.DATE_PATTERN.getCode(), errorIsNotPatternG3.getErrorCode());
        assertEquals(ErrorCode.REQUIRED.getCode(), errorIsRequired.getErrorCode());
        assertEquals(ErrorCode.DATE_PATTERN.getCode(), errorIsNotPatternG5.getErrorCode());

        assertTrue(errorIsNotPatternG3.getMessage().contains("The date must be in the"));
        assertTrue(errorIsRequired.getMessage().contains("The field is required"));
        assertTrue(errorIsNotPatternG5.getMessage().contains("The date must be in the"));
    }


    @ExcelModel
    public static class PastDateValidate {
        @ExcelPatternDate
        @ExcelRequired
        @ExcelColumn(index = 6)
        public String value;
    }
}
