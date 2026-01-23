package mypoio.annotations.constraints;

import mypoio.annotations.ExcelConstraint;
import mypoio.core.validators.constraints.EmailValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ExcelConstraint(validatedBy = EmailValidator.class)
public @interface ExcelEmail {
    String regex() default "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    String message() default "{address} - The provided email address is invalid.";
}