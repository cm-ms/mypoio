package mypoio.validations.custom;

import mypoio.ExcelReader;
import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelModel;
import mypoio.domain.ExcelResult;
import mypoio.helper.HelperTest;
import org.junit.jupiter.api.Test;

import static mypoio.validations.custom.DocumentoBRValidator.DOCUMENT_ERROR_CODE_TEST;
import static org.junit.jupiter.api.Assertions.*;

public class ValidateCustomAnnotationTest {

    private static final String SOURCE = "src/test/java/resources/excel_validation_tests_por_coluna.xlsx";


    @Test
    void shouldValidateCustomAnnotation() {
        ExcelReader<CustomDocument> excelReader = new ExcelReader<>(CustomDocument.class);
        ExcelResult<CustomDocument> result = excelReader.initRead(SOURCE);

        assertNotNull(result);
        assertEquals(4, result.getNumberOfRows());
        assertEquals(2, result.getErrorCount());
        assertEquals(2, result.getValidData().size());

        var errorM4 = HelperTest.getExcelError(result, "M4");
        var errorM5 = HelperTest.getExcelError(result, "M5");

        assertNotNull(errorM4);
        assertNotNull(errorM5);

        assertEquals(DOCUMENT_ERROR_CODE_TEST.getCode(), errorM4.getErrorCode());
        assertEquals(DOCUMENT_ERROR_CODE_TEST.getCode(), errorM5.getErrorCode());

        assertTrue(errorM4.getMessage().contains("O documento informado (CPF/CNPJ) é inválido."));
        assertTrue(errorM5.getMessage().contains("O documento informado (CPF/CNPJ) é inválido."));
    }

    @ExcelModel
    public static class CustomDocument {
        @ExcelDocumentoBR
        @ExcelColumn(index = 12)
        public String value;
    }
}
