package annotations.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelEmail {
    String regex() default "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    String message() default "[Address] - O e-mail informado é inválido.";
}