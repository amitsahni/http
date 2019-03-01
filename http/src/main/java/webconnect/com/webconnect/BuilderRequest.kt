package webconnect.com.webconnect

import android.content.ContentResolver
import android.net.Uri
import android.text.TextUtils
import android.webkit.MimeTypeMap
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import webconnect.com.webconnect.di.IProperties
import webconnect.com.webconnect.listener.*
import webconnect.com.webconnect.model.ErrorModel
import webconnect.com.webconnect.model.SuccessModel
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * Created by clickapps on 27/12/17.
 */
class BuilderRequest {

    open class GetRequestBuilder(private val param: WebParam) {

        private var okHttpClient: OkHttpClient? = ApiConfiguration.okHttpClient
        fun baseUrl(url: String): GetRequestBuilder {
            param.baseUrl = url
            return this
        }

        fun headerParam(headerParam: Map<String, String>): GetRequestBuilder {
            param.headerParam = headerParam
            return this
        }

        fun analyticsListener(callback: AnalyticsListener): GetRequestBuilder {
            param.analyticsListener = callback
            return this
        }

        fun timeOut(connectTimeOut: Long, readTimeOut: Long): GetRequestBuilder {
            param.connectTimeOut = connectTimeOut
            param.readTimeOut = readTimeOut
            return this
        }

        fun cache(isCache: Boolean): GetRequestBuilder {
            param.isCacheEnabled = isCache
            return this
        }

        fun queryParam(queryParam: QueryMap<String, String>): GetRequestBuilder {
            param.queryParam = queryParam
            return this
        }

        fun queryParam(queryParam: Map<String, String>): GetRequestBuilder {
            param.query = queryParam
            return this
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : SuccessModel> success(model: Class<T>, t: (T) -> Unit): GetRequestBuilder {
            param.model = model
            val success = object : OnSuccessListener<T> {
                override fun onSuccess(model: T) {
                    t(model)
                }
            }
            param.success = success as OnSuccessListener<Any>
            return this
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : ErrorModel> error(model: Class<T>, t: (T) -> Unit): GetRequestBuilder {
            param.error = model
            val error = object : OnErrorListener<T> {
                override fun onError(model: T) {
                    t(model)
                }
            }
            param.err = error as OnErrorListener<Any>
            return this
        }

        fun failure(t: (Exception, String) -> Unit): GetRequestBuilder {
            val failure = object : OnFailureListener {
                override fun onFailure(e: Exception, msg: String) {
                    t(e, msg)
                }
            }
            param.failure = failure
            return this
        }

        fun response(t: (String) -> Unit): GetRequestBuilder {
            val response = object : ResponseListener {
                override fun response(string: String) {
                    t(string)
                }
            }
            param.responseListener = response
            return this
        }

        fun loader(t: (Boolean) -> Unit): GetRequestBuilder {
            val loader = object : LoaderListener {
                override fun loader(isShowing: Boolean) {
                    t(isShowing)
                }
            }
            param.loaderListener = loader
            return this
        }

        fun progressListener(t: (Long, Long, Float) -> Unit): GetRequestBuilder {
            val process = object : ProgressListener {
                override fun onProgress(bytesRead: Long, contentLength: Long, progress: Float) {
                    t(bytesRead, contentLength, progress)
                }
            }
            param.progressListener = process
            return this
        }

        fun queue(): Call {
            return call()!!
        }

        fun connect() {
            call()?.enqueue(Callback.GetRequestCallbackEnqueue(param))
        }

        private fun call(): Call? {
            var baseUrl = ApiConfiguration.baseUrl
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl.toString()
            }
            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl + param.url)?.newBuilder()
            if (!param.query.isEmpty()) {
                param.query.forEach { (key, value) ->
                    urlBuilder?.addQueryParameter(key, value)
                }
            }
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
        private var okHttpClient: OkHttpClient? = ApiConfiguration.okHttpClient

        override fun baseUrl(url: String): PostRequestBuilder {
            param.baseUrl = url
            return this
        }

        override fun queryParam(queryParam: QueryMap<String, String>): PostRequestBuilder {
            param.queryParam = queryParam
            return this
        }

        fun queryParam(queryParam: Map<String, String>): PostRequestBuilder {
            param.query = queryParam
            return this
        }

        override fun headerParam(headerParam: Map<String, String>): PostRequestBuilder {
            param.headerParam = headerParam
            return this
        }

        fun <T : SuccessModel> success(t: Class<T>, onSuccessListener: OnSuccessListener<T>): PostRequestBuilder {
            param.model = t
            param.success = onSuccessListener as OnSuccessListener<Any>
            return this
        }

        fun <T : ErrorModel> error(t: Class<T>, onErrorListener: OnErrorListener<T>): PostRequestBuilder {
            param.error = t
            param.err = onErrorListener as OnErrorListener<Any>
            return this
        }

        fun loader(loaderListener: LoaderListener): PostRequestBuilder {
            param.loaderListener = loaderListener
            return this
        }

        fun failure(onFailure: OnFailureListener): PostRequestBuilder {
            param.failure = onFailure
            return this
        }

        fun response(responseListener: ResponseListener): PostRequestBuilder {
            param.responseListener = responseListener
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

        // Higher Order function
        fun <T : SuccessModel> success(model: Class<T>, t: (T) -> Unit): PostRequestBuilder {
            param.model = model
            val success = object : OnSuccessListener<T> {
                override fun onSuccess(model: T) {
                    t(model)
                }
            }
            param.success = success as OnSuccessListener<Any>
            return this
        }

        fun <T : ErrorModel> error(model: Class<T>, t: (T) -> Unit): PostRequestBuilder {
            param.error = model
            val error = object : OnErrorListener<T> {
                override fun onError(model: T) {
                    t(model)
                }
            }
            param.err = error as OnErrorListener<Any>
            return this
        }

        fun failure(t: (Exception, String) -> Unit): PostRequestBuilder {
            val failure = object : OnFailureListener {
                override fun onFailure(e: Exception, msg: String) {
                    t(e, msg)
                }
            }
            param.failure = failure
            return this
        }

        fun response(t: (String) -> Unit): PostRequestBuilder {
            val response = object : ResponseListener {
                override fun response(string: String) {
                    t(string)
                }
            }
            param.responseListener = response
            return this
        }

        fun loader(t: (Boolean) -> Unit): PostRequestBuilder {
            val loader = object : LoaderListener {
                override fun loader(isShowing: Boolean) {
                    t(isShowing)
                }
            }
            param.loaderListener = loader
            return this
        }

        fun progressListener(t: (Long, Long, Float) -> Unit): PostRequestBuilder {
            val process = object : ProgressListener {
                override fun onProgress(bytesRead: Long, contentLength: Long, progress: Float) {
                    t(bytesRead, contentLength, progress)
                }
            }
            param.progressListener = process
            return this
        }

        fun queue(): Call {
            return call()!!
        }

        override fun connect() {
            call()?.enqueue(Callback.PostRequestCallbackEnqueue(param))
        }

        private fun call(): Call? {
            val JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8")
            val FORM_ENCODED_TYPE = MediaType.parse("application/x-www-form-urlencoded")
            var baseUrl = ApiConfiguration.baseUrl
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl.toString()
            }
            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl + this.param.url)?.newBuilder()
            if (!param.query.isEmpty()) {
                param.query.forEach { (key, value) ->
                    urlBuilder?.addQueryParameter(key, value)
                }
            }
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
                        requestBody = RequestBody.create(FORM_ENCODED_TYPE, param.requestParam.convertFormData())
                    } else {
                        requestBody = RequestBody.create(JSON_MEDIA_TYPE, param.requestParam.toJson())
                    }
                    requestBody?.also {
                        builder = builder.post(it)
                    }
                }
                WebParam.HttpType.PUT -> {
                    if (!param.isJson) {
                        requestBody = RequestBody.create(FORM_ENCODED_TYPE, param.requestParam.convertFormData())
                    } else {
                        requestBody = RequestBody.create(JSON_MEDIA_TYPE, param.requestParam.toJson())
                    }
                    requestBody?.also {
                        builder = builder.put(it)
                    }
                }
                WebParam.HttpType.DELETE -> {
                    if (!param.isJson) {
                        requestBody = RequestBody.create(FORM_ENCODED_TYPE, param.requestParam.convertFormData())
                    } else {
                        requestBody = RequestBody.create(JSON_MEDIA_TYPE, param.requestParam.toJson())
                    }
                    requestBody?.also {
                        builder = builder.delete(it)
                    }
                }
                WebParam.HttpType.PATCH -> {
                    if (!param.isJson) {
                        requestBody = RequestBody.create(FORM_ENCODED_TYPE, param.requestParam.convertFormData())
                    } else {
                        requestBody = RequestBody.create(JSON_MEDIA_TYPE, param.requestParam.toJson())
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
        private var okHttpClient: OkHttpClient? = ApiConfiguration.okHttpClient
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


        fun success(onSuccessListener: OnSuccessListener<File>): DownloadBuilder {
            param.success = onSuccessListener as OnSuccessListener<Any>
            return this
        }

        fun <T : ErrorModel> error(t: Class<T>, onErrorListener: OnErrorListener<T>): DownloadBuilder {
            param.error = t
            param.err = onErrorListener as OnErrorListener<Any>
            return this
        }

        fun failure(onFailure: OnFailureListener): DownloadBuilder {
            param.failure = onFailure
            return this
        }

        fun loader(loaderListener: LoaderListener): DownloadBuilder {
            param.loaderListener = loaderListener
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

        fun queryParam(queryParam: Map<String, String>): DownloadBuilder {
            param.query = queryParam
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


        // Higher Order function
        fun success(t: (File) -> Unit): DownloadBuilder {
            param.model = t.javaClass.enclosingClass!!
            val success = object : OnSuccessListener<File> {
                override fun onSuccess(file: File) {
                    t(file)
                }
            }
            param.success = success as OnSuccessListener<Any>
            return this
        }

        fun <T : ErrorModel> error(model: Class<T>, t: (T) -> Unit): DownloadBuilder {
            param.error = model
            val error = object : OnErrorListener<T> {
                override fun onError(model: T) {
                    t(model)
                }
            }
            param.err = error as OnErrorListener<Any>
            return this
        }

        fun failure(t: (Exception, String) -> Unit): DownloadBuilder {
            val failure = object : OnFailureListener {
                override fun onFailure(e: Exception, msg: String) {
                    t(e, msg)
                }
            }
            param.failure = failure
            return this
        }

        fun loader(t: (Boolean) -> Unit): DownloadBuilder {
            val loader = object : LoaderListener {
                override fun loader(isShowing: Boolean) {
                    t(isShowing)
                }
            }
            param.loaderListener = loader
            return this
        }

        fun progressListener(t: (Long, Long, Float) -> Unit): DownloadBuilder {
            val process = object : ProgressListener {
                override fun onProgress(bytesRead: Long, contentLength: Long, progress: Float) {
                    t(bytesRead, contentLength, progress)
                }
            }
            param.progressListener = process
            return this
        }

        fun queue(): Call {
            return call()!!
        }

        override fun connect() {
            call()?.enqueue(Callback.DownloadRequestCallbackEnqueue(param))
        }


        private fun call(): Call? {
            var baseUrl = ApiConfiguration.baseUrl
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl.toString()
            }
            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl + param.url)?.newBuilder()
            if (!param.query.isEmpty()) {
                param.query.forEach { (key, value) ->
                    urlBuilder?.addQueryParameter(key, value)
                }
            }
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
                        requestBody = RequestBody.create(FORM_ENCODED_TYPE, param.requestParam.convertFormData())
                    } else {
                        requestBody = RequestBody.create(JSON_MEDIA_TYPE, param.requestParam.toJson())
                    }
                    requestBody?.also {
                        builder = builder.post(it)
                    }
                }
                WebParam.HttpType.PUT -> {
                    if (!param.isJson) {
                        requestBody = RequestBody.create(FORM_ENCODED_TYPE, param.requestParam.convertFormData())
                    } else {
                        requestBody = RequestBody.create(JSON_MEDIA_TYPE, param.requestParam.toJson())
                    }
                    requestBody?.also {
                        builder = builder.put(it)
                    }
                }
                else -> {
                }
            }
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
        private var okHttpClient: OkHttpClient? = ApiConfiguration.okHttpClient

        override fun baseUrl(url: String): MultiPartBuilder {
            param.baseUrl = url
            return this
        }

        override fun headerParam(headerParam: Map<String, String>): MultiPartBuilder {
            param.headerParam = headerParam
            return this
        }

        fun <T : SuccessModel> success(t: Class<T>, onSuccessListener: OnSuccessListener<T>): MultiPartBuilder {
            param.model = t
            param.success = onSuccessListener as OnSuccessListener<Any>
            return this
        }

        fun <T : ErrorModel> error(t: Class<T>, onErrorListener: OnErrorListener<T>): MultiPartBuilder {
            param.error = t
            param.err = onErrorListener as OnErrorListener<Any>
            return this
        }

        fun failure(onFailure: OnFailureListener): MultiPartBuilder {
            param.failure = onFailure
            return this
        }

        fun loader(loaderListener: LoaderListener): MultiPartBuilder {
            param.loaderListener = loaderListener
            return this
        }

        fun response(responseListener: ResponseListener): MultiPartBuilder {
            param.responseListener = responseListener
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

        fun queryParam(queryParam: Map<String, String>): MultiPartBuilder {
            param.query = queryParam
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

        // Higher Order function
        fun <T : SuccessModel> success(model: Class<T>, t: (T) -> Unit): MultiPartBuilder {
            param.model = model
            val success = object : OnSuccessListener<T> {
                override fun onSuccess(model: T) {
                    t(model)
                }
            }
            param.success = success as OnSuccessListener<Any>
            return this
        }

        fun <T : ErrorModel> error(model: Class<T>, t: (T) -> Unit): MultiPartBuilder {
            param.error = model
            val error = object : OnErrorListener<T> {
                override fun onError(model: T) {
                    t(model)
                }
            }
            param.err = error as OnErrorListener<Any>
            return this
        }

        fun failure(t: (Exception, String) -> Unit): MultiPartBuilder {
            val failure = object : OnFailureListener {
                override fun onFailure(e: Exception, msg: String) {
                    t(e, msg)
                }
            }
            param.failure = failure
            return this
        }

        fun response(t: (String) -> Unit): MultiPartBuilder {
            val response = object : ResponseListener {
                override fun response(string: String) {
                    t(string)
                }
            }
            param.responseListener = response
            return this
        }

        fun loader(t: (Boolean) -> Unit): MultiPartBuilder {
            val loader = object : LoaderListener {
                override fun loader(isShowing: Boolean) {
                    t(isShowing)
                }
            }
            param.loaderListener = loader
            return this
        }

        fun progressListener(t: (Long, Long, Float) -> Unit): MultiPartBuilder {
            val process = object : ProgressListener {
                override fun onProgress(bytesRead: Long, contentLength: Long, progress: Float) {
                    t(bytesRead, contentLength, progress)
                }
            }
            param.progressListener = process
            return this
        }

        fun queue(): Call {
            return call()!!
        }

        override fun connect() {
            call()?.enqueue(Callback.PostRequestCallbackEnqueue(param))
        }

        private fun call(): Call? {
            var baseUrl = ApiConfiguration.baseUrl
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl.toString()
            }

            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl + param.url)?.newBuilder()
            if (!param.query.isEmpty()) {
                param.query.forEach { (key, value) ->
                    urlBuilder?.addQueryParameter(key, value)
                }
            }
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
                    val part = MultipartBody.Part.createFormData(key, value);
//                    multipartBuilder.addPart(headers, body)
                    multipartBuilder.addPart(part)
                }
//                val body = RequestBody.create(JSON_MEDIA_TYPE, Gson().toJson(param.multipartParam))
//                multipartBuilder.addPart(body)
                for ((key, value) in param.multipartParamFile) {
                    val uri = Uri.fromFile(value)
                    var mimeType = "application/octet-stream"
                    if (uri?.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
//                        var cR: ContentResolver ?= null
//                        if (param.context != null) {
//                            cR = param.context?.contentResolver
//                        } else if (ApiConfiguration.context != null) {
//                            cR = ApiConfiguration.context?.contentResolver
//                        }
//                        MimeTypeMap.getFileExtensionFromUrl()
//                        mimeType = cR?.getType(uri).toString()
                    } else {
                        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                                .toString())
                        mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                fileExtension.toLowerCase())!!
                    }
                    val fileBody = RequestBody.create(MediaType.parse(mimeType),
                            value)
                    val disposition = StringBuilder("form-data; name=")
                    disposition.append(key)
                    disposition.append("; filename=")
                    disposition.append(value.name)
                    var headers = Headers.of("Content-Disposition", disposition.toString())
                    val part = MultipartBody.Part.createFormData(key, value.name, fileBody);
                    //multipartBuilder.addPart(MultipartBody.Part.create(headers, fileBody))
                    multipartBuilder.addPart(part)
                }
                for ((key, file) in param.multipartParamListFile) {
                    for (value in file) {
                        val uri = Uri.fromFile(value)
                        var mimeType = "application/octet-stream"
                        if (uri?.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
//                            var cR: ContentResolver ?= null
//                            if (param.context != null) {
//                                cR = param.context?.contentResolver
//                            } else if (ApiConfiguration.context != null) {
//                                cR = ApiConfiguration.context?.contentResolver
//                            }
//                            mimeType = cR?.getType(uri).toString()
                        } else {
                            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                                    .toString())
                            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                    fileExtension.toLowerCase())!!
                        }
                        val fileBody = RequestBody.create(MediaType.parse(mimeType),
                                value)
                        val disposition = StringBuilder("form-data; name=")
                        disposition.append(key)
                        disposition.append("; filename=")
                        disposition.append(value.name)
                        var headers = Headers.of("Content-Disposition", disposition.toString())
                        val part = MultipartBody.Part.createFormData(key, value.name, fileBody);
                        //multipartBuilder.addPart(MultipartBody.Part.create(headers, fileBody))
                        multipartBuilder.addPart(part)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            when (param.httpType) {
                WebParam.HttpType.POST -> {
                    multipartBuilder.build().also {
                        builder = builder.post(it)
                        param.requestBodyContentlength = it.contentLength()
                    }
                }
                WebParam.HttpType.PUT -> {
                    multipartBuilder.build().also {
                        builder = builder.put(it)
                        param.requestBodyContentlength = it.contentLength()
                    }
                }
                WebParam.HttpType.DELETE -> {
                    multipartBuilder.build().also {
                        builder = builder.delete(it)
                        param.requestBodyContentlength = it.contentLength()
                    }
                }
                WebParam.HttpType.PATCH -> {
                    multipartBuilder.build().also {
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
