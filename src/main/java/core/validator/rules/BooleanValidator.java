package core.validator.rules;

import annotations.validators.ExcelBoolean;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;

import java.util.Arrays;
import java.util.List;

public class BooleanValidator implements AnnotationValidator<ExcelBoolean> {


    @Override
    public void validate(ExcelBoolean annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) return;

        String cellValue = excelCell.getValue();

        boolean isTrue = Arrays.stream(annotation.trueValues())
                .anyMatch(v -> v.equalsIgnoreCase(cellValue));

        boolean isFalse = Arrays.stream(annotation.falseValues())
                .anyMatch(v -> v.equalsIgnoreCase(cellValue));

        if (!isTrue && !isFalse) {
            String msg = annotation.message().replace("{address}", excelCell.getAddress());
            errorList.add(ExcelError.of(ErrorCode.BOOLEAN, msg, excelCell.getAddress()));
        }
    }
}
