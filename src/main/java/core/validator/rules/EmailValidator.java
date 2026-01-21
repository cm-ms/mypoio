package core.validator.rules;

import annotations.validators.ExcelEmail;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;

import java.util.List;

public class EmailValidator implements AnnotationValidator<ExcelEmail> {

    @Override
    public void validate(ExcelEmail annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) return;

        if (excelCell.doesNotMatch(annotation.regex())) {
            String msg = annotation.message().replace("{address}", excelCell.getAddress());
            errorList.add(ExcelError.of(ErrorCode.EMAIL, msg, excelCell.getAddress()));
        }
    }
}
