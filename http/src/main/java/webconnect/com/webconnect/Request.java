package webconnect.com.webconnect;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.androidnetworking.utils.Utils;
import com.google.gson.Gson;
import com.rx2androidnetworking.Rx2ANRequest;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import webconnect.com.webconnect.di.IProperties;
import webconnect.com.webconnect.listener.AnalyticsListener;
import webconnect.com.webconnect.listener.OnWebCallback;
import webconnect.com.webconnect.listener.ProgressListener;

/**
 * Created by clickapps on 27/12/17.
 */
@SuppressWarnings({"unchecked"})
public class Request<T extends Request> {
    private WebParam param;

    public WebParam getParam() {
        return param;
    }

    public Request(GetRequestBuilder builder) {
        param = builder.param;
    }

    public Request(PostRequestBuilder builder) {
        param = builder.param;
    }

    public Request(DownloadBuilder builder) {
        param = builder.param;
    }

    public Request(MultiPartBuilder builder) {
        param = builder.param;
    }

    public Observable<?> execute() {
        switch (param.httpType) {
            case GET:
                return new GetRequestBuilder<>(param).execute();
            case OPTIONS:
                return new GetRequestBuilder<>(param).execute();
            case HEAD:
                return new GetRequestBuilder<>(param).execute();
            case POST:
                return new PostRequestBuilder<>(param).execute();
            case PUT:
                return new PostRequestBuilder<>(param).execute();
            case PATCH:
                return new PostRequestBuilder<>(param).execute();
            case DELETE:
                return new PostRequestBuilder<>(param).execute();
            case MULTIPART:
                return new MultiPartBuilder<>(param).execute();
            default:
                return new GetRequestBuilder<>(param).execute();
        }
    }

    public static class GetRequestBuilder<T extends GetRequestBuilder> implements IProperties {
        private WebParam param;
        private OkHttpClient okHttpClient;

        public GetRequestBuilder(WebParam param) {
            this.param = param;
        }

        @Override
        public T baseUrl(@NonNull String url) {
            param.baseUrl = url;
            return (T) this;
        }

        @Override
        public T headerParam(@NonNull Map<String, String> headerParam) {
            param.headerParam = headerParam;
            return (T) this;
        }

        @Override
        public T callback(@NonNull OnWebCallback callback) {
            param.callback = callback;
            return (T) this;
        }

        @Override
        public IProperties analyticsListener(@NonNull AnalyticsListener callback) {
            param.analyticsListener = callback;
            return (T) this;
        }

        @Override
        public T callback(@NonNull OnWebCallback callback, @NonNull Class<?> success, @NonNull Class<?> error) {
            param.callback = callback;
            param.model = success;
            param.error = error;
            return (T) this;
        }

        @Override
        public T taskId(int taskId) {
            param.taskId = taskId;
            return (T) this;
        }

        @Override
        public T timeOut(long connectTimeOut, long readTimeOut) {
            param.connectTimeOut = connectTimeOut;
            param.readTimeOut = readTimeOut;
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(ApiConfiguration.isDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                    .readTimeout(param.readTimeOut, TimeUnit.SECONDS)
                    .writeTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .cache(ApiConfiguration.getOkHttpClient().cache())
                    .dispatcher(ApiConfiguration.getOkHttpClient().dispatcher())
                    .build();
            return (T) this;
        }

        @Override
        public T progressDialog(Dialog dialog) {
            param.dialog = dialog;
            return (T) this;
        }

        @Override
        public T cache(boolean isCache) {
            param.isCacheEnabled = isCache;
            return (T) this;
        }

        @Override
        public T queryParam(@NonNull Map<String, String> requestParam) {
            param.queryParam = requestParam;
            return (T) this;
        }

        public Request build() {
            return new Request(this);
        }

        @Override
        public void connect() {
            execute().subscribe(new Callback.GetRequestCallback(param));
//            performGetRequest().subscribe(new Callback.GetOkHttpCallback(param));
//            performGetRequest().subscribe(new Callback.GetRequestCallback(param));
//            IAPIService request = RetrofitManager.get().createService(param);
//            if (param.httpType == WebParam.HttpType.GET) {
//                request.get(param.url, (Map<String, Object>) param.requestParam)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Callback.RequestCallback<>(param));
//            } else if (param.httpType == WebParam.HttpType.HEAD) {
//                request.head(param.url, (Map<String, Object>) param.requestParam)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Callback.RequestCallback<>(param));
//            } else if (param.httpType == WebParam.HttpType.OPTIONS) {
//                request.options(param.url, (Map<String, Object>) param.requestParam)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Callback.RequestCallback<>(param));
//            }
        }


        public Observable<?> execute() {
            String baseUrl = param.baseUrl;
            if (TextUtils.isEmpty(baseUrl)) {
                baseUrl = ApiConfiguration.getBaseUrl();
            }
            Rx2ANRequest.GetRequestBuilder getBuilder;
            switch (param.httpType) {
                case GET:
                    getBuilder = Rx2AndroidNetworking.get(baseUrl + param.url);
                    break;
                case HEAD:
                    getBuilder = Rx2AndroidNetworking.head(baseUrl + param.url);
                    break;
                case OPTIONS:
                    getBuilder = Rx2AndroidNetworking.options(baseUrl + param.url);
                    break;
                default:
                    getBuilder = Rx2AndroidNetworking.get(baseUrl + param.url);
                    break;
            }
            getBuilder.addQueryParameter(param.queryParam)
                    .addHeaders(param.headerParam);
            if (okHttpClient != null) {
                getBuilder.setOkHttpClient(okHttpClient);
            }
            getBuilder = param.isCacheEnabled ? getBuilder.getResponseOnlyIfCached() : getBuilder.getResponseOnlyFromNetwork();
            return getBuilder.setTag(param.taskId)
                    .build()
                    .setAnalyticsListener(new Callback.Analytics())
                    .getObjectObservable(param.model)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }


        public Observable<?> performGetRequest() {
            MediaType JSON_MEDIA_TYPE =
                    MediaType.parse("application/json; charset=utf-8");
            String baseUrl = ApiConfiguration.getBaseUrl();
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl;
            }
            okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + param.url).newBuilder();
            if (param.queryParam != null && param.queryParam.size() > 0) {
                Set<? extends Map.Entry<String, ?>> entries = param.queryParam.entrySet();
                for (Map.Entry<String, ?> entry : entries) {
                    String name = entry.getKey();
                    String value = (String) entry.getValue();
                    urlBuilder.addQueryParameter(name, value);
                }
            }
            builder.url(urlBuilder.build().toString());

            if (param.headerParam != null && param.headerParam.size() > 0) {
                Headers.Builder headerBuilder = new Headers.Builder();
                for (Map.Entry<String, String> entry : param.headerParam.entrySet()) {
                    headerBuilder.add(entry.getKey(), entry.getValue());
                }
                builder.headers(headerBuilder.build());
            }

            switch (param.httpType) {
                case GET: {
                    builder = builder.get();
                    break;
                }
                case HEAD: {
                    builder = builder.head();
                    break;
                }
                case OPTIONS: {
                    builder = builder.method("OPTIONS", null);
                    break;
                }
            }
            if (okHttpClient == null)
                okHttpClient = HTTPManager.get().getDefaultOkHttpClient(param);

            if (param.isCacheEnabled) {
                builder.cacheControl(CacheControl.FORCE_CACHE);
            } else {
                builder.cacheControl(CacheControl.FORCE_NETWORK);
            }
            okhttp3.Request okHttpRequest = builder.build();
            Call call = okHttpClient.newCall(okHttpRequest);
            return new RxObservable.SimpleANObservable<>(param, call)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public static class PostRequestBuilder<T extends PostRequestBuilder> implements IProperties {
        private WebParam param;
        private OkHttpClient okHttpClient;

        public PostRequestBuilder(WebParam param) {
            this.param = param;
        }

        @Override
        public T baseUrl(@NonNull String url) {
            param.baseUrl = url;
            return (T) this;
        }

        @Override
        public T queryParam(@NonNull Map<String, String> requestParam) {
            param.queryParam = requestParam;
            return (T) this;
        }

        @Override
        public T headerParam(@NonNull Map<String, String> headerParam) {
            param.headerParam = headerParam;
            return (T) this;
        }

        @Override
        public T callback(@NonNull OnWebCallback callback) {
            param.callback = callback;
            return (T) this;
        }

        @Override
        public T callback(@NonNull OnWebCallback callback, @NonNull Class<?> success, @NonNull Class<?> error) {
            param.callback = callback;
            param.model = success;
            param.error = error;
            return (T) this;
        }

        @Override
        public T taskId(int taskId) {
            param.taskId = taskId;
            return (T) this;
        }

        @Override
        public T timeOut(long connectTimeOut, long readTimeOut) {
            param.connectTimeOut = connectTimeOut;
            param.readTimeOut = readTimeOut;
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(ApiConfiguration.isDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                    .readTimeout(param.readTimeOut, TimeUnit.SECONDS)
                    .writeTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .cache(ApiConfiguration.getOkHttpClient().cache())
                    .dispatcher(ApiConfiguration.getOkHttpClient().dispatcher())
                    .build();
            return (T) this;
        }

        @Override
        public T progressDialog(Dialog dialog) {
            param.dialog = dialog;
            return (T) this;
        }

        @Override
        public T cache(boolean isCache) {
            param.isCacheEnabled = isCache;
            return (T) this;
        }

        @Override
        public IProperties analyticsListener(@NonNull AnalyticsListener callback) {
            param.analyticsListener = callback;
            return (T) this;
        }

        public T bodyParam(@NonNull Map<String, ?> requestParam) {
            param.requestParam = requestParam;
            return (T) this;
        }

        public T addFile(@NonNull File file) {
            param.file = file;
            return (T) this;
        }

        public Request build() {
            return new Request(this);
        }

        @Override
        public void connect() {
            execute().subscribe(new Callback.PostRequestCallback(param));
        }

        public Observable<?> execute() {
            String baseUrl = param.baseUrl;
            if (TextUtils.isEmpty(baseUrl)) {
                baseUrl = ApiConfiguration.getBaseUrl();
            }
            Rx2ANRequest.PostRequestBuilder postBuilder;
            switch (param.httpType) {
                case POST:
                    postBuilder = Rx2AndroidNetworking.post(baseUrl + param.url);
                    break;
                case PUT:
                    postBuilder = Rx2AndroidNetworking.put(baseUrl + param.url);
                    break;
                case DELETE:
                    postBuilder = Rx2AndroidNetworking.delete(baseUrl + param.url);
                    break;
                case PATCH:
                    postBuilder = Rx2AndroidNetworking.patch(baseUrl + param.url);
                    break;
                default:
                    postBuilder = Rx2AndroidNetworking.post(baseUrl + param.url);
                    break;
            }
            postBuilder.addBodyParameter(param.requestParam)
                    .addQueryParameter(param.queryParam)
                    .addHeaders(param.headerParam);
            if (param.file != null) {
                postBuilder.addFileBody(param.file);
            }
            if (okHttpClient != null) {
                postBuilder.setOkHttpClient(okHttpClient);
            }
            postBuilder = param.isCacheEnabled ? postBuilder.getResponseOnlyIfCached() : postBuilder.getResponseOnlyFromNetwork();
            return postBuilder.setTag(param.taskId)
                    .build()
                    .setAnalyticsListener(new Callback.Analytics())
                    .getObjectObservable(param.model)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

        public Observable<?> performPostRequest(final WebParam webParam) {
            MediaType JSON_MEDIA_TYPE =
                    MediaType.parse("application/json; charset=utf-8");
            String baseUrl = ApiConfiguration.getBaseUrl();
            if (!TextUtils.isEmpty(webParam.baseUrl)) {
                baseUrl = webParam.baseUrl;
            }
            okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + param.url).newBuilder();
            if (param.queryParam != null && param.queryParam.size() > 0) {
                Set<? extends Map.Entry<String, String>> entries = param.queryParam.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    String name = entry.getKey();
                    String value = entry.getValue();
                    urlBuilder.addQueryParameter(name, value);
                }
            }
            builder.url(urlBuilder.build().toString());

            if (webParam.headerParam != null && webParam.headerParam.size() > 0) {
                Headers.Builder headerBuilder = new Headers.Builder();
                for (Map.Entry<String, String> entry : webParam.headerParam.entrySet()) {
                    headerBuilder.add(entry.getKey(), entry.getValue());
                }
                builder.headers(headerBuilder.build());
            }

            RequestBody requestBody = null;
            switch (webParam.httpType) {
                case POST: {
                    requestBody = RequestBody.create(JSON_MEDIA_TYPE, new Gson().toJson(webParam.requestParam));
                    builder = builder.post(requestBody);
                    break;
                }
                case PUT: {
                    requestBody = RequestBody.create(JSON_MEDIA_TYPE, new Gson().toJson(webParam.requestParam));
                    builder = builder.put(requestBody);
                    break;
                }
                case DELETE: {
                    requestBody = RequestBody.create(JSON_MEDIA_TYPE, new Gson().toJson(webParam.requestParam));
                    builder = builder.delete(requestBody);
                    break;
                }
                case PATCH: {
                    requestBody = RequestBody.create(JSON_MEDIA_TYPE, new Gson().toJson(webParam.requestParam));
                    builder = builder.patch(requestBody);
                    break;
                }
            }
            if (requestBody != null) {
                try {
                    webParam.requestBodyContentlength = requestBody.contentLength();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (param.file != null) {
                //  builder.addFileBody(param.file);
            }
            if (okHttpClient == null)
                okHttpClient = HTTPManager.get().getDefaultOkHttpClient(webParam);

            if (webParam.isCacheEnabled) {
                builder.cacheControl(CacheControl.FORCE_CACHE);
            } else {
                builder.cacheControl(CacheControl.FORCE_NETWORK);
            }
            okhttp3.Request okHttpRequest = builder.build();
            Call call = okHttpClient.newCall(okHttpRequest);
            return new RxObservable.SimpleANObservable<>(webParam, call)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

    }

    public static class DownloadBuilder<T extends DownloadBuilder> implements IProperties {
        private WebParam param;
        private OkHttpClient okHttpClient;

        public DownloadBuilder(WebParam param) {
            this.param = param;
        }

        @Override
        public T baseUrl(@NonNull String url) {
            param.baseUrl = url;
            return (T) this;
        }

        @Override
        public T headerParam(@NonNull Map<String, String> headerParam) {
            param.headerParam = headerParam;
            return (T) this;
        }

        @Override
        public T callback(@NonNull OnWebCallback callback) {
            param.callback = callback;
            return (T) this;
        }

        @Override
        public T callback(@NonNull OnWebCallback callback, @NonNull Class<?> success, @NonNull Class<?> error) {
            param.callback = callback;
            param.model = success;
            param.error = error;
            return (T) this;
        }

        @Override
        public T taskId(int taskId) {
            param.taskId = taskId;
            return (T) this;
        }

        @Override
        public T timeOut(long connectTimeOut, long readTimeOut) {
            param.connectTimeOut = connectTimeOut;
            param.readTimeOut = readTimeOut;
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(ApiConfiguration.isDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                    .readTimeout(param.readTimeOut, TimeUnit.SECONDS)
                    .writeTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .cache(ApiConfiguration.getOkHttpClient().cache())
                    .dispatcher(ApiConfiguration.getOkHttpClient().dispatcher())
                    .build();
            return (T) this;
        }

        @Override
        public T progressDialog(Dialog dialog) {
            param.dialog = dialog;
            return (T) this;
        }

        @Override
        public T cache(boolean isCache) {
            param.isCacheEnabled = isCache;
            return (T) this;
        }

        @Override
        public IProperties analyticsListener(@NonNull AnalyticsListener callback) {
            param.analyticsListener = callback;
            return (T) this;
        }

        @Override
        public T queryParam(@NonNull Map<String, String> requestParam) {
            param.queryParam = requestParam;
            return (T) this;
        }

        public T progressListener(@NonNull ProgressListener callback) {
            param.progressListener = callback;
            return (T) this;
        }

        public T file(@NonNull File file) {
            param.file = file;
            return (T) this;
        }

        @Override
        public void connect() {
            download();
//            performDownloadRequest().subscribe(new Callback.DownloadRequestCallback(param));
        }

        void download() {
            String baseUrl = param.baseUrl;
            if (TextUtils.isEmpty(baseUrl)) {
                baseUrl = ApiConfiguration.getBaseUrl();
            }
            Rx2ANRequest.DownloadBuilder downloadBuilder = Rx2AndroidNetworking.download(param.url, param.file.getParent(), param.file.getName());
            downloadBuilder
                    .addQueryParameter(param.queryParam)
                    .setTag(param.taskId)
                    .addHeaders(param.headerParam);
            if (okHttpClient != null) {
                downloadBuilder.setOkHttpClient(okHttpClient);
            }
            Rx2ANRequest okHttpRequest = downloadBuilder.build();
            okHttpRequest
                    .setAnalyticsListener(new Callback.Analytics())
                    .setDownloadProgressListener(new Callback.ProgressCallback(param))
                    .startDownload(new Callback.DownloadRequestCallback(param));
        }

        Observable<?> performDownloadRequest() {
            MediaType JSON_MEDIA_TYPE =
                    MediaType.parse("application/json; charset=utf-8");
            String baseUrl = ApiConfiguration.getBaseUrl();
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl;
            }
            okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(param.url).newBuilder();
            if (param.queryParam != null && param.queryParam.size() > 0) {
                Set<? extends Map.Entry<String, String>> entries = param.queryParam.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    String name = entry.getKey();
                    String value = entry.getValue();
                    urlBuilder.addQueryParameter(name, value);
                }
            }
            builder.url(urlBuilder.build().toString());

            if (param.headerParam != null && param.headerParam.size() > 0) {
                Headers.Builder headerBuilder = new Headers.Builder();
                for (Map.Entry<String, String> entry : param.headerParam.entrySet()) {
                    headerBuilder.add(entry.getKey(), entry.getValue());
                }
                builder.headers(headerBuilder.build());
            }

            switch (param.httpType) {
                case DOWNLOAD: {
                    builder = builder.get();
                    break;
                }
            }
            if (okHttpClient == null)
                okHttpClient = HTTPManager.get().getDefaultOkHttpClient(param);

            if (param.isCacheEnabled) {
                builder.cacheControl(CacheControl.FORCE_CACHE);
            } else {
                builder.cacheControl(CacheControl.FORCE_NETWORK);
            }
            okHttpClient = okHttpClient.newBuilder().addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new HTTPInternalNetworking.ProgressResponseBody(originalResponse.body(), param))
                            .build();
                }
            }).build();
            okhttp3.Request okHttpRequest = builder.build();
            Call call = okHttpClient.newCall(okHttpRequest);
            return new RxObservable.DownloadANObservable<>(param, call)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public static class MultiPartBuilder<T extends MultiPartBuilder> implements IProperties {
        private WebParam param;
        private OkHttpClient okHttpClient;

        public MultiPartBuilder(WebParam param) {
            this.param = param;
        }

        @Override
        public T baseUrl(@NonNull String url) {
            param.baseUrl = url;
            return (T) this;
        }

        @Override
        public T headerParam(@NonNull Map<String, String> headerParam) {
            param.headerParam = headerParam;
            return (T) this;
        }

        @Override
        public T callback(@NonNull OnWebCallback callback) {
            param.callback = callback;
            return (T) this;
        }

        @Override
        public T callback(@NonNull OnWebCallback callback, @NonNull Class<?> success, @NonNull Class<?> error) {
            param.callback = callback;
            param.model = success;
            param.error = error;
            return (T) this;
        }

        @Override
        public T taskId(int taskId) {
            param.taskId = taskId;
            return (T) this;
        }

        @Override
        public T timeOut(long connectTimeOut, long readTimeOut) {
            param.connectTimeOut = connectTimeOut;
            param.readTimeOut = readTimeOut;
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(ApiConfiguration.isDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                    .readTimeout(param.readTimeOut, TimeUnit.SECONDS)
                    .writeTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .cache(ApiConfiguration.getOkHttpClient().cache())
                    .dispatcher(ApiConfiguration.getOkHttpClient().dispatcher())
                    .build();
            return (T) this;
        }

        @Override
        public T progressDialog(Dialog dialog) {
            param.dialog = dialog;
            return (T) this;
        }

        @Override
        public T cache(boolean isCache) {
            param.isCacheEnabled = isCache;
            return (T) this;
        }

        @Override
        public IProperties analyticsListener(@NonNull AnalyticsListener callback) {
            param.analyticsListener = callback;
            return (T) this;
        }

        @Override
        public T queryParam(@NonNull Map<String, String> requestParam) {
            param.queryParam = requestParam;
            return (T) this;
        }

        public T multipartParam(@NonNull Map<String, String> multipartParam) {
            param.multipartParam = multipartParam;
            return (T) this;
        }

        public T multipartParamFile(@NonNull Map<String, File> multipartFile) {
            param.multipartParamFile = multipartFile;
            return (T) this;
        }

        public T progressListener(@NonNull ProgressListener callback) {
            param.progressListener = callback;
            return (T) this;
        }

        public Request build() {
            return new Request(this);
        }

        @Override
        public void connect() {
            execute().subscribe(new Callback.UploadRequestCallback(param));
            // performMultipartRequest().subscribe(new Callback.UploadRequestCallback(param));
        }

        public Observable<?> execute() {
            String baseUrl = param.baseUrl;
            if (TextUtils.isEmpty(baseUrl)) {
                baseUrl = ApiConfiguration.getBaseUrl();
            }
            Rx2ANRequest.MultiPartBuilder multipartBuilder = Rx2AndroidNetworking.upload(baseUrl + param.url);
            multipartBuilder
                    .setTag(param.taskId)
                    .addHeaders(param.headerParam);
            if (okHttpClient != null) {
                multipartBuilder.setOkHttpClient(okHttpClient);
            }
            return multipartBuilder
                    .addMultipartParameter(param.queryParam)
                    .addMultipartFile(param.multipartParamFile)
                    .addMultipartParameter(param.multipartParam)
                    .build()
                    .setAnalyticsListener(new Callback.Analytics())
                    .setUploadProgressListener(new Callback.UploadProgressCallback(param))
                    .getObjectObservable(param.model)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }


        public Observable<?> performMultipartRequest() {
            MediaType JSON_MEDIA_TYPE =
                    MediaType.parse("application/json; charset=utf-8");
            String baseUrl = ApiConfiguration.getBaseUrl();
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl;
            }

            okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + param.url).newBuilder();
            if (param.queryParam != null && param.queryParam.size() > 0) {
                Set<? extends Map.Entry<String, String>> entries = param.queryParam.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    String name = entry.getKey();
                    String value = entry.getValue();
                    urlBuilder.addQueryParameter(name, value);
                }
            }
            builder.url(urlBuilder.build().toString());

            if (param.headerParam != null && param.headerParam.size() > 0) {
                Headers.Builder headerBuilder = new Headers.Builder();
                for (Map.Entry<String, String> entry : param.headerParam.entrySet()) {
                    headerBuilder.add(entry.getKey(), entry.getValue());
                }
                builder.headers(headerBuilder.build());
            }

            RequestBody requestBody = null;
            switch (param.httpType) {
                case MULTIPART: {
                    MultipartBody.Builder multipartBuilder = new MultipartBody
                            .Builder()
                            .setType(MultipartBody.FORM);
                    try {
                        for (HashMap.Entry<String, String> entry : param.multipartParam.entrySet()) {
                            multipartBuilder.addPart(Headers.of("Content-Disposition",
                                    "form-data; name=\"" + entry.getKey() + "\""),
                                    RequestBody.create(null, entry.getValue()));
                        }
                        for (HashMap.Entry<String, File> entry : param.multipartParamFile.entrySet()) {
                            String fileName = entry.getValue().getName();
                            RequestBody fileBody = RequestBody.create(MediaType.parse(Utils.getMimeType(fileName)),
                                    entry.getValue());
                            multipartBuilder.addPart(Headers.of("Content-Disposition",
                                    "form-data; name=\"" + entry.getKey() + "\"; filename=\"" + fileName + "\""),
                                    fileBody);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    requestBody = multipartBuilder.build();
                    break;
                }
            }
            if (requestBody != null) {
                try {
                    param.requestBodyContentlength = requestBody.contentLength();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (param.isCacheEnabled) {
                builder.cacheControl(CacheControl.FORCE_CACHE);
            } else {
                builder.cacheControl(CacheControl.FORCE_NETWORK);
            }
            okHttpClient = okHttpClient.newBuilder().addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new HTTPInternalNetworking.ProgressResponseBody(originalResponse.body(), param))
                            .build();
                }
            }).build();
            okhttp3.Request okHttpRequest = builder.build();
            Call call = okHttpClient.newCall(okHttpRequest);
            return new RxObservable.SimpleANObservable<>(param, call)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

    }
}
