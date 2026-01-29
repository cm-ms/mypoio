package mypoio.core.validators.constraints;

import mypoio.annotations.constraints.ExcelFuture;
import mypoio.core.reader.ExcelCell;
import mypoio.core.validators.AnnotationValidator;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FutureValidator implements AnnotationValidator<ExcelFuture> {
    @Override
    public Class<ExcelFuture> supports() {
        return ExcelFuture.class;
    }

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
