package webconnect.com.webconnect

import android.content.ContentResolver
import android.content.Context
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
        fun <T : SuccessModel> success(model: Class<T>, f: T.() -> Unit): GetRequestBuilder {
            param.model = model
            val success = object : OnSuccessListener<T> {
                override fun onSuccess(model: T) {
                    f(model)
                }
            }
            param.success = success as OnSuccessListener<Any>
            return this
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : ErrorModel> error(model: Class<T>, f: T.() -> Unit): GetRequestBuilder {
            param.error = model
            val error = object : OnErrorListener<T> {
                override fun onError(model: T) {
                    f(model)
                }
            }
            param.err = error as OnErrorListener<Any>
            return this
        }

        fun analyticsListener(f: (timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) -> Unit): GetRequestBuilder {
            val analyticsListener = object : AnalyticsListener {
                override fun onReceived(timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) {
                    f(timeTakenInMillis, bytesSent, bytesReceived, isFromCache)
                }
            }
            param.analyticsListener = analyticsListener
            return this
        }

        fun response(f: String.() -> Unit): GetRequestBuilder {
            val response = object : ResponseListener {
                override fun response(string: String) {
                    f(string)
                }
            }
            param.responseListener = response
            return this
        }

        fun loader(f: Boolean.() -> Unit): GetRequestBuilder {
            val loader = object : LoaderListener {
                override fun loader(isShowing: Boolean) {
                    f(isShowing)
                }
            }
            param.loaderListener = loader
            return this
        }

        fun failure(f: (Exception, String) -> Unit): GetRequestBuilder {
            val failure = object : OnFailureListener {
                override fun onFailure(e: Exception, msg: String) {
                    f(e, msg)
                }
            }
            param.failure = failure
            return this
        }

        fun queue(): Call? {
            return call()
        }

        fun connect() {
            call()?.enqueue(Callback.GetRequestCallbackEnqueue(param))
        }

        private fun call(): Call? {
            var baseUrl = ApiConfiguration.baseUrl
            if (!param.baseUrl.isEmpty()) {
                baseUrl = param.baseUrl
            }
            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl.plus(param.url))?.newBuilder()
            param.query.forEach { (key, value) ->
                urlBuilder?.addQueryParameter(key, value)
            }
            for (i in 0 until param.queryParam.key.size()) {
                val key = param.queryParam.key[i]
                val value = param.queryParam.value[i]
                urlBuilder?.addQueryParameter(key, value)
            }
            builder.url(urlBuilder?.build().toString())

            val headerBuilder = Headers.Builder()
            param.headerParam.forEach { (key, value) ->
                headerBuilder.add(key, value)
            }
            builder.headers(headerBuilder.build())

            builder = when (param.httpType) {
                WebParam.HttpType.GET -> {
                    builder.get()
                }
                WebParam.HttpType.HEAD -> {
                    builder.head()
                }
                WebParam.HttpType.OPTIONS -> {
                    builder.method("OPTIONS", null)
                }
                else -> {
                    builder.get()
                }
            }
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
            return okHttpClient?.newCall(builder.build())
        }
    }

    class HeadRequestBuilder(param: WebParam) : GetRequestBuilder(param)

    class OptionsRequestBuilder(param: WebParam) : GetRequestBuilder(param)

    /******************************************************************************************/

    open class PostRequestBuilder(private val param: WebParam) {
        private var okHttpClient: OkHttpClient? = ApiConfiguration.okHttpClient

//        fun multipart(): MultiPartBuilder {
//            return BuilderRequest.MultiPartBuilder(param)
//        }

        fun baseUrl(url: String): PostRequestBuilder {
            param.baseUrl = url
            return this
        }

        fun queryParam(queryParam: QueryMap<String, String>): PostRequestBuilder {
            param.queryParam = queryParam
            return this
        }

        fun queryParam(queryParam: Map<String, String>): PostRequestBuilder {
            param.query = queryParam
            return this
        }

        fun headerParam(headerParam: Map<String, String>): PostRequestBuilder {
            param.headerParam = headerParam
            return this
        }

        fun timeOut(connectTimeOut: Long, readTimeOut: Long): PostRequestBuilder {
            param.connectTimeOut = connectTimeOut
            param.readTimeOut = readTimeOut
            return this
        }

        fun bodyParam(requestParam: Map<String, Any>): PostRequestBuilder {
            param.requestParam = requestParam
            param.isJson = true
            return this
        }

        fun formDataParam(requestParam: Map<String, String>): PostRequestBuilder {
            param.requestParam = requestParam
            param.isJson = false
            return this
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : SuccessModel> success(model: Class<T>, f: T.() -> Unit): PostRequestBuilder {
            param.model = model
            val success = object : OnSuccessListener<T> {
                override fun onSuccess(model: T) {
                    f(model)
                }
            }
            param.success = success as OnSuccessListener<Any>
            return this
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : ErrorModel> error(model: Class<T>, f: T.() -> Unit): PostRequestBuilder {
            param.error = model
            val error = object : OnErrorListener<T> {
                override fun onError(model: T) {
                    f(model)
                }
            }
            param.err = error as OnErrorListener<Any>
            return this
        }

        fun analyticsListener(f: (timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) -> Unit): PostRequestBuilder {
            val analyticsListener = object : AnalyticsListener {
                override fun onReceived(timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) {
                    f(timeTakenInMillis, bytesSent, bytesReceived, isFromCache)
                }
            }
            param.analyticsListener = analyticsListener
            return this
        }

        fun response(f: String.() -> Unit): PostRequestBuilder {
            val response = object : ResponseListener {
                override fun response(string: String) {
                    f(string)
                }
            }
            param.responseListener = response
            return this
        }

        fun loader(f: Boolean.() -> Unit): PostRequestBuilder {
            val loader = object : LoaderListener {
                override fun loader(isShowing: Boolean) {
                    f(isShowing)
                }
            }
            param.loaderListener = loader
            return this
        }

        fun progressListener(f: (Long, Long, Float) -> Unit): PostRequestBuilder {
            val process = object : ProgressListener {
                override fun onProgress(bytesRead: Long, contentLength: Long, progress: Float) {
                    f(bytesRead, contentLength, progress)
                }
            }
            param.progressListener = process
            return this
        }

        fun failure(f: (Exception, String) -> Unit): PostRequestBuilder {
            val failure = object : OnFailureListener {
                override fun onFailure(e: Exception, msg: String) {
                    f(e, msg)
                }
            }
            param.failure = failure
            return this
        }

        fun queue(): Call? {
            return call()
        }

        fun connect() {
            call()?.enqueue(Callback.PostRequestCallbackEnqueue(param))
        }

        private fun call(): Call? {
            val JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8")
            val FORM_ENCODED_TYPE = MediaType.parse("application/x-www-form-urlencoded")
            var baseUrl = ApiConfiguration.baseUrl
            if (!param.baseUrl.isEmpty()) {
                baseUrl = param.baseUrl
            }
            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl + this.param.url)?.newBuilder()
            param.query.forEach { (key, value) ->
                urlBuilder?.addQueryParameter(key, value)
            }
            for (i in 0 until param.queryParam.key.size()) {
                val key = param.queryParam.key[i]
                val value = param.queryParam.value[i]
                urlBuilder?.addQueryParameter(key, value)
            }
            builder.url(urlBuilder?.build().toString())

            val headerBuilder = Headers.Builder()
            param.headerParam.forEach { (key, value) ->
                headerBuilder.add(key, value)
            }
            builder.headers(headerBuilder.build())

            val requestBody: RequestBody
            when (param.httpType) {
                WebParam.HttpType.POST -> {
                    requestBody = if (!param.isJson) {
                        RequestBody.create(FORM_ENCODED_TYPE, param.requestParam.convertFormData())
                    } else {
                        RequestBody.create(JSON_MEDIA_TYPE, param.requestParam.toJson())
                    }
                    requestBody?.let {
                        builder = builder.post(it)
                    }
                }
                WebParam.HttpType.PUT -> {
                    requestBody = if (!param.isJson) {
                        RequestBody.create(FORM_ENCODED_TYPE, param.requestParam.convertFormData())
                    } else {
                        RequestBody.create(JSON_MEDIA_TYPE, param.requestParam.toJson())
                    }
                    requestBody?.let {
                        builder = builder.put(it)
                    }
                }
                WebParam.HttpType.DELETE -> {
                    requestBody = if (!param.isJson) {
                        RequestBody.create(FORM_ENCODED_TYPE, param.requestParam.convertFormData())
                    } else {
                        RequestBody.create(JSON_MEDIA_TYPE, param.requestParam.toJson())
                    }
                    requestBody?.let {
                        builder = builder.delete(it)
                    }
                }
                WebParam.HttpType.PATCH -> {
                    requestBody = if (!param.isJson) {
                        RequestBody.create(FORM_ENCODED_TYPE, param.requestParam.convertFormData())
                    } else {
                        RequestBody.create(JSON_MEDIA_TYPE, param.requestParam.toJson())
                    }
                    requestBody?.let {
                        builder = builder.patch(it)
                    }
                }
                else -> {
                    requestBody = if (!param.isJson) {
                        RequestBody.create(FORM_ENCODED_TYPE, param.requestParam.convertFormData())
                    } else {
                        RequestBody.create(JSON_MEDIA_TYPE, param.requestParam.toJson())
                    }
                    requestBody?.let {
                        builder = builder.post(it)
                    }
                }
            }
            requestBody?.let {
                param.requestBodyContentlength = it.contentLength()
            }

            if (param.connectTimeOut != 0L && param.readTimeOut != 0L) {
                okHttpClient = okHttpClient?.newBuilder()
                        ?.connectTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        ?.readTimeout(param.readTimeOut, TimeUnit.SECONDS)
                        ?.writeTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        ?.build()
            }
            param.progressListener?.let {
                okHttpClient = okHttpClient?.newBuilder()?.addInterceptor {
                    val originalResponse = it.proceed(it.request())
                    val originalBody = originalResponse.body()
                    originalResponse.newBuilder()
                            .body(HTTPInternalNetworking.ProgressResponseBody(originalBody!!, param))
                            .build()
                }?.build()
            }
            return okHttpClient?.newCall(builder.build())
        }
    }

    class PutRequestBuilder(param: WebParam) : PostRequestBuilder(param)

    class DeleteRequestBuilder(param: WebParam) : PostRequestBuilder(param)

    class PatchRequestBuilder(param: WebParam) : PostRequestBuilder(param)

    /******************************************************************************************/

    open class DownloadBuilder(val param: WebParam) {
        private var okHttpClient: OkHttpClient? = ApiConfiguration.okHttpClient
        private val JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8")
        private var FORM_ENCODED_TYPE = MediaType.parse("application/x-www-form-urlencoded")

        fun baseUrl(url: String): DownloadBuilder {
            param.baseUrl = url
            return this
        }

        fun headerParam(headerParam: Map<String, String>): DownloadBuilder {
            param.headerParam = headerParam
            return this
        }

        fun timeOut(connectTimeOut: Long, readTimeOut: Long): DownloadBuilder {
            param.connectTimeOut = connectTimeOut
            param.readTimeOut = readTimeOut
            return this
        }

        fun queryParam(queryParam: QueryMap<String, String>): DownloadBuilder {
            param.queryParam = queryParam
            return this
        }

        fun queryParam(queryParam: Map<String, String>): DownloadBuilder {
            param.query = queryParam
            return this
        }

        fun file(file: File): DownloadBuilder {
            param.file = file
            return this
        }

        @Suppress("UNCHECKED_CAST")
        fun success(t: File.() -> Unit): DownloadBuilder {
            param.model = t.javaClass.enclosingClass!!
            val success = object : OnSuccessListener<File> {
                override fun onSuccess(file: File) {
                    t(file)
                }
            }
            param.success = success as OnSuccessListener<Any>
            return this
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : ErrorModel> error(model: Class<T>, t: T.() -> Unit): DownloadBuilder {
            param.error = model
            val error = object : OnErrorListener<T> {
                override fun onError(model: T) {
                    t(model)
                }
            }
            param.err = error as OnErrorListener<Any>
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


        fun analyticsListener(f: (timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) -> Unit): DownloadBuilder {
            val analyticsListener = object : AnalyticsListener {
                override fun onReceived(timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) {
                    f(timeTakenInMillis, bytesSent, bytesReceived, isFromCache)
                }
            }
            param.analyticsListener = analyticsListener
            return this
        }

        fun loader(f: Boolean.() -> Unit): DownloadBuilder {
            val loader = object : LoaderListener {
                override fun loader(isShowing: Boolean) {
                    f(isShowing)
                }
            }
            param.loaderListener = loader
            return this
        }

        fun failure(f: (Exception, String) -> Unit): DownloadBuilder {
            val failure = object : OnFailureListener {
                override fun onFailure(e: Exception, msg: String) {
                    f(e, msg)
                }
            }
            param.failure = failure
            return this
        }

        fun queue(): Call? {
            return call()
        }

        fun connect() {
            call()?.enqueue(Callback.DownloadRequestCallbackEnqueue(param))
        }


        private fun call(): Call? {
            var baseUrl = ApiConfiguration.baseUrl
            if (!param.baseUrl.isEmpty()) {
                baseUrl = param.baseUrl
            }
            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl.plus(param.url))?.newBuilder()
            param.query.forEach { (key, value) ->
                urlBuilder?.addQueryParameter(key, value)
            }
            for (i in 0 until param.queryParam.key.size()) {
                val key = param.queryParam.key[i]
                val value = param.queryParam.value[i]
                urlBuilder?.addQueryParameter(key, value)
            }
            builder.url(urlBuilder?.build().toString())

            val headerBuilder = Headers.Builder()
            param.headerParam.forEach { (key, value) ->
                headerBuilder.add(key, value)
            }
            builder.headers(headerBuilder.build())

            val requestBody: RequestBody
            when (param.httpType) {
                WebParam.HttpType.GET -> {
                    builder = builder.get()
                }
                WebParam.HttpType.POST -> {
                    requestBody = if (!param.isJson) {
                        RequestBody.create(FORM_ENCODED_TYPE, param.requestParam.convertFormData())
                    } else {
                        RequestBody.create(JSON_MEDIA_TYPE, param.requestParam.toJson())
                    }
                    requestBody?.also {
                        builder = builder.post(it)
                    }
                }
                WebParam.HttpType.PUT -> {
                    requestBody = if (!param.isJson) {
                        RequestBody.create(FORM_ENCODED_TYPE, param.requestParam.convertFormData())
                    } else {
                        RequestBody.create(JSON_MEDIA_TYPE, param.requestParam.toJson())
                    }
                    requestBody?.also {
                        builder = builder.put(it)
                    }
                }
                else -> {
                    builder = builder.get()
                }
            }
            if (param.connectTimeOut != 0L && param.readTimeOut != 0L) {
                okHttpClient = okHttpClient?.newBuilder()
                        ?.connectTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        ?.readTimeout(param.readTimeOut, TimeUnit.SECONDS)
                        ?.writeTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        ?.build()
            }
            param.progressListener?.let {
                okHttpClient = okHttpClient?.newBuilder()?.addInterceptor {
                    val originalResponse = it.proceed(it.request())
                    val originalBody = originalResponse.body()
                    originalResponse.newBuilder()
                            .body(HTTPInternalNetworking.ProgressResponseBody(originalBody!!, param))
                            .build()
                }?.build()
            }
            return okHttpClient?.newCall(builder.build())
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
            param.isJson = false
            return this
        }
    }


    class DownloadBuilderPut(param: WebParam) : DownloadBuilderPost(param)

    /******************************************************************************************/

    class MultiPartBuilder(private val param: WebParam) {
        private var okHttpClient: OkHttpClient? = ApiConfiguration.okHttpClient

        fun baseUrl(url: String): MultiPartBuilder {
            param.baseUrl = url
            return this
        }

        fun headerParam(headerParam: Map<String, String>): MultiPartBuilder {
            param.headerParam = headerParam
            return this
        }

        fun timeOut(connectTimeOut: Long, readTimeOut: Long): MultiPartBuilder {
            param.connectTimeOut = connectTimeOut
            param.readTimeOut = readTimeOut
            return this
        }

        fun analyticsListener(callback: AnalyticsListener): MultiPartBuilder {
            param.analyticsListener = callback
            return this
        }

        fun queryParam(queryParam: QueryMap<String, String>): MultiPartBuilder {
            param.queryParam = queryParam
            return this
        }

        fun queryParam(queryParam: Map<String, String>): MultiPartBuilder {
            param.query = queryParam
            return this
        }

        fun multipartParam(multipartParam: Map<String, String>): MultiPartBuilder {
            param.requestParam = multipartParam
            param.isJson = false
            return this
        }

        fun multipartBodyParam(multipartParam: Map<String, Any>): MultiPartBuilder {
            param.requestParam = multipartParam
            param.isJson = true
            return this
        }

        fun multipartParamFile(multipartFile: Map<String, File>, context: Context): MultiPartBuilder {
            param.multipartParamFile = multipartFile
            param.context = context
            return this
        }

        fun multipartParamListFile(multipartFile: Map<String, List<File>>, context: Context): MultiPartBuilder {
            param.multipartParamListFile = multipartFile
            param.context = context
            return this
        }

        fun logging(isLog: Boolean): MultiPartBuilder {
            param.debug = isLog
            return this
        }

        // Higher Order function
        @Suppress("UNCHECKED_CAST")
        fun <T : SuccessModel> success(model: Class<T>, t: T.() -> Unit): MultiPartBuilder {
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
        fun <T : ErrorModel> error(model: Class<T>, t: T.() -> Unit): MultiPartBuilder {
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

        fun response(t: String.() -> Unit): MultiPartBuilder {
            val response = object : ResponseListener {
                override fun response(string: String) {
                    t(string)
                }
            }
            param.responseListener = response
            return this
        }

        fun loader(t: Boolean.() -> Unit): MultiPartBuilder {
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

        fun analyticsListener(f: (timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) -> Unit): MultiPartBuilder {
            val analyticsListener = object : AnalyticsListener {
                override fun onReceived(timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) {
                    f(timeTakenInMillis, bytesSent, bytesReceived, isFromCache)
                }
            }
            param.analyticsListener = analyticsListener
            return this
        }


        fun queue(): Call? {
            return call()
        }

        fun connect() {
            call()?.enqueue(Callback.PostRequestCallbackEnqueue(param))
        }

        private fun call(): Call? {
            var baseUrl = ApiConfiguration.baseUrl
            if (!param.baseUrl.isEmpty()) {
                baseUrl = param.baseUrl
            }

            var builder = okhttp3.Request.Builder()
            val urlBuilder = HttpUrl.parse(baseUrl + param.url)?.newBuilder()

            param.query.forEach { (key, value) ->
                urlBuilder?.addQueryParameter(key, value)
            }

            for (i in 0 until param.queryParam.key.size()) {
                val key = param.queryParam.key[i]
                val value = param.queryParam.value[i]
                urlBuilder?.addQueryParameter(key, value)
            }
            builder.url(urlBuilder?.build().toString())

            val headerBuilder = Headers.Builder()
            param.headerParam.forEach { (key, value) ->
                headerBuilder.add(key, value)
            }
            builder.headers(headerBuilder.build())

            val multipartBuilder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
            val JSON_MEDIA_TYPE = MediaType.parse("multipart/form-data")
            if (!param.isJson) {
                param.requestParam.forEach { (key, value) ->
                    val part = MultipartBody.Part.createFormData(key, value as String);
                    multipartBuilder.addPart(part)
                }
            } else {
                val body = RequestBody.create(JSON_MEDIA_TYPE, param.requestParam.toJson())
                multipartBuilder.addPart(body)
            }

            param.multipartParamFile.forEach { (key, value) ->
                val uri = Uri.fromFile(value)
                var mimeType = "application/octet-stream"
                mimeType = if (uri?.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
                    param.context?.let {
                        it.contentResolver?.getType(uri).toString()
                    }!!
                } else {
                    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                            .toString())
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                            fileExtension.toLowerCase())!!
                }
                val fileBody = RequestBody.create(MediaType.parse(mimeType),
                        value)
                val part = MultipartBody.Part.createFormData(key, value.name, fileBody);
                multipartBuilder.addPart(part)
            }

            param.multipartParamListFile.forEach { (key: String, file: List<File>) ->
                file.forEach {
                    val uri = Uri.fromFile(it)
                    var mimeType = "application/octet-stream"
                    mimeType = if (uri?.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
                        param.context?.let {
                            param.context?.contentResolver?.getType(uri).toString()
                        }!!
                    } else {
                        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                                .toString())
                        MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                fileExtension.toLowerCase())!!
                    }
                    val fileBody = RequestBody.create(MediaType.parse(mimeType),
                            it)
                    val part = MultipartBody.Part.createFormData(key, it.name, fileBody);
                    multipartBuilder.addPart(part)
                }
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
                    multipartBuilder.build().also {
                        builder = builder.post(it)
                        param.requestBodyContentlength = it.contentLength()
                    }
                }
            }

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
                okHttpClient = okHttpClient?.newBuilder()
                        ?.addInterceptor(interceptor)
                        ?.build()
            }
            param.progressListener?.let {
                okHttpClient = okHttpClient?.newBuilder()?.addInterceptor {
                    val originalResponse = it.proceed(it.request())
                    val originalBody = originalResponse.body()
                    originalResponse.newBuilder()
                            .body(HTTPInternalNetworking.ProgressResponseBody(originalBody!!, param))
                            .build()
                }?.build()
            }
            return okHttpClient?.newCall(builder.build())
        }

    }


}
