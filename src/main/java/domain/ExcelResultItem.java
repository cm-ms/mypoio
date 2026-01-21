package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExcelResultItem<T> {
    private final T data;
    private final String cellAddress;
    private final boolean isValid;
    private List<ExcelError> errors;

    public ExcelResultItem(T data, String cellAddress, boolean isValid) {
        this.data = data;
        this.cellAddress = cellAddress;
        this.isValid = isValid;
    }

    public void addExcelError(ExcelError excelError) {
        if (Objects.isNull(excelError)) {
            errors = new ArrayList<>();
        }
        errors.add(excelError);
    }

    public T getData() {
        return data;
    }

    public String getCellAddress() {
        return cellAddress;
    }

    public boolean isValid() {
        return isValid;
    }

    public List<ExcelError> getErrors() {
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExcelResultItem<?> that = (ExcelResultItem<?>) o;
        return isValid == that.isValid && Objects.equals(data, that.data) && Objects.equals(cellAddress, that.cellAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, cellAddress, isValid, errors);
    }
}
