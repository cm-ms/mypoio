package mypoio.core.validators.constraints;

import mypoio.annotations.constraints.ExcelAllowedValues;
import mypoio.core.reader.ExcelCell;
import mypoio.core.validators.AnnotationValidator;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelError;

import java.util.Arrays;
import java.util.List;

public class AllowedValuesValidator implements AnnotationValidator<ExcelAllowedValues> {

    @Override
    public Class<ExcelAllowedValues> supports() {
        return ExcelAllowedValues.class;
    }

    @Override
    public void validate(ExcelAllowedValues annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) return;

        boolean isAllowed = Arrays.stream(annotation.value())
                .anyMatch(v -> v != null && v.equalsIgnoreCase(excelCell.getValue()));

        if (!isAllowed) {
            String allowed = String.join(", ", annotation.value());
            String msg = annotation.message()
                    .replace("{address}", excelCell.getAddress())
                    .replace("{allowedValues}", allowed);

            errorList.add(ExcelError.of(ErrorCode.ALLOWED_VALUE, msg, excelCell.getAddress()));
        }
    }
}