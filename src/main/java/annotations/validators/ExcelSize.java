package annotations.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSize {
    int min() default 0; // Opcional, por padrão aceita qualquer tamanho >= 0
    int max() default Integer.MAX_VALUE;
    String message() default "[Address] - O tamanho deve estar entre {min} e {max} caracteres.";
}