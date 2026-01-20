package helper;

import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;
import dtos.PersonRequiredHasPriorityTestModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HelperTest {

    public static ExcelError getExcelError(ExcelResult<?> result, String addressCell) {
        var response = getOptionalExcelError(result, addressCell);
        return response.orElseThrow();
    }

    public static Optional<ExcelError> getOptionalExcelError(ExcelResult<?> result, String addressCell) {
        return result.getErrors()
                .stream()
                .filter(excelError -> excelError.getAddress().equalsIgnoreCase(addressCell))
                .findFirst();
    }

    public static Map<String, Long> countErrorCode(ExcelResult<?> result) {
        return result.getErrors().stream()
                        .collect(Collectors.groupingBy(
                                ExcelError::getErrorCode,
                                Collectors.counting()
                        ));
    }

    public static List<String> getAllAddressError(ExcelResult<?> result) {
        return result.getErrors().stream().map(ExcelError::getAddress).collect(Collectors.toList());
    }

}
