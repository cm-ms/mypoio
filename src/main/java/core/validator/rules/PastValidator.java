package core.validator.rules;

import annotations.validators.ExcelPast;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PastValidator implements AnnotationValidator<ExcelPast> {
    @Override
    public void validate(ExcelPast ann, Field field, ExcelCell excelCell, ExcelResult<?> res) {
        if (excelCell.isBlank()) return;

        try {
            LocalDate date = LocalDate.parse(excelCell.getValue(), DateTimeFormatter.ofPattern(ann.pattern()));
            if (!date.isBefore(LocalDate.now())) {
                String msg = ann.message().replace("{address}", excelCell.getAddress());
                res.addErrorData(ExcelError.of(field, ErrorCode.DATE_PATTERN_PAST, msg, excelCell));
            }
        } catch (Exception e) {
            // Todo: adicionar erro que não é uma data válida
        }
    }
}
