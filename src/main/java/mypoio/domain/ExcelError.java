package mypoio.domain;

import java.util.Objects;

public final class ExcelError {

    private final String errorCode;
    private final String message;
    private final String address;

    private ExcelError(
            String errorCode,
            String message,
            String address
    ) {
        this.errorCode = errorCode;
        this.message = message;
        this.address = address;
    }

    public static ExcelError of(ErrorCode errorCode, String message, String address) {
        return new ExcelError(
                errorCode.getCode(),
                message,
                address
        );
    }

    public static ExcelError of(ErrorCode errorCode, String message) {
        return new ExcelError(
                errorCode.getCode(),
                message,
                null
        );
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExcelError that = (ExcelError) o;
        return Objects.equals(errorCode, that.errorCode) && Objects.equals(message, that.message) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode, message, address);
    }

    @Override
    public String toString() {
        return "ExcelError{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + message + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
