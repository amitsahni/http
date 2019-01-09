package webconnect.com.webconnect

import android.app.Dialog
import android.content.ContentResolver
import android.net.Uri
import android.text.TextUtils
import android.webkit.MimeTypeMap
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import webconnect.com.webconnect.di.IProperties
import webconnect.com.webconnect.listener.*
import java.io.File
import java.util.concurrent.TimeUnit


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

        fun <T> success(onSuccessListener: OnSuccessListener<T>): GetRequestBuilder {
            param.success = onSuccessListener as OnSuccessListener<Any>
            return this
        }

        fun <T> error(onErrorListener: OnErrorListener<T>): GetRequestBuilder {
            param.err = onErrorListener as OnErrorListener<Any>
            return this
        }

        fun failure(onFailure: OnFailureListener): GetRequestBuilder {
            param.failure = onFailure
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

        override fun queryParam(queryParam: QueryMap<String, String>): GetRequestBuilder {
            param.queryParam = queryParam
            return this
        }


        fun progressListener(callback: ProgressListener): GetRequestBuilder {
            param.progressListener = callback
            return this
        }

        fun queue(): Call {
            return call()!!
        }

        override fun connect() {
            call()?.enqueue(Callback.GetRequestCallbackEnqueue(param))
        }

        private fun call(): Call? {
            var baseUrl = ApiConfiguration.getBaseUrl()
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl
            }
            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl + param.url)?.newBuilder()
            if (!param.queryParam.isEmpty()) {
                for (i in 0 until param.queryParam.key.size()) {
                    val key = param.queryParam.key[i]
                    val value = param.queryParam.value[i]
                    urlBuilder?.addQueryParameter(key, value)
                }
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
                        ?.addInterceptor {
                            val originalResponse = it.proceed(it.request())
                            val originalBody = originalResponse.body()
                            originalResponse.newBuilder()
                                    .body(HTTPInternalNetworking.ProgressResponseBody(originalBody!!, param))
                                    .build()
                        }
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
            return call
        }
    }

    class HeadRequestBuilder(param: WebParam) : GetRequestBuilder(param)

    class OptionsRequestBuilder(param: WebParam) : GetRequestBuilder(param)

    /******************************************************************************************/

    open class PostRequestBuilder(private val param: WebParam) : IProperties<PostRequestBuilder> {
        private var okHttpClient: OkHttpClient? = null

        override fun baseUrl(url: String): PostRequestBuilder {
            param.baseUrl = url
            return this
        }

        override fun queryParam(queryParam: QueryMap<String, String>): PostRequestBuilder {
            param.queryParam = queryParam
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

        fun <T> success(onSuccessListener: OnSuccessListener<T>): PostRequestBuilder {
            param.success = onSuccessListener as OnSuccessListener<Any>
            return this
        }

        fun <T> error(onErrorListener: OnErrorListener<T>): PostRequestBuilder {
            param.err = onErrorListener as OnErrorListener<Any>
            return this
        }

        fun failure(onFailure: OnFailureListener): PostRequestBuilder {
            param.failure = onFailure
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
            param.isJson = true
            return this
        }

        fun formDataParam(requestParam: Map<String, String>): PostRequestBuilder {
            param.requestParam = requestParam
            return this
        }

        fun progressListener(callback: ProgressListener): PostRequestBuilder {
            param.progressListener = callback
            return this
        }

        fun multipart(): MultiPartBuilder {
            return BuilderRequest.MultiPartBuilder(param)
        }

        fun queue(): Call {
            return call()!!
        }

        override fun connect() {
            call()?.enqueue(Callback.PostRequestCallbackEnqueue(param))
        }

        private fun call(): Call? {
            val JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8")
            var FORM_ENCODED_TYPE = MediaType.parse("application/x-www-form-urlencoded")
            var baseUrl = ApiConfiguration.getBaseUrl()
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl
            }
            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl + this.param.url)?.newBuilder()
            if (!param.queryParam.isEmpty()) {
                for (i in 0 until param.queryParam.key.size()) {
                    val key = param.queryParam.key[i]
                    val value = param.queryParam.value[i]
                    urlBuilder?.addQueryParameter(key, value)
                }
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
                    if (!param.isJson) {
                        requestBody = RequestBody.create(FORM_ENCODED_TYPE, HTTPManager.get().convertFormData(param.requestParam as MutableMap<String, String>))
                    } else {
                        requestBody = RequestBody.create(JSON_MEDIA_TYPE, ApiConfiguration.getGson().toJson(param.requestParam))
                    }
                    requestBody?.also {
                        builder = builder.post(it)
                    }
                }
                WebParam.HttpType.PUT -> {
                    if (!param.isJson) {
                        requestBody = RequestBody.create(FORM_ENCODED_TYPE, HTTPManager.get().convertFormData(param.requestParam as MutableMap<String, String>))
                    } else {
                        requestBody = RequestBody.create(JSON_MEDIA_TYPE, ApiConfiguration.getGson().toJson(param.requestParam))
                    }
                    requestBody?.also {
                        builder = builder.put(it)
                    }
                }
                WebParam.HttpType.DELETE -> {
                    if (!param.isJson) {
                        requestBody = RequestBody.create(FORM_ENCODED_TYPE, HTTPManager.get().convertFormData(param.requestParam as MutableMap<String, String>))
                    } else {
                        requestBody = RequestBody.create(JSON_MEDIA_TYPE, ApiConfiguration.getGson().toJson(param.requestParam))
                    }
                    requestBody?.also {
                        builder = builder.delete(it)
                    }
                }
                WebParam.HttpType.PATCH -> {
                    if (!param.isJson) {
                        requestBody = RequestBody.create(FORM_ENCODED_TYPE, HTTPManager.get().convertFormData(param.requestParam as MutableMap<String, String>))
                    } else {
                        requestBody = RequestBody.create(JSON_MEDIA_TYPE, ApiConfiguration.getGson().toJson(param.requestParam))
                    }
                    requestBody?.also {
                        builder = builder.patch(it)
                    }
                }
                else -> {
                }
            }
            requestBody?.also {
                param.requestBodyContentlength = it.contentLength()
            }

            okHttpClient = HTTPManager.get().getDefaultOkHttpClient(this.param)
            if (param.connectTimeOut != 0L && param.readTimeOut != 0L) {
                okHttpClient = okHttpClient?.newBuilder()
                        ?.connectTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        ?.readTimeout(param.readTimeOut, TimeUnit.SECONDS)
                        ?.writeTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        ?.addInterceptor {
                            val originalResponse = it.proceed(it.request())
                            val originalBody = originalResponse.body()
                            originalResponse.newBuilder()
                                    .body(HTTPInternalNetworking.ProgressResponseBody(originalBody!!, param))
                                    .build()
                        }
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
            return call!!
        }
    }

    class PutRequestBuilder(param: WebParam) : PostRequestBuilder(param)

    class DeleteRequestBuilder(param: WebParam) : PostRequestBuilder(param)

    class PatchRequestBuilder(param: WebParam) : PostRequestBuilder(param)

    /******************************************************************************************/

    open class DownloadBuilder(val param: WebParam) : IProperties<DownloadBuilder> {
        private var okHttpClient: OkHttpClient? = null
        private val JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8")
        private var FORM_ENCODED_TYPE = MediaType.parse("application/x-www-form-urlencoded")
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

        fun success(onSuccessListener: OnSuccessListener<File>): DownloadBuilder {
            param.success = onSuccessListener as OnSuccessListener<Any>
            return this
        }

        fun error(onErrorListener: OnErrorListener<String>): DownloadBuilder {
            param.err = onErrorListener as OnErrorListener<Any>
            return this
        }

        fun failure(onFailure: OnFailureListener): DownloadBuilder {
            param.failure = onFailure
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

        override fun queryParam(queryParam: QueryMap<String, String>): DownloadBuilder {
            param.queryParam = queryParam
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

        fun queue(): Call {
            return call()!!
        }

        override fun connect() {
            call()?.enqueue(Callback.DownloadRequestCallbackEnqueue(param))
        }


        private fun call(): Call? {
            var baseUrl = ApiConfiguration.getBaseUrl()
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl
            }
            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl + param.url)?.newBuilder()
            if (!param.queryParam.isEmpty()) {
                for (i in 0 until param.queryParam.key.size()) {
                    val key = param.queryParam.key[i]
                    val value = param.queryParam.value[i]
                    urlBuilder?.addQueryParameter(key, value)
                }
            }
            builder.url(urlBuilder?.build().toString())

            val headerBuilder = Headers.Builder()
            for ((key, value) in param.headerParam) {
                headerBuilder.add(key, value)
            }
            builder.headers(headerBuilder.build())
            var requestBody: RequestBody? = null
            when (param.httpType) {
                WebParam.HttpType.GET -> {
                    builder = builder.get()
                }
                WebParam.HttpType.POST -> {
                    if (!param.isJson) {
                        requestBody = RequestBody.create(FORM_ENCODED_TYPE, HTTPManager.get().convertFormData(param.requestParam as MutableMap<String, String>))
                    } else {
                        requestBody = RequestBody.create(JSON_MEDIA_TYPE, ApiConfiguration.getGson().toJson(param.requestParam))
                    }
                    requestBody?.also {
                        builder = builder.post(it)
                    }
                }
                WebParam.HttpType.PUT -> {
                    if (!param.isJson) {
                        requestBody = RequestBody.create(FORM_ENCODED_TYPE, HTTPManager.get().convertFormData(param.requestParam as MutableMap<String, String>))
                    } else {
                        requestBody = RequestBody.create(JSON_MEDIA_TYPE, ApiConfiguration.getGson().toJson(param.requestParam))
                    }
                    requestBody?.also {
                        builder = builder.put(it)
                    }
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
                        ?.addInterceptor {
                            val originalResponse = it.proceed(it.request())
                            val originalBody = originalResponse.body()
                            originalResponse.newBuilder()
                                    .body(HTTPInternalNetworking.ProgressResponseBody(originalBody!!, param))
                                    .build()
                        }
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
            return call
        }
    }

    open class DownloadBuilderPost(param: WebParam) : DownloadBuilder(param) {

        fun bodyParam(requestParam: Map<String, Any>): DownloadBuilderPost {
            param.requestParam = requestParam
            param.isJson = true
            return this
        }

        fun formDataParam(requestParam: Map<String, String>): DownloadBuilderPost {
            param.requestParam = requestParam
            return this
        }
    }


    class DownloadBuilderPut(param: WebParam) : DownloadBuilderPost(param)

    /******************************************************************************************/

    class MultiPartBuilder(private val param: WebParam) : IProperties<MultiPartBuilder> {
        private var okHttpClient: OkHttpClient? = null

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

        fun <T> success(onSuccessListener: OnSuccessListener<T>): MultiPartBuilder {
            param.success = onSuccessListener as OnSuccessListener<Any>
            return this
        }

        fun <T> error(onErrorListener: OnErrorListener<T>): MultiPartBuilder {
            param.err = onErrorListener as OnErrorListener<Any>
            return this
        }

        fun failure(onFailure: OnFailureListener): MultiPartBuilder {
            param.failure = onFailure
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

        override fun queryParam(queryParam: QueryMap<String, String>): MultiPartBuilder {
            param.queryParam = queryParam
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

        fun multipartParamListFile(multipartFile: Map<String, List<File>>): MultiPartBuilder {
            param.multipartParamListFile = multipartFile
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

        fun queue(): Call {
            return call()!!
        }

        override fun connect() {
            call()?.enqueue(Callback.PostRequestCallbackEnqueue(param))
        }

        fun call(): Call? {
            var baseUrl = ApiConfiguration.getBaseUrl()
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl
            }

            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl + param.url)?.newBuilder()
            if (!param.queryParam.isEmpty()) {
                for (i in 0 until param.queryParam.key.size()) {
                    val key = param.queryParam.key[i]
                    val value = param.queryParam.value[i]
                    urlBuilder?.addQueryParameter(key, value)
                }
            }
            builder.url(urlBuilder?.build().toString())

            val headerBuilder = Headers.Builder()
            for ((key, value) in param.headerParam) {
                headerBuilder.add(key, value)
            }
            builder.headers(headerBuilder.build())
            val multipartBuilder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
            val JSON_MEDIA_TYPE = MediaType.parse("multipart/form-data")
            try {
                for ((key, value) in param.multipartParam) {
                    val body = RequestBody.create(JSON_MEDIA_TYPE, value)
                    val disposition = StringBuilder("form-data; name=")
                    disposition.append(key)
                    var headers = Headers.of("Content-Disposition", disposition.toString())
                    var part = MultipartBody.Part.createFormData(key, value);
//                    multipartBuilder.addPart(headers, body)
                    multipartBuilder.addPart(part)
                }
//                val body = RequestBody.create(JSON_MEDIA_TYPE, Gson().toJson(param.multipartParam))
//                multipartBuilder.addPart(body)
                for ((key, value) in param.multipartParamFile) {

                    val uri = Uri.fromFile(value)
                    var mimeType: String
                    if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                        val cR = param.context?.contentResolver
                        mimeType = cR?.getType(uri).toString()
                    } else {
                        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                                .toString())
                        mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                fileExtension.toLowerCase())
                    }
                    val fileBody = RequestBody.create(MediaType.parse(mimeType),
                            value)
                    val disposition = StringBuilder("form-data; name=")
                    disposition.append(key)
                    disposition.append("; filename=")
                    disposition.append(value.name)
                    var headers = Headers.of("Content-Disposition", disposition.toString())
                    var part = MultipartBody.Part.createFormData(key, value.name, fileBody);
                    //multipartBuilder.addPart(MultipartBody.Part.create(headers, fileBody))
                    multipartBuilder.addPart(part)
                }
                for ((key, file) in param.multipartParamListFile) {
                    for (value in file) {
                        val uri = Uri.fromFile(value)
                        var mimeType: String
                        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                            val cR = param.context?.contentResolver
                            mimeType = cR?.getType(uri).toString()
                        } else {
                            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                                    .toString())
                            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                    fileExtension.toLowerCase())
                        }
                        val fileBody = RequestBody.create(MediaType.parse(mimeType),
                                value)
                        val disposition = StringBuilder("form-data; name=")
                        disposition.append(key)
                        disposition.append("; filename=")
                        disposition.append(value.name)
                        var headers = Headers.of("Content-Disposition", disposition.toString())
                        var part = MultipartBody.Part.createFormData(key, value.name, fileBody);
                        //multipartBuilder.addPart(MultipartBody.Part.create(headers, fileBody))
                        multipartBuilder.addPart(part)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            when (param.httpType) {
                WebParam.HttpType.POST -> {
                    multipartBuilder.build()?.also {
                        builder = builder.post(it)
                        param.requestBodyContentlength = it.contentLength()
                    }
                }
                WebParam.HttpType.PUT -> {
                    multipartBuilder.build()?.also {
                        builder = builder.put(it)
                        param.requestBodyContentlength = it.contentLength()
                    }
                }
                WebParam.HttpType.DELETE -> {
                    multipartBuilder.build()?.also {
                        builder = builder.delete(it)
                        param.requestBodyContentlength = it.contentLength()
                    }
                }
                WebParam.HttpType.PATCH -> {
                    multipartBuilder.build()?.also {
                        builder = builder.patch(it)
                        param.requestBodyContentlength = it.contentLength()
                    }
                }
                else -> {
                }
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
                        ?.addInterceptor {
                            val originalResponse = it.proceed(it.request())
                            val originalBody = originalResponse.body()
                            originalResponse.newBuilder()
                                    .body(HTTPInternalNetworking.ProgressResponseBody(originalBody!!, param))
                                    .build()
                        }
                        ?.build()
            }
            if (!param.debug) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.NONE
                okHttpClient = okHttpClient?.newBuilder()
                        ?.addInterceptor(interceptor)
                        ?.build()
            }
            val okHttpRequest = builder.build()
            val call = okHttpClient?.newCall(okHttpRequest)
            param.analyticsListener = Callback.Analytics()
            return call
        }

    }


}
