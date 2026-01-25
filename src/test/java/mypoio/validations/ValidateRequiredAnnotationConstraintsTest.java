package mypoio.validations;

import mypoio.ExcelReader;
import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelRequired;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelResult;
import mypoio.helper.HelperTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateRequiredAnnotationConstraintsTest {

    private static final String SOURCE = "src/test/java/resources/excel_validation_tests_por_coluna.xlsx";


    @Test
    void shouldValidateRequired() {
        ExcelReader<RequiredValidate> excelReader = new ExcelReader<>(RequiredValidate.class);
        ExcelResult<RequiredValidate> result = excelReader.initRead(SOURCE);

        assertNotNull(result);
        assertEquals(4, result.getNumberOfRows());
        assertEquals(1, result.getErrorCount());
        assertEquals(3, result.getValidData().size());

        var errorIsRequired = HelperTest.getExcelError(result, "J3");

        assertNotNull(errorIsRequired);
        assertNotNull(errorIsRequired);

        assertEquals(ErrorCode.REQUIRED.getCode(), errorIsRequired.getErrorCode());

        assertTrue(errorIsRequired.getMessage().contains("The field is required"));
    }


    @ExcelModel
    public static class RequiredValidate {
        @ExcelRequired
        @ExcelColumn(index = 9)
        public String value;
    }
}
