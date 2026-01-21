package core.validator.rules;

import annotations.validators.ExcelBoolean;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;

import java.lang.reflect.Field;
import java.util.Arrays;

public class BooleanValidator implements AnnotationValidator<ExcelBoolean> {
    @Override
    public void validate(ExcelBoolean ann, Field field, ExcelCell excelCell, ExcelResult<?> res) {
        if (excelCell.isBlank()) return;

        String cellValue = excelCell.getValue();

        boolean isTrue = Arrays.stream(ann.trueValues())
                .anyMatch(v -> v.equalsIgnoreCase(cellValue));

        boolean isFalse = Arrays.stream(ann.falseValues())
                .anyMatch(v -> v.equalsIgnoreCase(cellValue));

        if (!isTrue && !isFalse) {
            String msg = ann.message().replace("{address}", excelCell.getAddress());
            res.addErrorData(ExcelError.of(field, ErrorCode.BOOLEAN, msg, excelCell));
        }
    }
}
