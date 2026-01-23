package mypoio.core.validators.constraints;

import mypoio.annotations.constraints.ExcelEmail;
import mypoio.core.reader.ExcelCell;
import mypoio.core.validators.AnnotationValidator;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelError;

import java.util.List;

public class EmailValidator implements AnnotationValidator<ExcelEmail> {

    @Override
    public Class<ExcelEmail> supports() {
        return ExcelEmail.class;
    }

    @Override
    public void validate(ExcelEmail annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) return;

        if (excelCell.doesNotMatch(annotation.regex())) {
            String msg = annotation.message().replace("{address}", excelCell.getAddress());
            errorList.add(ExcelError.of(ErrorCode.EMAIL, msg, excelCell.getAddress()));
        }
    }
}
