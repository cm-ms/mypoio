package core.validator.rules;

import annotations.validators.ExcelPhone;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;

import java.util.List;

public class PhoneValidator implements AnnotationValidator<ExcelPhone> {
    @Override
    public void validate(ExcelPhone annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) return;

        if (excelCell.doesNotMatch(annotation.regex())) {
            String msg = annotation.message().replace("{address}", excelCell.getAddress());
            errorList.add(ExcelError.of(ErrorCode.PHONE, msg, excelCell.getAddress()));
        }
    }
}
