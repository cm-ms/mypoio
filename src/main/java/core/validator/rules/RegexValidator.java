package core.validator.rules;

import annotations.validators.ExcelRegex;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;

import java.lang.reflect.Field;

public class RegexValidator implements AnnotationValidator<ExcelRegex> {
    @Override
    public void validate(ExcelRegex ann, Field field, ExcelCell excelCell, ExcelResult<?> res) {
        if (excelCell.isBlank()) return;

        if (excelCell.doesNotMatch(ann.value())) {
            String msg = ann.message().replace("{address}", excelCell.getAddress());
            res.addErrorData(ExcelError.of(field, ErrorCode.REGEX, msg, excelCell));
        }
    }
}