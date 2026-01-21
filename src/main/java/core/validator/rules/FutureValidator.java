package core.validator.rules;

import annotations.validators.ExcelFuture;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FutureValidator implements AnnotationValidator<ExcelFuture> {
    @Override
    public void validate(ExcelFuture annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) return;

        try {
            LocalDate date = LocalDate.parse(excelCell.getValue(), DateTimeFormatter.ofPattern(annotation.pattern()));
            if (!date.isAfter(LocalDate.now())) {
                String msg = annotation.message().replace("{address}", excelCell.getAddress());
                errorList.add(ExcelError.of(ErrorCode.DATE_PATTERN_FUTURE, msg, excelCell.getAddress()));
            }
        } catch (Exception e) {
            // The exception will be ignored, since standards are handled by @PatternDateValidator
        }
    }
}
