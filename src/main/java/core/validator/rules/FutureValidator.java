package core.validator.rules;

import annotations.validators.ExcelFuture;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// valida regra
public class FutureValidator implements AnnotationValidator<ExcelFuture> {
    @Override
    public void validate(ExcelFuture ann, Field field, ExcelCell excelCell, ExcelResult<?> res) {
        if (excelCell.isBlank()) return;

        try {
            LocalDate date = LocalDate.parse(excelCell.getValue(), DateTimeFormatter.ofPattern(ann.pattern()));
            if (!date.isAfter(LocalDate.now())) {
                String msg = ann.message().replace("[Address]", excelCell.getAddress());
                res.addErrorData(ExcelError.of(field, ErrorCode.DATE_PATTERN_FUTURE, msg, excelCell));
            }
        } catch (Exception e) {
            // Todo: verificar se o erro já existe com o PatternDateValidator - senão o erro duplica
            // Todo: adicionar erro que não é uma data válida? nao aqui é regra de negócio, e não se é uma data vpalida, usar o @ExcelPatternDate
        }
    }
}
