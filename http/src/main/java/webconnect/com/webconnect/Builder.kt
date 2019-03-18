package webconnect.com.webconnect;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

/**
 * Created by amit on 23/9/17.
 */

public class Builder(url: String) {

    private val webParam: WebParam = WebParam()

    init {
        webParam.url = url
    }

    fun get(): BuilderRequest.GetRequestBuilder {
        webParam.httpType = WebParam.HttpType.GET
        return BuilderRequest.GetRequestBuilder(webParam)
    }

    fun head(): BuilderRequest.GetRequestBuilder {
        webParam.httpType = WebParam.HttpType.HEAD
        return BuilderRequest.GetRequestBuilder(webParam)
    }

    fun options(): BuilderRequest.GetRequestBuilder {
        webParam.httpType = WebParam.HttpType.OPTIONS
        return BuilderRequest.GetRequestBuilder(webParam)
    }

    fun post(): BuilderRequest.PostRequestBuilder {
        webParam.httpType = WebParam.HttpType.POST
        return BuilderRequest.PostRequestBuilder(webParam)
    }

    fun put(): BuilderRequest.PutRequestBuilder {
        webParam.httpType = WebParam.HttpType.PUT
        return BuilderRequest.PutRequestBuilder(webParam)
    }

    fun delete(): BuilderRequest.DeleteRequestBuilder {
        webParam.httpType = WebParam.HttpType.DELETE
        return BuilderRequest.DeleteRequestBuilder(webParam)
    }

    fun patch(): BuilderRequest.PatchRequestBuilder {
        webParam.httpType = WebParam.HttpType.PATCH
        return BuilderRequest.PatchRequestBuilder(webParam)
    }

    fun multipart(): MultiPartBuilder {
        return MultiPartBuilder(webParam)
    }

    fun download(file: File): DownloaderBuilder {
        webParam.file = file
        return DownloaderBuilder(webParam);
    }

    class MultiPartBuilder(private val webParam: WebParam) {


        fun post(): BuilderRequest.MultiPartBuilder {
            webParam.httpType = WebParam.HttpType.POST
            return BuilderRequest.MultiPartBuilder(webParam)
        }

        fun put(): BuilderRequest.MultiPartBuilder {
            webParam.httpType = WebParam.HttpType.PUT
            return BuilderRequest.MultiPartBuilder(webParam)
        }

        fun delete(): BuilderRequest.MultiPartBuilder {
            webParam.httpType = WebParam.HttpType.DELETE
            return BuilderRequest.MultiPartBuilder(webParam)
        }

        fun patch(): BuilderRequest.MultiPartBuilder {
            webParam.httpType = WebParam.HttpType.PATCH
            return BuilderRequest.MultiPartBuilder(webParam)
        }

    }

    class DownloaderBuilder(private val webParam: WebParam) {

        fun get(): BuilderRequest.DownloadBuilder {
            webParam.httpType = WebParam.HttpType.GET
            return BuilderRequest.DownloadBuilder(webParam);
        }

        fun post(): BuilderRequest.DownloadBuilderPost {
            webParam.httpType = WebParam.HttpType.POST
            return BuilderRequest.DownloadBuilderPost(webParam)
        }

        fun put(): BuilderRequest.DownloadBuilderPut {
            webParam.httpType = WebParam.HttpType.PUT
            return BuilderRequest.DownloadBuilderPut(webParam)
        }
    }

}
