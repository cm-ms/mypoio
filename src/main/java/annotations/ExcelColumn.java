package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
   int index();
//
//   boolean required() default true;
//   String requiredMessage() default "[Address] - O campo é obrigatório.";
//
//   boolean isNumber() default false;
//   String isNumberMessage() default "[Address] - O valor deve ser numérico.";
//
//   double min() default Double.NEGATIVE_INFINITY;
//   double max() default Double.POSITIVE_INFINITY;
//
//   int maxSize() default Integer.MAX_VALUE;
//   String maxSizeMessage() default "[Address] - O tamanho máximo é de {maxSize} caracteres.";
//
//   String[] allowedValues() default {};
//   String allowedValuesMessage() default "[Address] - Valor não permitido. Use: {allowedValues}.";
//
//   String regex() default "";
//   String regexMessage() default "[Address] - O formato está inválido.";
//
//   String patternDate() default "";
}
