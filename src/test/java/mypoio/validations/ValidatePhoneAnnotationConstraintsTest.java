package mypoio.validations;

import mypoio.ExcelReader;
import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.annotations.constraints.ExcelPhone;
import mypoio.annotations.constraints.ExcelRequired;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelResult;
import mypoio.helper.HelperTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatePhoneAnnotationConstraintsTest {

    private static final String SOURCE = "src/test/java/resources/excel_validation_tests_por_coluna.xlsx";


    @Test
    void shouldValidatePhone() {
        ExcelReader<PhoneValidate> excelReader = new ExcelReader<>(PhoneValidate.class);
        ExcelResult<PhoneValidate> result = excelReader.initRead(SOURCE);

        assertNotNull(result);
        assertEquals(4, result.getNumberOfRows());
        assertEquals(3, result.getErrorCount());
        assertEquals(1, result.getValidData().size());

        var errorIsNotPatternH3 = HelperTest.getExcelError(result, "H3");
        var errorIsNotPatternH5 = HelperTest.getExcelError(result, "H4");
        var errorIsRequired = HelperTest.getExcelError(result, "H5");

        assertNotNull(errorIsNotPatternH3);
        assertNotNull(errorIsRequired);
        assertNotNull(errorIsNotPatternH5);

        assertEquals(ErrorCode.PHONE.getCode(), errorIsNotPatternH3.getErrorCode());
        assertEquals(ErrorCode.REQUIRED.getCode(), errorIsRequired.getErrorCode());
        assertEquals(ErrorCode.PHONE.getCode(), errorIsNotPatternH5.getErrorCode());

        assertTrue(errorIsNotPatternH3.getMessage().contains("Invalid phone number format."));
        assertTrue(errorIsRequired.getMessage().contains("The field is required"));
        assertTrue(errorIsNotPatternH5.getMessage().contains("Invalid phone number format."));
    }


    @ExcelModel
    public static class PhoneValidate {
        @ExcelPhone
        @ExcelRequired
        @ExcelColumn(index = 7)
        public String value;
    }
}
