package exceptions;

public class ExcelPipelineException extends RuntimeException {
    private Object object;

    public ExcelPipelineException(String message) {
        super(message);
    }

    public ExcelPipelineException(String message, Object object) {
        super(message);
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
