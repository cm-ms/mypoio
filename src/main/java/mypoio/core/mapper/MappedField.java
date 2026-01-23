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
    private final ExcelColumn excelColumn;
    private final List<ValidatorContext> validators;

    public MappedField(Field field, ExcelColumn excelColumn) {
        this.field = field;
        this.excelColumn = excelColumn;
        this.validators = discoverValidators(field);
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

    public int getIndexColumn() {
        return excelColumn.index();
    }

    public Field getField() {
        return field;
    }

    public ExcelColumn getExcelColumn() {
        return excelColumn;
    }

    public List<ValidatorContext> getValidators() {
        return validators;
    }
}
