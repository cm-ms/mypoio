package mypoio.core.validators.constraints;

import mypoio.annotations.constraints.ExcelRegex;
import mypoio.core.reader.ExcelCell;
import mypoio.core.validators.AnnotationValidator;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelError;

import java.util.List;

public class RegexValidator implements AnnotationValidator<ExcelRegex> {
    @Override
    public Class<ExcelRegex> supports() {
        return ExcelRegex.class;
    }

    @Override
    public void validate(ExcelRegex annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) return;

        if (excelCell.doesNotMatch(annotation.value())) {
            String msg = annotation.message().replace("{address}", excelCell.getAddress());
            errorList.add(ExcelError.of(ErrorCode.REGEX, msg, excelCell.getAddress()));
        }
    }
}