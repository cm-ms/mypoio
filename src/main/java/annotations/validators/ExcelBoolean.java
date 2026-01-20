package annotations.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelBoolean {
    String[] trueValues() default {"true", "1", "sim", "s", "yes", "y"};
    String[] falseValues() default {"false", "0", "não", "n", "no"};
    String message() default "[Address] - Valor inválido para campo booleano.";
}