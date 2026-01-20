package core.validator;

import core.reader.ExcelCell;
import domain.ExcelResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface AnnotationValidator<A extends Annotation> {
    void validate(A annotation, Field field, ExcelCell excelCell, ExcelResult<?> result);
}
