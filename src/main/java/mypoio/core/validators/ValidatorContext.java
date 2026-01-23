package mypoio.core.validators;

import mypoio.domain.ExcelError;
import mypoio.core.reader.ExcelCell;

import java.lang.annotation.Annotation;
import java.util.List;

public final class ValidatorContext {
    private final Annotation annotation;
    private final AnnotationValidator<Annotation> validator;

    @SuppressWarnings("unchecked")
    public ValidatorContext(Annotation annotation, AnnotationValidator<?> validator) {
        this.annotation = annotation;
        this.validator = (AnnotationValidator<Annotation>) validator;
    }

    public void execute(ExcelCell excelCell, List<ExcelError> errors) {
        this.validator.validate(annotation, excelCell, errors);
    }

}
