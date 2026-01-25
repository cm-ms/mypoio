package mypoio.validations;

import mypoio.ExcelReader;
import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelNumber;
import mypoio.annotations.constraints.ExcelRequired;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelResult;
import mypoio.helper.HelperTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateNumberAnnotationConstraintsTest {

    private static final String SOURCE = "src/test/java/resources/excel_validation_tests_por_coluna.xlsx";


    @Test
    void shouldValidateNumber() {
        ExcelReader<NumberValidate> excelReader = new ExcelReader<>(NumberValidate.class);
        ExcelResult<NumberValidate> result = excelReader.initRead(SOURCE);

        assertNotNull(result);
        assertEquals(4, result.getNumberOfRows());
        assertEquals(3, result.getErrorCount());
        assertEquals(1, result.getValidData().size());

        var errorIsNumber = HelperTest.getExcelError(result, "E3");
        var errorIsRequired = HelperTest.getExcelError(result, "E4");
        var errorIsNotRange = HelperTest.getExcelError(result, "E5");

        assertNotNull(errorIsNumber);
        assertNotNull(errorIsRequired);
        assertNotNull(errorIsNotRange);

        assertEquals(ErrorCode.NUMBER.getCode(), errorIsNumber.getErrorCode());
        assertEquals(ErrorCode.REQUIRED.getCode(), errorIsRequired.getErrorCode());
        assertEquals(ErrorCode.OUT_OF_RANGE.getCode(), errorIsNotRange.getErrorCode());

        assertTrue(errorIsNumber.getMessage().contains("The value must be a valid number."));
        assertTrue(errorIsRequired.getMessage().contains("The field is required"));
        assertTrue(errorIsNotRange.getMessage().contains("The value must be between"));
    }


    @ExcelModel
    public static class NumberValidate {
        @ExcelNumber(min = 0, max = 124)
        @ExcelRequired
        @ExcelColumn(index = 4)
        public String value;
    }
}
