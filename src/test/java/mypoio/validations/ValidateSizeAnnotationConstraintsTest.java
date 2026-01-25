package mypoio.validations;

import mypoio.ExcelReader;
import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelSize;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelResult;
import mypoio.helper.HelperTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateSizeAnnotationConstraintsTest {

    private static final String SOURCE = "src/test/java/resources/excel_validation_tests_por_coluna.xlsx";


    @Test
    void shouldValidateSize() {
        ExcelReader<SizeValidate> excelReader = new ExcelReader<>(SizeValidate.class);
        ExcelResult<SizeValidate> result = excelReader.initRead(SOURCE);

        assertNotNull(result);
        assertEquals(4, result.getNumberOfRows());
        assertEquals(2, result.getErrorCount());
        assertEquals(2, result.getValidData().size());

        var errorSizeK4 = HelperTest.getExcelError(result, "K4");
        var errorSizeK5 = HelperTest.getExcelError(result, "k5");

        assertNotNull(errorSizeK4);
        assertNotNull(errorSizeK5);

        assertEquals(ErrorCode.SIZE.getCode(), errorSizeK4.getErrorCode());
        assertEquals(ErrorCode.SIZE.getCode(), errorSizeK5.getErrorCode());

        assertTrue(errorSizeK4.getMessage().contains("The length must be between"));
        assertTrue(errorSizeK5.getMessage().contains("The length must be between"));
    }


    @ExcelModel
    public static class SizeValidate {
        @ExcelSize(min = 2, max = 3)
        @ExcelColumn(index = 10)
        public String value;
    }
}
