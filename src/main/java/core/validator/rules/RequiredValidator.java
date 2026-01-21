package core.validator.rules;

import annotations.validators.ExcelRequired;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;

import java.util.List;

public class RequiredValidator implements AnnotationValidator<ExcelRequired> {

    @Override
    public void validate(ExcelRequired annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) {
            String msg = annotation.message().replace("{address}", excelCell.getAddress());
            errorList.add(ExcelError.of(ErrorCode.REQUIRED, msg, excelCell.getAddress()));
        }
    }
}
