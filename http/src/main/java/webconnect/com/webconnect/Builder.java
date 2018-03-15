package webconnect.com.webconnect;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by amit on 23/9/17.
 */

public class Builder {

    private WebParam webParam;

    public Builder(@NonNull Context context, @NonNull String url) {
        webParam = new WebParam();
        webParam.setContext(context);
        webParam.setUrl(url);
    }

    public BuilderRequest.GetRequestBuilder get() {
        webParam.setHttpType(WebParam.HttpType.GET);
        return new BuilderRequest.GetRequestBuilder(webParam);
    }

    public BuilderRequest.HeadRequestBuilder head() {
        webParam.setHttpType(WebParam.HttpType.HEAD);
        return new BuilderRequest.HeadRequestBuilder(webParam);
    }

    public BuilderRequest.OptionsRequestBuilder options() {
        webParam.setHttpType(WebParam.HttpType.OPTIONS);
        return new BuilderRequest.OptionsRequestBuilder(webParam);
    }

    public BuilderRequest.PostRequestBuilder post() {
        webParam.setHttpType(WebParam.HttpType.POST);
        return new BuilderRequest.PostRequestBuilder(webParam);
    }

    public BuilderRequest.PutRequestBuilder put() {
        webParam.setHttpType(WebParam.HttpType.PUT);
        return new BuilderRequest.PutRequestBuilder(webParam);
    }

    public BuilderRequest.DeleteRequestBuilder delete() {
        webParam.setHttpType(WebParam.HttpType.DELETE);
        return new BuilderRequest.DeleteRequestBuilder(webParam);
    }

    public BuilderRequest.PatchRequestBuilder patch() {
        webParam.setHttpType(WebParam.HttpType.PATCH);
        return new BuilderRequest.PatchRequestBuilder(webParam);
    }

    public BuilderRequest.DownloadBuilder download(File file) {
        webParam.setHttpType(WebParam.HttpType.DOWNLOAD);
        webParam.setFile(file);
        return new BuilderRequest.DownloadBuilder(webParam);
    }

    public BuilderRequest.MultiPartBuilder multipartPost() {
        webParam.setHttpType(WebParam.HttpType.POST);
        return new BuilderRequest.MultiPartBuilder(webParam);
    }

    public BuilderRequest.MultiPartBuilder multipartPut() {
        webParam.setHttpType(WebParam.HttpType.PUT);
        return new BuilderRequest.MultiPartBuilder(webParam);
    }

    public BuilderRequest.MultiPartBuilder multipartPatch() {
        webParam.setHttpType(WebParam.HttpType.PATCH);
        return new BuilderRequest.MultiPartBuilder(webParam);
    }

}
