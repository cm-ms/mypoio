package core.validator;

import annotations.validators.*;
import core.reader.ExcelCell;
import core.validator.rules.*;
import domain.ExcelError;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationEngine {
    private final Map<Class<? extends Annotation>, AnnotationValidator<?>> validators = new HashMap<>();

    public ValidationEngine() {
        // defaults da minha biblioteca - ficou parecido com o jakarta
        validators.put(ExcelRequired.class, new RequiredValidator());
        validators.put(ExcelNumber.class, new NumberValidator());
        validators.put(ExcelRegex.class, new RegexValidator());
        validators.put(ExcelSize.class, new SizeValidator());
        validators.put(ExcelAllowedValues.class, new AllowedValuesValidator());
        validators.put(ExcelEmail.class, new EmailValidator());
        validators.put(ExcelBoolean.class, new BooleanValidator());
        validators.put(ExcelPhone.class, new PhoneValidator());
        validators.put(ExcelPatternDate.class, new PatternDateValidator());
        validators.put(ExcelPast.class, new PastValidator());
        validators.put(ExcelFuture.class, new FutureValidator());
    }

    public <A extends Annotation> void registerValidator(Class<A> annotationType, AnnotationValidator<A> validator) {
        this.validators.put(annotationType, validator);
    }

    public void validate(Field field, ExcelCell excelCell, List<ExcelError> errorList) {
        int initialErrorCount = errorList.size();
        // Overall: check how the order of the annotations behaved.

        for (Annotation annotation : field.getAnnotations()) {
            AnnotationValidator<?> validator = validators.get(annotation.annotationType());
            if (validator != null) {
                executeValidator(validator, annotation, excelCell, errorList);
                if (errorList.size() > initialErrorCount) {
                    break;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation> void executeValidator(
            AnnotationValidator<A> validator,
            Annotation annotation,
            ExcelCell excelCell,
            List<ExcelError> errorList
    ) {
        validator.validate((A) annotation, excelCell, errorList);
    }

}
