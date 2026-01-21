package core.validator.rules;

import annotations.validators.ExcelSize;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;

import java.util.List;

public class SizeValidator implements AnnotationValidator<ExcelSize> {
    @Override
    public void validate(ExcelSize annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) return;

        int length = excelCell.valueLength();

        if (length < annotation.min() || length > annotation.max()) {
            String msg = annotation.message()
                    .replace("{address}", excelCell.getAddress())
                    .replace("{min}", String.valueOf(annotation.min()))
                    .replace("{max}", String.valueOf(annotation.max()));

            errorList.add(ExcelError.of(ErrorCode.SIZE, msg, excelCell.getAddress()));
        }
    }
}