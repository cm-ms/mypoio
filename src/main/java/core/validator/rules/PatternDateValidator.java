package core.validator.rules;

import annotations.validators.ExcelPatternDate;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// valida formato
public class PatternDateValidator implements AnnotationValidator<ExcelPatternDate> {
    @Override
    public void validate(ExcelPatternDate ann, Field field, ExcelCell excelCell, ExcelResult<?> res) {
        if (excelCell.isBlank()) return;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ann.value());
            LocalDate.parse(excelCell.getValue(), formatter);
        } catch (DateTimeParseException e) {
            String msg = ann.message()
                    .replace("[Address]", excelCell.getAddress())
                    .replace("{pattern}", ann.value());
            res.addErrorData(ExcelError.of(field, ErrorCode.DATE_PATTERN, msg, excelCell));
        }
    }
}