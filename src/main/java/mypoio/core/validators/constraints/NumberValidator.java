package mypoio.core.validators.constraints;

import mypoio.annotations.constraints.ExcelNumber;
import mypoio.core.reader.ExcelCell;
import mypoio.core.validators.AnnotationValidator;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelError;

import java.util.List;

public class NumberValidator implements AnnotationValidator<ExcelNumber> {

    @Override
    public Class<ExcelNumber> supports() {
        return ExcelNumber.class;
    }

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