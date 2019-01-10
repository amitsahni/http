package test.retrofit;

import webconnect.com.webconnect.model.ErrorModel;
import webconnect.com.webconnect.model.SuccessModel;

public class ResponseModel extends SuccessModel {

    private boolean success;
    private String message;
    private int error_code;

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return error_code;
    }

    public boolean isSuccess() {
        return success;
    }
}
