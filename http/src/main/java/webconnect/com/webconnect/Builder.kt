package webconnect.com.webconnect;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

/**
 * Created by amit on 23/9/17.
 */

public class Builder(context: Context?, url: String) {

    val webParam: WebParam = WebParam()

    init {
        webParam.context = context
        webParam.url = url
    }

    fun get(): BuilderRequest.GetRequestBuilder {
        return BuilderRequest.GetRequestBuilder(webParam)
    }

//    public BuilderRequest.HeadRequestBuilder head() {
//        webParam.setHttpType(WebParam.HttpType.HEAD);
//        return new BuilderRequest.HeadRequestBuilder(webParam);
//    }
//
//    public BuilderRequest.OptionsRequestBuilder options() {
//        webParam.setHttpType(WebParam.HttpType.OPTIONS);
//        return new BuilderRequest.OptionsRequestBuilder(webParam);
//    }
//
//    public BuilderRequest.PostRequestBuilder post() {
//        webParam.setHttpType(WebParam.HttpType.POST);
//        return new BuilderRequest.PostRequestBuilder(webParam);
//    }
//
//    public BuilderRequest.PutRequestBuilder put() {
//        webParam.setHttpType(WebParam.HttpType.PUT);
//        return new BuilderRequest.PutRequestBuilder(webParam);
//    }
//
//    public BuilderRequest.DeleteRequestBuilder delete() {
//        webParam.setHttpType(WebParam.HttpType.DELETE);
//        return new BuilderRequest.DeleteRequestBuilder(webParam);
//    }
//
//    public MultiPartBuilder multipart() {
//        return new MultiPartBuilder(webParam);
//    }
//
//    public BuilderRequest.PatchRequestBuilder patch() {
//        webParam.setHttpType(WebParam.HttpType.PATCH);
//        return new BuilderRequest.PatchRequestBuilder(webParam);
//    }
//
//    public DownloaderBuilder download(File file) {
//        webParam.setHttpType(WebParam.HttpType.DOWNLOAD);
//        webParam.setFile(file);
//        return new DownloaderBuilder(webParam);
//    }
//
//    /*
//     * Download Builder
//     */
//    private class DownloaderBuilder {
//
//        private WebParam webParam;
//
//        DownloaderBuilder(WebParam webParam) {
//            this.webParam = webParam;
//        }
//
//        public BuilderRequest.DownloadBuilder get() {
//            webParam.setHttpType(WebParam.HttpType.GET);
//            return new BuilderRequest.DownloadBuilder(webParam);
//        }
//
//        public BuilderRequest.DownloadBuilderPost post() {
//            webParam.setHttpType(WebParam.HttpType.POST);
//            return new BuilderRequest.DownloadBuilderPost(webParam);
//        }
//
//        public BuilderRequest.DownloadBuilderPut put() {
//            webParam.setHttpType(WebParam.HttpType.PUT);
//            return new BuilderRequest.DownloadBuilderPut(webParam);
//        }
//
//    }
//
//    /*
//     * Multipart Builder
//     */
//    private class MultiPartBuilder {
//
//        private WebParam webParam;
//
//        MultiPartBuilder(WebParam webParam) {
//            this.webParam = webParam;
//        }
//
//        public BuilderRequest.MultiPartBuilder post() {
//            webParam.setHttpType(WebParam.HttpType.POST);
//            return new BuilderRequest.MultiPartBuilder(webParam);
//        }
//
//        public BuilderRequest.MultiPartBuilder put() {
//            webParam.setHttpType(WebParam.HttpType.POST);
//            return new BuilderRequest.MultiPartBuilder(webParam);
//        }
//
//        public BuilderRequest.MultiPartBuilder delete() {
//            webParam.setHttpType(WebParam.HttpType.DELETE);
//            return new BuilderRequest.MultiPartBuilder(webParam);
//        }
//
//        public BuilderRequest.MultiPartBuilder patch() {
//            webParam.setHttpType(WebParam.HttpType.PATCH);
//            return new BuilderRequest.MultiPartBuilder(webParam);
//        }
//
//    }

}
