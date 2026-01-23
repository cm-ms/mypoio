package mypoio.core.validators.constraints;

import mypoio.annotations.constraints.ExcelPatternDate;
import mypoio.core.reader.ExcelCell;
import mypoio.core.validators.AnnotationValidator;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PatternDateValidator implements AnnotationValidator<ExcelPatternDate> {
    @Override
    public Class<ExcelPatternDate> supports() {
        return ExcelPatternDate.class;
    }

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