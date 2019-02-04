package test.retrofit;


import com.google.gson.annotations.SerializedName;

import webconnect.com.webconnect.model.ErrorModel;

public class Error extends ErrorModel {

    @SerializedName("success")
    private String success;

    @SerializedName("message")
    private String message;
}
