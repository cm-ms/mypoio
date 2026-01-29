package mypoio.core.validators.constraints;

import mypoio.annotations.constraints.ExcelRequired;
import mypoio.core.reader.ExcelCell;
import mypoio.core.validators.AnnotationValidator;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelError;

import java.util.List;

public class RequiredValidator implements AnnotationValidator<ExcelRequired> {

    @Override
    public Class<ExcelRequired> supports() {
        return ExcelRequired.class;
    }

    @Override
    public void validate(ExcelRequired annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) {
            String msg = annotation.message().replace("{address}", excelCell.getAddress());
            errorList.add(ExcelError.of(ErrorCode.REQUIRED, msg, excelCell.getAddress()));
        }
    }
}
