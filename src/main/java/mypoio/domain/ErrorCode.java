package mypoio.domain;

public final class ErrorCode {
    private final String code;

    private ErrorCode(String code) {
        this.code = code;
    }

    public static ErrorCode of(String code) {
        return new ErrorCode(code);
    }

    public String getCode() {
        return code;
    }

    public static final ErrorCode UNKNOWN = of("ERROR");
    public static final ErrorCode REQUIRED = of("REQUIRED");
    public static final ErrorCode NUMBER = of("NOT_NUMBER");
    public static final ErrorCode OUT_OF_RANGE = of("OUT_OF_RANGE");
    public static final ErrorCode SIZE = of("INVALID_SIZE");
    public static final ErrorCode ALLOWED_VALUE = of("INVALID_ALLOWED_VALUE");
    public static final ErrorCode DATE_PATTERN = of("INVALID_DATE");
    public static final ErrorCode DATE_PATTERN_FUTURE = of("INVALID_DATE_FUTURE");
    public static final ErrorCode DATE_PATTERN_PAST = of("INVALID_DATE_PAST");
    public static final ErrorCode PHONE = of("INVALID_PHONE");
    public static final ErrorCode REGEX = of("INVALID_REGEX");
    public static final ErrorCode BOOLEAN = of("INVALID_BOOLEAN");
    public static final ErrorCode EMAIL = of("INVALID_EMAIL");


    @Override
    public String toString() {
        return "ErrorCode{" +
                "code='" + code + '\'' +
                '}';
    }
}
