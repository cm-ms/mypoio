package core.validator;

import core.reader.ExcelCell;
import domain.ExcelError;
import domain.ExcelResult;
import domain.ExcelResultItem;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public interface AnnotationValidator<A extends Annotation> {
    void validate(A annotation, ExcelCell excelCell, List<ExcelError> errorList);
}
