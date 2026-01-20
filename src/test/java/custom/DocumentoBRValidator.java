package custom;

import core.reader.ExcelCell;
import core.validator.AnnotationValidator;
import domain.ErrorCode;
import domain.ExcelError;
import domain.ExcelResult;

import java.lang.reflect.Field;

public class DocumentoBRValidator implements AnnotationValidator<ExcelDocumentoBR> {

    @Override
    public void validate(ExcelDocumentoBR ann, Field field, ExcelCell excelCell, ExcelResult<?> res) {
        if (excelCell.isBlank()) return;

        if (!excelCell.doesNotMatch(".*[a-zA-Z].*")) {
            reportError(ann, field, excelCell, res);
            return;
        }

        String digitsOnly = excelCell.getValue().replaceAll("\\D", "");

        boolean isValid = false;

        if (digitsOnly.length() == 11 && ann.validarCpf()) {
            isValid = isCpfValido(digitsOnly);
        } else if (digitsOnly.length() == 14 && ann.validarCnpj()) {
            isValid = isCnpjValido(digitsOnly);
        }

        if (!isValid) {
            reportError(ann, field, excelCell, res);
        }
    }

    private void reportError(ExcelDocumentoBR ann, Field field, ExcelCell dr, ExcelResult<?> res) {
        String msg = ann.message().replace("[Address]", dr.getAddress());
        res.addErrorData(ExcelError.of(field, ErrorCode.of("INVALID_DOCUMENT"), msg, dr));
    }

    private boolean isCpfValido(String cpf) {
        if (cpf.matches("(\\d)\\1{10}")) return false;
        return true;
    }

    private boolean isCnpjValido(String cnpj) {
        if (cnpj.matches("(\\d)\\1{13}")) return false;
        return true;
    }
}