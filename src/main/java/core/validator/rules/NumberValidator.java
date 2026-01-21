package core.validator.rules;

import annotations.validators.ExcelNumber;
import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;

import java.util.List;

public class NumberValidator implements AnnotationValidator<ExcelNumber> {

    @Override
    public void validate(ExcelNumber annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) {
            // Important: This makes the notes independent. To be mandatory, it must be marked as Mandatory.
            return;
        }

        String value = excelCell.getValue();

        try {
            double number = Double.parseDouble(value.replace(",", "."));

            if (number < annotation.min() || number > annotation.max()) {
                String msg = annotation.rangeMessage()
                        .replace("{address}", excelCell.getAddress())
                        .replace("{min}", String.valueOf(annotation.min()))
                        .replace("{max}", String.valueOf(annotation.max()));

                errorList.add(ExcelError.of(ErrorCode.OUT_OF_RANGE, msg, excelCell.getAddress()));
            }

        } catch (NumberFormatException e) {
            String msg = annotation.message().replace("{address}", excelCell.getAddress());
            errorList.add((ExcelError.of(ErrorCode.NUMBER, msg, excelCell.getAddress())));
        }
    }
}