package core.validator.rules;

import annotations.validators.ExcelAllowedValues;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;

import java.lang.reflect.Field;
import java.util.Arrays;

public class AllowedValuesValidator implements AnnotationValidator<ExcelAllowedValues> {
    @Override
    public void validate(ExcelAllowedValues ann, Field field, ExcelCell excelCell, ExcelResult<?> res) {
        if (excelCell.isBlank()) return;

        boolean isAllowed = Arrays.stream(ann.value())
                .anyMatch(v -> v != null && v.equalsIgnoreCase(excelCell.getValue()));

        if (!isAllowed) {
            String allowed = String.join(", ", ann.value());
            String msg = ann.message()
                    .replace("{address}", excelCell.getAddress())
                    .replace("{allowedValues}", allowed);
            res.addErrorData(ExcelError.of(field, ErrorCode.ALLOWED_VALUE, msg, excelCell));
        }
    }
}