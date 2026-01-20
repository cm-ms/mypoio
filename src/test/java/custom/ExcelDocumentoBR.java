package custom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelDocumentoBR {
    boolean validarCpf() default true;
    boolean validarCnpj() default true;
    String message() default "[Address] - O documento informado (CPF/CNPJ) é inválido.";
}