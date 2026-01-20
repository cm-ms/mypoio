package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ExcelResult<T> {
    private List<T> data = new ArrayList<>();
    private List<ExcelError> errors = new ArrayList<>();

    public void addData(T item) {
        if (item != null) {
            this.data.add(item);
        }
    }

    public void addErrorData(ExcelError excelError) {
        if (excelError != null) {
            this.errors.add(excelError);
        }
    }

    public List<T> getData() {
        return Collections.unmodifiableList(data);
    }

    public List<ExcelError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean hasData() {
        return !data.isEmpty();
    }

    public boolean isSuccess() {
        return !hasErrors() && hasData();
    }

    public boolean isPartialSuccess() {
        return hasErrors() && hasData();
    }

    public void setErrors(List<ExcelError> errors) {
        if (Objects.nonNull(errors)) {
            this.errors = errors;
        }
    }

    public void setData(List<T> data) {
        if (Objects.nonNull(data)) {
            this.data = data;
        }
    }

    public int errorSize() {
        return errors.size();
    }

    public int dataSize() {
        return data.size();
    }
}

