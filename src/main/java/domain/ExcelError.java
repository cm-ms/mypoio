package domain;

import core.reader.ExcelCell;

import java.lang.reflect.Field;

public class ExcelError {

    private final String field;
    private final String errorCode;
    private final String message;
    private final String address;
    private final int rowLine;
    private final String value;

    private ExcelError(String field, String errorCode, String message, String address, int rowLine, String value) {
        this.field = field;
        this.errorCode = errorCode;
        this.message = message;
        this.address = address;
        this.rowLine = rowLine;
        this.value = value;
    }

    public static ExcelError of(Field field, ErrorCode errorCode, String message, ExcelCell excelCell) {
        return new ExcelError(
                field.getName(),
                errorCode.getCode(),
                message,
                excelCell.getAddress(),
                excelCell.getRowNum(),
                excelCell.getValue()
        );
    }

    public static ExcelError ofLine(int line, String message, ErrorCode errorCode) {
        return new ExcelError("", errorCode.getCode(), message, "", line, "");
    }


    public String getField() {
        return field;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getAddress() {
        return address;
    }

    public int getRowLine() {
        return rowLine;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ExcelError{" +
                "field='" + getField() + '\'' +
                ", errorCode=" + getErrorCode() +
                ", message='" + getMessage() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", rowLine=" + getRowLine() +
                ", value='" + getValue() + '\'' +
                '}';
    }
}
