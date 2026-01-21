package core.validator.rules;

import annotations.validators.ExcelRegex;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;

import java.util.List;

public class RegexValidator implements AnnotationValidator<ExcelRegex> {
    @Override
    public void validate(ExcelRegex annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) return;

        if (excelCell.doesNotMatch(annotation.value())) {
            String msg = annotation.message().replace("{address}", excelCell.getAddress());
            errorList.add(ExcelError.of(ErrorCode.REGEX, msg, excelCell.getAddress()));
        }
    }
}