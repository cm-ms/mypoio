package core.validator;

import annotations.validators.*;
import core.reader.ExcelCell;
import core.validator.rules.*;
import domain.ExcelResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
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

    public void validate(Field field, ExcelCell excelCell, ExcelResult<?> result) {
        int initialErrorCount = result.getErrors().size();
        // Todo: verificar como ficou o comportamento da ordem das anotações

        for (Annotation annotation : field.getAnnotations()) {
            AnnotationValidator<?> validator = validators.get(annotation.annotationType());
            if (validator != null) {
                executeValidator(validator, annotation, field, excelCell, result);
                if (result.getErrors().size() > initialErrorCount) {
                    break;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation> void executeValidator(AnnotationValidator<A> validator, Annotation annotation, Field field, ExcelCell excelCell, ExcelResult<?> result) {
        validator.validate((A) annotation, field, excelCell, result);
    }

}
