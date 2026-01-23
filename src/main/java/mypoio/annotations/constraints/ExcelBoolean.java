package mypoio.annotations.constraints;

import mypoio.annotations.ExcelConstraint;
import mypoio.core.validators.constraints.BooleanValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ExcelConstraint(validatedBy = BooleanValidator.class)
public @interface ExcelBoolean {
    String[] trueValues() default {"true", "1", "sim", "s", "yes", "y"};

    String[] falseValues() default {"false", "0", "não", "n", "no"};

    String message() default "{address} - Invalid value for boolean field.";
}