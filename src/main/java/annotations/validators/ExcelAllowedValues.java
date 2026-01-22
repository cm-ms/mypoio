package annotations.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelAllowedValues {
    String[] value();
    String message() default "{address} - Value not allowed. Use: {allowedValues}.";
}
