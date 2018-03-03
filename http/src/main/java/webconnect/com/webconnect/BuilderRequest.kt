package webconnect.com.webconnect

import android.app.Dialog
import android.content.ContentResolver
import android.net.Uri
import android.text.TextUtils

import com.google.gson.Gson

import java.io.File
import java.io.IOException
import java.util.HashMap
import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.CacheControl
import okhttp3.Call
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import webconnect.com.webconnect.di.IProperties
import webconnect.com.webconnect.listener.AnalyticsListener
import webconnect.com.webconnect.listener.OnWebCallback
import webconnect.com.webconnect.listener.ProgressListener

/**
 * Created by clickapps on 27/12/17.
 */
class BuilderRequest {

    open class GetRequestBuilder(private val param: WebParam) : IProperties<GetRequestBuilder> {
        private var okHttpClient: OkHttpClient? = null

        override fun baseUrl(url: String): GetRequestBuilder {
            param.baseUrl = url
            return this
        }

        override fun headerParam(headerParam: Map<String, String>): GetRequestBuilder {
            param.headerParam = headerParam
            return this
        }

        override fun callback(callback: OnWebCallback): GetRequestBuilder {
            param.callback = callback
            return this
        }

        override fun analyticsListener(callback: AnalyticsListener): GetRequestBuilder {
            param.analyticsListener = callback
            return this
        }

        override fun callback(callback: OnWebCallback, success: Class<*>, error: Class<*>): GetRequestBuilder {
            param.callback = callback
            param.model = success
            param.error = error
            return this
        }

        override fun taskId(taskId: Int): GetRequestBuilder {
            param.taskId = taskId
            return this
        }

        override fun timeOut(connectTimeOut: Long, readTimeOut: Long): GetRequestBuilder {
            param.connectTimeOut = connectTimeOut
            param.readTimeOut = readTimeOut
            return this
        }

        override fun progressDialog(dialog: Dialog): GetRequestBuilder {
            param.dialog = dialog
            return this
        }

        override fun cache(isCache: Boolean): GetRequestBuilder {
            param.isCacheEnabled = isCache
            return this
        }

        override fun queryParam(requestParam: Map<String, String>): GetRequestBuilder {
            param.queryParam = requestParam
            return this
        }

        override fun connect() {
            performGetRequest().subscribe(Callback.GetRequestCallback(param))
        }

        fun performGetRequest(): Observable<*> {
            var baseUrl = ApiConfiguration.getBaseUrl()
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl
            }
            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl + param.url)?.newBuilder()

            val entries = param.queryParam.entries
            for ((name, value1) in entries) {
                val value = value1
                urlBuilder?.addQueryParameter(name, value)
            }
            builder.url(urlBuilder?.build().toString())

            val headerBuilder = Headers.Builder()
            for ((key, value) in param.headerParam) {
                headerBuilder.add(key, value)
            }
            builder.headers(headerBuilder.build())

            when (param.httpType) {
                WebParam.HttpType.GET -> {
                    builder = builder.get()
                }
                WebParam.HttpType.HEAD -> {
                    builder = builder.head()
                }
                WebParam.HttpType.OPTIONS -> {
                    builder = builder.method("OPTIONS", null)
                }
                else -> {
                }
            }
            okHttpClient = HTTPManager.get().getDefaultOkHttpClient(param)
            if (param.connectTimeOut != 0L && param.readTimeOut != 0L) {
                okHttpClient = okHttpClient?.newBuilder()
                        ?.connectTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        ?.readTimeout(param.readTimeOut, TimeUnit.SECONDS)
                        ?.writeTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        ?.build()
            }
            if (param.isCacheEnabled) {
                builder.cacheControl(CacheControl.FORCE_CACHE)
            } else {
                builder.cacheControl(CacheControl.FORCE_NETWORK)
            }
            val okHttpRequest = builder.build()
            val call = okHttpClient?.newCall(okHttpRequest)
            param.analyticsListener = Callback.Analytics()
            return RxObservable.SimpleANObservable<Any>(param, call)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    class HeadRequestBuilder(param: WebParam) : GetRequestBuilder(param)

    class OptionsRequestBuilder(param: WebParam) : GetRequestBuilder(param)

    open class PostRequestBuilder(private val param: WebParam) : IProperties<PostRequestBuilder> {
        private var okHttpClient: OkHttpClient? = null

        override fun baseUrl(url: String): PostRequestBuilder {
            param.baseUrl = url
            return this
        }

        override fun queryParam(requestParam: Map<String, String>): PostRequestBuilder {
            param.queryParam = requestParam
            return this
        }

        override fun headerParam(headerParam: Map<String, String>): PostRequestBuilder {
            param.headerParam = headerParam
            return this
        }

        override fun callback(callback: OnWebCallback): PostRequestBuilder {
            param.callback = callback
            return this
        }

        override fun callback(callback: OnWebCallback, success: Class<*>, error: Class<*>): PostRequestBuilder {
            param.callback = callback
            param.model = success
            param.error = error
            return this
        }

        override fun taskId(taskId: Int): PostRequestBuilder {
            param.taskId = taskId
            return this
        }

        override fun timeOut(connectTimeOut: Long, readTimeOut: Long): PostRequestBuilder {
            param.connectTimeOut = connectTimeOut
            param.readTimeOut = readTimeOut
            return this
        }

        override fun progressDialog(dialog: Dialog): PostRequestBuilder {
            param.dialog = dialog
            return this
        }

        override fun cache(isCache: Boolean): PostRequestBuilder {
            param.isCacheEnabled = isCache
            return this
        }

        override fun analyticsListener(callback: AnalyticsListener): PostRequestBuilder {
            param.analyticsListener = callback
            return this
        }

        fun bodyParam(requestParam: Map<String, Any>): PostRequestBuilder {
            param.requestParam = requestParam
            return this
        }

        override fun connect() {
            performPostRequest().subscribe(Callback.PostRequestCallback(param))
        }

        fun performPostRequest(): Observable<*> {
            val JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8")
            var baseUrl = ApiConfiguration.getBaseUrl()
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl
            }
            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl + this.param.url)?.newBuilder()

            val entries = param.queryParam.entries
            for ((name, value1) in entries) {
                val value = value1
                urlBuilder?.addQueryParameter(name, value)
            }
            builder.url(urlBuilder?.build().toString())

            val headerBuilder = Headers.Builder()
            for ((key, value) in param.headerParam) {
                headerBuilder.add(key, value)
            }
            builder.headers(headerBuilder.build())

            var requestBody: RequestBody? = null
            when (param.httpType) {
                WebParam.HttpType.POST -> {
                    requestBody = RequestBody.create(JSON_MEDIA_TYPE, Gson().toJson(param.requestParam))
                    requestBody?.let {
                        builder = builder.post(requestBody!!)
                    }
                }
                WebParam.HttpType.PUT -> {
                    requestBody = RequestBody.create(JSON_MEDIA_TYPE, Gson().toJson(param.requestParam))
                    requestBody?.let {
                        builder = builder.put(requestBody!!)
                    }
                }
                WebParam.HttpType.DELETE -> {
                    requestBody = RequestBody.create(JSON_MEDIA_TYPE, Gson().toJson(param.requestParam))
                    requestBody?.let {
                        builder = builder.delete(requestBody)
                    }
                }
                WebParam.HttpType.PATCH -> {
                    requestBody = RequestBody.create(JSON_MEDIA_TYPE, Gson().toJson(param.requestParam))
                    requestBody?.let {
                        builder = builder.patch(requestBody)
                    }
                }
                else -> {
                }
            }
            requestBody?.let {
                param.requestBodyContentlength = requestBody.contentLength()
            }

            okHttpClient = HTTPManager.get().getDefaultOkHttpClient(this.param)
            if (param.connectTimeOut != 0L && param.readTimeOut != 0L) {
                okHttpClient = okHttpClient?.newBuilder()
                        ?.connectTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        ?.readTimeout(param.readTimeOut, TimeUnit.SECONDS)
                        ?.writeTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        ?.build()
            }

            if (param.isCacheEnabled) {
                builder.cacheControl(CacheControl.FORCE_CACHE)
            } else {
                builder.cacheControl(CacheControl.FORCE_NETWORK)
            }
            val okHttpRequest = builder.build()
            val call = okHttpClient?.newCall(okHttpRequest)
            param.analyticsListener = Callback.Analytics()
            return RxObservable.SimpleANObservable<Any>(param, call)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    class PutRequestBuilder(param: WebParam) : PostRequestBuilder(param)

    class DeleteRequestBuilder(param: WebParam) : PostRequestBuilder(param)

    class PatchRequestBuilder(param: WebParam) : PostRequestBuilder(param)

    class DownloadBuilder(private val param: WebParam) : IProperties<DownloadBuilder> {
        private var okHttpClient: OkHttpClient? = null

        override fun baseUrl(url: String): DownloadBuilder {
            param.baseUrl = url
            return this
        }

        override fun headerParam(headerParam: Map<String, String>): DownloadBuilder {
            param.headerParam = headerParam
            return this
        }

        override fun callback(callback: OnWebCallback): DownloadBuilder {
            param.callback = callback
            return this
        }

        override fun callback(callback: OnWebCallback, success: Class<*>, error: Class<*>): DownloadBuilder {
            param.callback = callback
            param.model = success
            param.error = error
            return this
        }

        override fun taskId(taskId: Int): DownloadBuilder {
            param.taskId = taskId
            return this
        }

        override fun timeOut(connectTimeOut: Long, readTimeOut: Long): DownloadBuilder {
            param.connectTimeOut = connectTimeOut
            param.readTimeOut = readTimeOut
            return this
        }

        override fun progressDialog(dialog: Dialog): DownloadBuilder {
            param.dialog = dialog
            return this
        }

        override fun cache(isCache: Boolean): DownloadBuilder {
            param.isCacheEnabled = isCache
            return this
        }

        override fun analyticsListener(callback: AnalyticsListener): DownloadBuilder {
            param.analyticsListener = callback
            return this
        }

        override fun queryParam(requestParam: Map<String, String>): DownloadBuilder {
            param.queryParam = requestParam
            return this
        }

        fun progressListener(callback: ProgressListener): DownloadBuilder {
            param.progressListener = callback
            return this
        }

        fun file(file: File): DownloadBuilder {
            param.file = file
            return this
        }

        override fun connect() {
            performDownloadRequest()
        }


        internal fun performDownloadRequest(): Observable<*> {
            var baseUrl = ApiConfiguration.getBaseUrl()
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl
            }
            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl + param.url)?.newBuilder()
            val entries = param.queryParam.entries
            for ((name, value1) in entries) {
                val value = value1
                urlBuilder?.addQueryParameter(name, value)
            }
            builder.url(urlBuilder?.build().toString())

            val headerBuilder = Headers.Builder()
            for ((key, value) in param.headerParam) {
                headerBuilder.add(key, value)
            }
            builder.headers(headerBuilder.build())

            when (param.httpType) {
                WebParam.HttpType.DOWNLOAD -> {
                    builder = builder.get()
                }
                else -> {
                }
            }
            okHttpClient = HTTPManager.get().getDefaultOkHttpClient(param)
            if (param.connectTimeOut != 0L && param.readTimeOut != 0L) {
                okHttpClient = okHttpClient?.newBuilder()
                        ?.connectTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        ?.readTimeout(param.readTimeOut, TimeUnit.SECONDS)
                        ?.writeTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        ?.build()
            }

            if (param.isCacheEnabled) {
                builder.cacheControl(CacheControl.FORCE_CACHE)
            } else {
                builder.cacheControl(CacheControl.FORCE_NETWORK)
            }
            okHttpClient = okHttpClient?.newBuilder()?.addNetworkInterceptor { chain ->
                val originalResponse = chain.proceed(chain.request())
                originalResponse.newBuilder()
                        .body(originalResponse.body()?.let { HTTPInternalNetworking.ProgressResponseBody(it, param) })
                        .build()
            }?.build()
            val okHttpRequest = builder.build()
            val call = okHttpClient?.newCall(okHttpRequest)
            param.analyticsListener = Callback.Analytics()
            return RxObservable.DownloadANObservable<Any>(param, call)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    class MultiPartBuilder(private val param: WebParam) : IProperties<MultiPartBuilder> {
        private var okHttpClient: OkHttpClient? = null

        init {
            param.debug = true
        }

        override fun baseUrl(url: String): MultiPartBuilder {
            param.baseUrl = url
            return this
        }

        override fun headerParam(headerParam: Map<String, String>): MultiPartBuilder {
            param.headerParam = headerParam
            return this
        }

        override fun callback(callback: OnWebCallback): MultiPartBuilder {
            param.callback = callback
            return this
        }

        override fun callback(callback: OnWebCallback, success: Class<*>, error: Class<*>): MultiPartBuilder {
            param.callback = callback
            param.model = success
            param.error = error
            return this
        }

        override fun taskId(taskId: Int): MultiPartBuilder {
            param.taskId = taskId
            return this
        }

        override fun timeOut(connectTimeOut: Long, readTimeOut: Long): MultiPartBuilder {
            param.connectTimeOut = connectTimeOut
            param.readTimeOut = readTimeOut
            return this
        }

        override fun progressDialog(dialog: Dialog): MultiPartBuilder {
            param.dialog = dialog
            return this
        }

        override fun cache(isCache: Boolean): MultiPartBuilder {
            param.isCacheEnabled = isCache
            return this
        }

        override fun analyticsListener(callback: AnalyticsListener): MultiPartBuilder {
            param.analyticsListener = callback
            return this
        }

        override fun queryParam(requestParam: Map<String, String>): MultiPartBuilder {
            param.queryParam = requestParam
            return this
        }

        fun multipartParam(multipartParam: Map<String, String>): MultiPartBuilder {
            param.multipartParam = multipartParam
            return this
        }

        fun multipartParamFile(multipartFile: Map<String, File>): MultiPartBuilder {
            param.multipartParamFile = multipartFile
            return this
        }

        fun progressListener(callback: ProgressListener): MultiPartBuilder {
            param.progressListener = callback
            return this
        }

        fun logging(isLog: Boolean): MultiPartBuilder {
            param.debug = isLog
            return this
        }

        override fun connect() {
            performMultipartRequest().subscribe(Callback.UploadRequestCallback(param))
        }

        fun performMultipartRequest(): Observable<*> {
            var baseUrl = ApiConfiguration.getBaseUrl()
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl
            }

            val builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl + param.url)?.newBuilder()
            val entries = param.queryParam.entries
            for ((name, value1) in entries) {
                val value = value1
                urlBuilder?.addQueryParameter(name, value)
            }
            builder.url(urlBuilder?.build().toString())

            val headerBuilder = Headers.Builder()
            for ((key, value) in param.headerParam) {
                headerBuilder.add(key, value)
            }
            builder.headers(headerBuilder.build())

            var requestBody: RequestBody? = null
            when (param.httpType) {
                WebParam.HttpType.MULTIPART -> {
                    val multipartBuilder = MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                    try {
                        for ((key, value) in param.multipartParam) {
                            val body = RequestBody.create(null, value)
                            //                            multipartBuilder.addPart(Headers.of("Content-Disposition",
                            //                                    "form-data; name=\"" + entry.getKey() + "\""),
                            //                                    body);
                            multipartBuilder.addFormDataPart(key, key, body)
                        }
                        for ((key, value) in param.multipartParamFile) {

                            val uri = Uri.fromFile(value)
                            val cR = param.context?.contentResolver
                            val mime = cR?.getType(uri)
                            val fileBody = RequestBody.create(MediaType.parse(mime),
                                    value)
                            //                            multipartBuilder.addPart(Headers.of("Content-Disposition",
                            //                                    "form-data; name=\"" + entry.getKey() + "\"; filename=\"" + fileName + "\""),
                            //                                    fileBody);
                            multipartBuilder.addFormDataPart(key, key, fileBody)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    requestBody = multipartBuilder.build()
                }
                else -> {
                }
            }
            requestBody?.let {
                param.requestBodyContentlength = requestBody.contentLength()
            }

            if (param.isCacheEnabled) {
                builder.cacheControl(CacheControl.FORCE_CACHE)
            } else {
                builder.cacheControl(CacheControl.FORCE_NETWORK)
            }
            okHttpClient = HTTPManager.get().getDefaultOkHttpClient(param)
            if (param.connectTimeOut != 0L && param.readTimeOut != 0L) {
                okHttpClient = okHttpClient?.newBuilder()
                        ?.connectTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        ?.readTimeout(param.readTimeOut, TimeUnit.SECONDS)
                        ?.writeTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        ?.build()
            }
            if (!param.debug) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.NONE
                okHttpClient = okHttpClient!!.newBuilder()
                        .addInterceptor(interceptor)
                        .build()
            }
            okHttpClient = okHttpClient?.newBuilder()
                    ?.addNetworkInterceptor { chain ->
                        val originalResponse = chain.proceed(chain.request())
                        originalResponse.newBuilder()
                                .body(originalResponse.body()?.let { HTTPInternalNetworking.ProgressResponseBody(it, param) })
                                .build()
                    }?.build()
            val okHttpRequest = builder.build()
            val call = okHttpClient?.newCall(okHttpRequest)
            param.analyticsListener = Callback.Analytics()
            return RxObservable.SimpleANObservable<Any>(param, call)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }

    }


}