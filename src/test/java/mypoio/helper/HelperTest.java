package mypoio.helper;

import mypoio.domain.ExcelError;
import mypoio.domain.ExcelResult;

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
        return result.getAllErrors()
                .stream()
                .filter(excelError -> excelError.getAddress().equalsIgnoreCase(addressCell))
                .findFirst();
    }

    public static Map<String, Long> countErrorCode(ExcelResult<?> result) {
        return result.getAllErrors().stream()
                .collect(Collectors.groupingBy(
                        ExcelError::getErrorCode,
                        Collectors.counting()
                ));
    }

    public static List<String> getAllAddressError(ExcelResult<?> result) {
        return result.getAllErrors().stream().map(ExcelError::getAddress).collect(Collectors.toList());
    }

}
