package mypoio.validations;

import mypoio.ExcelReader;
import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelEmail;
import mypoio.annotations.constraints.ExcelRequired;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelResult;
import mypoio.helper.HelperTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateEmailAnnotationConstraintsTest {

    private static final String SOURCE = "src/test/java/resources/excel_validation_tests_por_coluna.xlsx";


    @Test
    void shouldValidateEmail() {
        ExcelReader<EmailValidate> excelReader = new ExcelReader<>(EmailValidate.class);
        ExcelResult<EmailValidate> result = excelReader.initRead(SOURCE);

        assertNotNull(result);
        assertEquals(4, result.getNumberOfRows());
        assertEquals(2, result.getErrorCount());
        assertEquals(2, result.getValidData().size());

        var errorNotBool = HelperTest.getExcelError(result, "C3");
        var errorIsRequired = HelperTest.getExcelError(result, "C4");

        assertNotNull(errorNotBool);
        assertNotNull(errorIsRequired);

        assertEquals(ErrorCode.EMAIL.getCode(), errorNotBool.getErrorCode());
        assertEquals(ErrorCode.REQUIRED.getCode(), errorIsRequired.getErrorCode());

        assertTrue(errorNotBool.getMessage().contains("The provided email address is invalid."));
        assertTrue(errorIsRequired.getMessage().contains("The field is required"));
    }


    @ExcelModel
    public static class EmailValidate {
        @ExcelEmail
        @ExcelRequired
        @ExcelColumn(index = 2)
        public String value;
    }
}
