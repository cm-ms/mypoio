package infrastructure;

import core.ExcelReader;
import domain.ErrorCode;
import domain.ExcelResult;
import dtos.PersonNumberValidationScenariosTestModel;
import helper.HelperTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidationNumberTest {
    private static String SOURCE = "src/test/java/resources/validacoes.xlsx";

    @Test
    void shouldReadDataAndReturnErrors() {
        var result = new ExcelReader<>(PersonNumberValidationScenariosTestModel.class, 1)
                .initRead(SOURCE);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.hasErrors());
        Assertions.assertTrue(result.hasData());
    }

    @Test
    void shouldReturnExpectedErrorAndDataSize() {
        var result = read();

        Assertions.assertEquals(8, result.errorSize());
        Assertions.assertEquals(7, result.dataSize());
    }

    @Test
    void shouldReturnExpectedMessagesForSpecificCells() {
        var result = read();

        Assertions.assertEquals(
                "B3 - o mínimo permitido é 350",
                HelperTest.getExcelError(result, "B3").getMessage()
        );

        Assertions.assertEquals(
                "valor deve ser informado",
                HelperTest.getExcelError(result, "H7").getMessage()
        );

        Assertions.assertEquals(
                "H8 - o máximo permitido é 400",
                HelperTest.getExcelError(result, "H8").getMessage()
        );
    }

    @Test
    void shouldGroupErrorsByErrorCode() {
        var result = read();
        var count = HelperTest.countErrorCode(result);

        Assertions.assertEquals(2, count.get(ErrorCode.NUMBER.getCode()));
        Assertions.assertEquals(4, count.get(ErrorCode.OUT_OF_RANGE.getCode()));
        Assertions.assertEquals(2, count.get(ErrorCode.REQUIRED.getCode()));
    }


    private ExcelResult<?> read() {
        return new ExcelReader<>(PersonNumberValidationScenariosTestModel.class, 1)
                .initRead(SOURCE);
    }
}
