package annotations.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelNumber {
    double min() default Double.NEGATIVE_INFINITY;
    double max() default Double.POSITIVE_INFINITY;

    String message() default "{address} - The value must be a valid number.";
    String rangeMessage() default "{address} - The value must be between {min} and {max}.";
}
