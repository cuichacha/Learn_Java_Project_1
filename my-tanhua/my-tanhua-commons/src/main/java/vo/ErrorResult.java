package vo;

public class ErrorResult {
    private String errCode;
    private String errMessage;

    public ErrorResult() {
    }

    public ErrorResult(String errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    @Override
    public String toString() {
        return "ErrorResult{" +
                "errCode='" + errCode + '\'' +
                ", errMessage='" + errMessage + '\'' +
                '}';
    }
}
