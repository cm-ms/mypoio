package custom;

import mypoio.core.reader.ExcelCell;
import mypoio.core.validators.AnnotationValidator;
import mypoio.domain.ErrorCode;
import mypoio.domain.ExcelError;

import java.util.List;

public class DocumentoBRValidator implements AnnotationValidator<ExcelDocumentoBR> {

    @Override
    public Class<ExcelDocumentoBR> supports() {
        return ExcelDocumentoBR.class;
    }

    @Override
    public void validate(ExcelDocumentoBR annotation, ExcelCell excelCell, List<ExcelError> errorList) {
        if (excelCell.isBlank()) return;

        if (!excelCell.doesNotMatch(".*[a-zA-Z].*")) {
            reportError(annotation, excelCell, errorList);
            return;
        }

        String digitsOnly = excelCell.getValue().replaceAll("\\D", "");

        boolean isValid = false;

        if (digitsOnly.length() == 11 && annotation.validarCpf()) {
            isValid = isCpfValido(digitsOnly);
        } else if (digitsOnly.length() == 14 && annotation.validarCnpj()) {
            isValid = isCnpjValido(digitsOnly);
        }

        if (!isValid) {
            reportError(annotation, excelCell, errorList);
        }
    }

    private void reportError(ExcelDocumentoBR ann, ExcelCell dr, List<ExcelError> res) {
        String msg = ann.message().replace("{address}", dr.getAddress());
        res.add(ExcelError.of(ErrorCode.of("INVALID_DOCUMENT"), msg, dr.getAddress()));
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