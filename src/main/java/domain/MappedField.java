package domain;

import annotations.ExcelColumn;

import java.lang.reflect.Field;

public final class MappedField {

    private final Field field;
    private final ExcelColumn excelColumn;

    public MappedField(Field field, ExcelColumn excelColumn) {
        this.field = field;
        this.excelColumn = excelColumn;
    }

    public int getIndexColumn() {
        return excelColumn.index();
    }

    public Field getField() {
        return field;
    }

    public ExcelColumn getExcelColumn() {
        return excelColumn;
    }
}
