package mypoio.annotations.constraints;

import mypoio.annotations.ExcelConstraint;
import mypoio.core.validators.constraints.RequiredValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ExcelConstraint(validatedBy = RequiredValidator.class)
public @interface ExcelRequired {
    String message() default "{address} - The field is required.";
}
