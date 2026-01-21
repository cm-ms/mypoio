package core.validator.rules;

import annotations.validators.ExcelEmail;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;

import java.lang.reflect.Field;

public class EmailValidator implements AnnotationValidator<ExcelEmail> {

    @Override
    public void validate(ExcelEmail ann, Field field, ExcelCell excelCell, ExcelResult<?> res) {
        if (excelCell.isBlank()) return;

        if (excelCell.doesNotMatch(ann.regex())) {
            String msg = ann.message().replace("{address}", excelCell.getAddress());

            res.addErrorData(ExcelError.of(field, ErrorCode.EMAIL, msg, excelCell));
        }
    }
}
