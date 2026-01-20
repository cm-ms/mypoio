package annotations.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelPhone {
    String regex() default "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$";
    String message() default "[Address] - Formato de telefone inválido.";
}