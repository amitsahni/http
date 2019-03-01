package webconnect.com.webconnect.model;

public final class FailureModel {

    Exception e;
    String msg;

    public Exception exception() {
        return e;
    }

    public String getMessage() {
        return msg;
    }
}
