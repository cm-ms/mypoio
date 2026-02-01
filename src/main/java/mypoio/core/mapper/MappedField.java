package mypoio.core.mapper;

import mypoio.annotations.ExcelColumn;
import mypoio.annotations.ExcelConstraint;
import mypoio.core.validators.AnnotationValidator;
import mypoio.core.validators.ValidatorContext;
import mypoio.exceptions.ExcelPipelineException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class MappedField {

    private final Field field;
    private final int referenceColumnIndex;
    private final List<ValidatorContext> validators;

    public MappedField(Field field, int referenceColumnIndex) {
        this.field = field;
        this.validators = discoverValidators(field);
        this.referenceColumnIndex = referenceColumnIndex;
    }

    private List<ValidatorContext> discoverValidators(Field field) {
        List<ValidatorContext> contexts = new ArrayList<>();

        for (Annotation ann : field.getAnnotations()) {
            ExcelConstraint constraint = ann.annotationType().getAnnotation(ExcelConstraint.class);
            if (constraint != null) {
                try {
                    AnnotationValidator<?> validator = constraint.validatedBy()
                            .getDeclaredConstructor().newInstance();

                    if (!validator.supports().equals(ann.annotationType())) {
                        throw new ExcelPipelineException(
                                "Validator " + validator.getClass().getSimpleName() +
                                        " does not support annotation " +
                                        ann.annotationType().getSimpleName()
                        );
                    }

                    contexts.add(new ValidatorContext(ann, validator));
                } catch (Exception e) {
                    throw new RuntimeException("Failed to instantiate validator for: " + ann.annotationType());
                }
            }
        }

        return contexts;
    }


    public Field getField() {
        return field;
    }


    public List<ValidatorContext> getValidators() {
        return validators;
    }

    public int getReferenceColumnIndex() {
        return referenceColumnIndex;
    }
}
