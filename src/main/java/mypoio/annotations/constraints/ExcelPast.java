package mypoio.annotations.constraints;

import mypoio.annotations.ExcelConstraint;
import mypoio.core.validators.constraints.PastValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ExcelConstraint(validatedBy = PastValidator.class)
public @interface ExcelPast {
    // A data informada no Excel seja anterior à data atual do sistema.
    String pattern() default "dd/MM/yyyy";
    String message() default "{address} - The date must be in the past.";
}
