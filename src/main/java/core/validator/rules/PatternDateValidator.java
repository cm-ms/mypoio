package core.validator.rules;

import annotations.validators.ExcelPatternDate;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PatternDateValidator implements AnnotationValidator<ExcelPatternDate> {
    @Override
    public void validate(ExcelPatternDate annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) return;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(annotation.value());
            LocalDate.parse(excelCell.getValue(), formatter);
        } catch (DateTimeParseException e) {
            String msg = annotation.message()
                    .replace("{address}", excelCell.getAddress())
                    .replace("{pattern}", annotation.value());
            errorList.add(ExcelError.of(ErrorCode.DATE_PATTERN, msg, excelCell.getAddress()));
        }
    }
}