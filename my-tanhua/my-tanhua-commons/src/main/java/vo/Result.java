package vo;

public class Result {
    private String statusCode;
    private String message;

    public Result() {
    }

    public Result(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "statusCode='" + statusCode + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
