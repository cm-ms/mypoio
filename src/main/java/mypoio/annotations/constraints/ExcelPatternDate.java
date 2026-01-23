package mypoio.annotations.constraints;

import mypoio.annotations.ExcelConstraint;
import mypoio.core.validators.constraints.PatternDateValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ExcelConstraint(validatedBy = PatternDateValidator.class)
public @interface ExcelPatternDate {
    String value() default "dd/MM/yyyy";

    String message() default "{address} - The date must be in the {pattern} format.";
}
