package mypoio.annotations.constraints;

import mypoio.annotations.ExcelConstraint;
import mypoio.core.validators.constraints.AllowedValuesValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ExcelConstraint(validatedBy = AllowedValuesValidator.class)
public @interface ExcelAllowedValues {
    String[] value();
    String message() default "{address} - Value not allowed. Use: {allowedValues}.";
}
