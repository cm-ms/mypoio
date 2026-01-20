package core.validator.rules;

import annotations.validators.ExcelRequired;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;

import java.lang.reflect.Field;

public class RequiredValidator implements AnnotationValidator<ExcelRequired> {

    @Override
    public void validate(ExcelRequired annotation, Field field, ExcelCell excelCell, ExcelResult<?> result) {
        if (excelCell.isBlank()) {
            String formattedMessage = annotation.message().replace("{address}", excelCell.getAddress());

            ExcelError excelError = ExcelError.of(
                    field,
                    ErrorCode.REQUIRED,
                    formattedMessage,
                    excelCell
            );

            result.addErrorData(excelError);
        }
    }
}
