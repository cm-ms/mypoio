package core.validator.rules;

import annotations.validators.ExcelPhone;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;

import java.lang.reflect.Field;

public class PhoneValidator implements AnnotationValidator<ExcelPhone> {
    @Override
    public void validate(ExcelPhone ann, Field field, ExcelCell excelCell, ExcelResult<?> res) {
        if (excelCell.isBlank()) return;

        if (excelCell.doesNotMatch(ann.regex())) {
            String msg = ann.message().replace("[Address]", excelCell.getAddress());
            res.addErrorData(ExcelError.of(field, ErrorCode.PHONE, msg, excelCell));
        }
    }
}
