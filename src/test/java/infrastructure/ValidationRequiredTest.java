package infrastructure;

import annotations.validators.ExcelNumber;
import annotations.validators.ExcelRequired;
import core.ExcelReader;
import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;
import dtos.PersonRequiredHasPriorityTestModel;
import dtos.PersonRequiredTestModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

import static helper.HelperTest.getExcelError;

public class ValidationRequiredTest {

    private static final String SOURCE = "src/test/java/resources/validacoes.xlsx";

    @Test
    void shouldReturnDefaultAndCustomRequiredMessages() throws Exception {
        var result = read(PersonRequiredTestModel.class);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.hasErrors());
        Assertions.assertEquals(2, result.errorSize());

        var error1 = result.getErrors().get(0);
        var error2 = result.getErrors().get(1);

        Assertions.assertEquals(
                defaultRequiredMessage(error1.getAddress()),
                error1.getMessage()
        );

        Assertions.assertEquals(
                customRequiredMessage(PersonRequiredTestModel.class, "number"),
                error2.getMessage()
        );
    }

    @Test
    void shouldApplyRequiredValidationBeforeOtherValidations() throws Exception {
        var result = read(PersonRequiredHasPriorityTestModel.class);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.hasErrors());
        Assertions.assertEquals(7, result.errorSize());

        var errB3 = getExcelError(result, "B3");
        var errB5 = getExcelError(result, "B5");

        // ExcelNumber should be applied when value exists
        Assertions.assertEquals(
                customNumberMessage(PersonRequiredHasPriorityTestModel.class, "number"),
                errB3.getMessage()
        );

        // ExcelRequired should have priority when value is blank
        Assertions.assertEquals(
                defaultRequiredMessage(errB5.getAddress()),
                errB5.getMessage()
        );

        assertErrorCodeCount(result);
    }

    // ===== Helpers =====

    private static ExcelResult<?> read(Class<?> model) {
        return new ExcelReader<>(model, 1).initRead(SOURCE);
    }

    private static String defaultRequiredMessage(String address) throws NoSuchMethodException {
        return ExcelRequired.class
                .getMethod("message")
                .getDefaultValue()
                .toString()
                .replace("{address}", address);
    }

    private static String customRequiredMessage(Class<?> clazz, String field)
            throws NoSuchFieldException {
        return clazz
                .getDeclaredField(field)
                .getAnnotation(ExcelRequired.class)
                .message();
    }

    private static String customNumberMessage(Class<?> clazz, String field)
            throws NoSuchFieldException {
        return clazz
                .getDeclaredField(field)
                .getAnnotation(ExcelNumber.class)
                .message();
    }

    private static void assertErrorCodeCount(ExcelResult<?> result) {
        Map<String, Long> countByErrorCode =
                result.getErrors().stream()
                        .collect(Collectors.groupingBy(
                                ExcelError::getErrorCode,
                                Collectors.counting()
                        ));

        Assertions.assertEquals(2, countByErrorCode.get(ErrorCode.NUMBER.getCode()));
        Assertions.assertEquals(3, countByErrorCode.get(ErrorCode.OUT_OF_RANGE.getCode()));
        Assertions.assertEquals(2, countByErrorCode.get(ErrorCode.REQUIRED.getCode()));
    }
}

