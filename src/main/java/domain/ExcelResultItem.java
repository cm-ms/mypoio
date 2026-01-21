package domain;

import java.util.ArrayList;
import java.util.List;

public class ExcelResultItem<T> {
    private final T data;
    private final int rowNumber;
    private final boolean isValid;
    private final List<ExcelError> errors;

    public ExcelResultItem(T data, int rowNumber, List<ExcelError> errors) {
        this.data = data;
        this.rowNumber = rowNumber;
        this.errors = (errors != null) ? errors : new ArrayList<>();
        this.isValid = this.errors.isEmpty();
    }

    public T getData() {
        return data;
    }

    public List<ExcelError> getErrors() {
        return errors;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public boolean isValid() {
        return isValid;
    }
}
