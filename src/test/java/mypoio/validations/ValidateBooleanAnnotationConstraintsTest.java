package mypoio.validations;

import mypoio.ExcelReader;
import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelBoolean;
import mypoio.annotations.constraints.ExcelRequired;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelResult;
import mypoio.helper.HelperTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateBooleanAnnotationConstraintsTest {

    private static final String SOURCE = "src/test/java/resources/excel_validation_tests_por_coluna.xlsx";


    @Test
    void shouldValidateBoolean() {
        ExcelReader<BooleanValidate> excelReader = new ExcelReader<>(BooleanValidate.class);
        ExcelResult<BooleanValidate> result = excelReader.initRead(SOURCE);

        assertNotNull(result);
        assertEquals(4, result.getNumberOfRows());
        assertEquals(2, result.getErrorCount());
        assertEquals(2, result.getValidData().size());

        var errorNotBool = HelperTest.getExcelError(result, "B3");
        var errorIsRequired = HelperTest.getExcelError(result, "B4");

        assertNotNull(errorNotBool);
        assertNotNull(errorIsRequired);

        assertEquals(ErrorCode.BOOLEAN.getCode(), errorNotBool.getErrorCode());
        assertEquals(ErrorCode.REQUIRED.getCode(), errorIsRequired.getErrorCode());

        assertTrue(errorNotBool.getMessage().contains("Invalid value for boolean field."));
        assertTrue(errorIsRequired.getMessage().contains("The field is required"));
    }


    @ExcelModel
    public static class BooleanValidate {
        @ExcelColumn(index = 1)
        @ExcelRequired
        @ExcelBoolean
        public String value;
    }
}
