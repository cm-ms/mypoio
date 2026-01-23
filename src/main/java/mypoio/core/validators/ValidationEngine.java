package mypoio.core.validators;

import mypoio.core.reader.ExcelCell;
import mypoio.domain.ExcelError;
import mypoio.core.mapper.MappedField;

import java.util.List;

public class ValidationEngine {
    public static void validate(MappedField mappedField, ExcelCell excelCell, List<ExcelError> errorList) {
        int initialErrorCount = errorList.size();

        for (ValidatorContext context : mappedField.getValidators()) {
            context.execute(excelCell, errorList);

            if (errorList.size() > initialErrorCount) {
                break;
            }
        }
    }
}
