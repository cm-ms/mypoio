package core.validator.rules;

import annotations.validators.ExcelNumber;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;

import java.lang.reflect.Field;

public class NumberValidator implements AnnotationValidator<ExcelNumber> {

    @Override
    public void validate(ExcelNumber ann, Field field, ExcelCell excelCell, ExcelResult<?> res) {

        if (excelCell.isBlank()) {
            // Important: This makes the notes independent. To be mandatory, it must be marked as Mandatory.
            return;
        }

        String value = excelCell.getValue();

        try {
            double number = Double.parseDouble(value.replace(",", "."));

            if (number < ann.min() || number > ann.max()) {
                String msg = ann.rangeMessage()
                        .replace("{address}", excelCell.getAddress())
                        .replace("{min}", String.valueOf(ann.min()))
                        .replace("{max}", String.valueOf(ann.max()));

                res.addErrorData(ExcelError.of(field, ErrorCode.OUT_OF_RANGE, msg, excelCell));
            }

        } catch (NumberFormatException e) {
            String msg = ann.message().replace("{address}", excelCell.getAddress());
            res.addErrorData(ExcelError.of(field, ErrorCode.NUMBER, msg, excelCell));
        }
    }
}