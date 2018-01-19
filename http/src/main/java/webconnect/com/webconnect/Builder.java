package webconnect.com.webconnect;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by amit on 23/9/17.
 */

public class Builder {

    private WebParam webParam;

    public Builder(@NonNull Activity context, @NonNull String url) {
        webParam = new WebParam();
        webParam.activityContext = context;
        webParam.context = context;
        webParam.url = url;
    }

    public Builder(@NonNull Context context, @NonNull String url) {
        webParam = new WebParam();
        webParam.context = context;
        webParam.url = url;
    }

    public BuilderRequest.GetRequestBuilder get() {
        webParam.httpType = WebParam.HttpType.GET;
        return new BuilderRequest.GetRequestBuilder(webParam);
    }

    public BuilderRequest.HeadRequestBuilder head() {
        webParam.httpType = WebParam.HttpType.HEAD;
        return new BuilderRequest.HeadRequestBuilder(webParam);
    }

    public BuilderRequest.OptionsRequestBuilder options() {
        webParam.httpType = WebParam.HttpType.OPTIONS;
        return new BuilderRequest.OptionsRequestBuilder(webParam);
    }

    public BuilderRequest.PostRequestBuilder post() {
        webParam.httpType = WebParam.HttpType.POST;
        return new BuilderRequest.PostRequestBuilder(webParam);
    }

    public BuilderRequest.PutRequestBuilder put() {
        webParam.httpType = WebParam.HttpType.PUT;
        return new BuilderRequest.PutRequestBuilder(webParam);
    }

    public BuilderRequest.DeleteRequestBuilder delete() {
        webParam.httpType = WebParam.HttpType.DELETE;
        return new BuilderRequest.DeleteRequestBuilder(webParam);
    }

    public BuilderRequest.PatchRequestBuilder patch() {
        webParam.httpType = WebParam.HttpType.PATCH;
        return new BuilderRequest.PatchRequestBuilder(webParam);
    }

    public BuilderRequest.DownloadBuilder download(File file) {
        webParam.httpType = WebParam.HttpType.DOWNLOAD;
        webParam.file = file;
        return new BuilderRequest.DownloadBuilder(webParam);
    }

    public BuilderRequest.MultiPartBuilder multipart() {
        webParam.httpType = WebParam.HttpType.MULTIPART;
        return new BuilderRequest.MultiPartBuilder(webParam);
    }

}
