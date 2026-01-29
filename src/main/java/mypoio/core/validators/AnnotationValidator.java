package mypoio.core.validators;

import mypoio.core.reader.ExcelCell;
import mypoio.domain.ExcelError;

import java.lang.annotation.Annotation;
import java.util.List;

public interface AnnotationValidator<A extends Annotation> {
    Class<A> supports();
    void validate(A annotation, ExcelCell excelCell, List<ExcelError> errorList);
}
