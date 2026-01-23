package mypoio.core.validators.constraints;

import mypoio.annotations.constraints.ExcelPhone;
import mypoio.core.reader.ExcelCell;
import mypoio.core.validators.AnnotationValidator;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelError;

import java.util.List;

public class PhoneValidator implements AnnotationValidator<ExcelPhone> {
    @Override
    public Class<ExcelPhone> supports() {
        return ExcelPhone.class;
    }

    @Override
    public void validate(ExcelPhone annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) return;

        if (excelCell.doesNotMatch(annotation.regex())) {
            String msg = annotation.message().replace("{address}", excelCell.getAddress());
            errorList.add(ExcelError.of(ErrorCode.PHONE, msg, excelCell.getAddress()));
        }
    }
}
