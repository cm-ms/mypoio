package core.validator.rules;

import annotations.validators.ExcelSize;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;

import java.lang.reflect.Field;

public class SizeValidator implements AnnotationValidator<ExcelSize> {
    @Override
    public void validate(ExcelSize ann, Field field, ExcelCell excelCell, ExcelResult<?> res) {
        if (excelCell.isBlank()) return;

        int length = excelCell.valueLength();
        if (length < ann.min() || length > ann.max()) {
            String msg = ann.message()
                    .replace("{address}", excelCell.getAddress())
                    .replace("{min}", String.valueOf(ann.min()))
                    .replace("{max}", String.valueOf(ann.max()));
            res.addErrorData(ExcelError.of(field, ErrorCode.SIZE, msg, excelCell));
        }
    }
}