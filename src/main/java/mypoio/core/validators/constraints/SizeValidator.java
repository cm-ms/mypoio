package mypoio.core.validators.constraints;

import mypoio.annotations.constraints.ExcelSize;
import mypoio.core.reader.ExcelCell;
import mypoio.core.validators.AnnotationValidator;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelError;

import java.util.List;

public class SizeValidator implements AnnotationValidator<ExcelSize> {
    @Override
    public Class<ExcelSize> supports() {
        return ExcelSize.class;
    }

    @Override
    public void validate(ExcelSize annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        //if (excelCell.isBlank()) return;

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