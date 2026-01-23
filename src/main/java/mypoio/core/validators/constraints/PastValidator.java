package mypoio.core.validators.constraints;

import mypoio.annotations.constraints.ExcelPast;
import mypoio.core.reader.ExcelCell;
import mypoio.core.validators.AnnotationValidator;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PastValidator implements AnnotationValidator<ExcelPast> {
    @Override
    public Class<ExcelPast> supports() {
        return ExcelPast.class;
    }

    @Override
    public void validate(ExcelPast annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) return;

        try {
            LocalDate date = LocalDate.parse(excelCell.getValue(), DateTimeFormatter.ofPattern(annotation.pattern()));
            if (!date.isBefore(LocalDate.now())) {
                String msg = annotation.message().replace("{address}", excelCell.getAddress());
                errorList.add(ExcelError.of(ErrorCode.DATE_PATTERN_PAST, msg, excelCell.getAddress()));
            }
        } catch (Exception e) {
            // The exception will be ignored, since standards are handled by @PatternDateValidator
        }
    }
}
