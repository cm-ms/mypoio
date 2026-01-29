package mypoio.annotations;

import mypoio.core.validators.AnnotationValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ExcelConstraint {
    Class<? extends AnnotationValidator<?>> validatedBy();
}
