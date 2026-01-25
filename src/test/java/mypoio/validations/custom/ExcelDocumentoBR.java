package mypoio.validations.custom;

import mypoio.annotations.ExcelConstraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ExcelConstraint(validatedBy = DocumentoBRValidator.class)
public @interface ExcelDocumentoBR {
    boolean validarCpf() default true;

    boolean validarCnpj() default true;

    String message() default "{{address}} - O documento informado (CPF/CNPJ) é inválido.";
}