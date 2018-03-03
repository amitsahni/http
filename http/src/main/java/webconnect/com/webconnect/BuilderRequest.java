package webconnect.com.webconnect;

import android.app.Dialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;

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
public class BuilderRequest {

    public static class GetRequestBuilder implements IProperties<GetRequestBuilder> {


        private WebParam param;
        private OkHttpClient okHttpClient;

        public GetRequestBuilder(WebParam param) {
            this.param = param;
        }

        @Override
        public GetRequestBuilder baseUrl(@NonNull String url) {
            param.baseUrl = url;
            return this;
        }

        @Override
        public GetRequestBuilder headerParam(@NonNull Map<String, String> headerParam) {
            param.headerParam = headerParam;
            return this;
        }

        @Override
        public GetRequestBuilder callback(@NonNull OnWebCallback callback) {
            param.callback = callback;
            return this;
        }

        @Override
        public GetRequestBuilder analyticsListener(@NonNull AnalyticsListener callback) {
            param.analyticsListener = callback;
            return this;
        }

        @Override
        public GetRequestBuilder callback(@NonNull OnWebCallback callback, @NonNull Class<?> success, @NonNull Class<?> error) {
            param.callback = callback;
            param.model = success;
            param.error = error;
            return this;
        }

        @Override
        public GetRequestBuilder taskId(int taskId) {
            param.taskId = taskId;
            return this;
        }

        @Override
        public GetRequestBuilder timeOut(long connectTimeOut, long readTimeOut) {
            param.connectTimeOut = connectTimeOut;
            param.readTimeOut = readTimeOut;
            return this;
        }

        @Override
        public GetRequestBuilder progressDialog(Dialog dialog) {
            param.dialog = dialog;
            return this;
        }

        @Override
        public GetRequestBuilder cache(boolean isCache) {
            param.isCacheEnabled = isCache;
            return this;
        }

        @Override
        public GetRequestBuilder queryParam(@NonNull Map<String, String> requestParam) {
            param.queryParam = requestParam;
            return this;
        }

        @Override
        public void connect() {
            performGetRequest().subscribe(new Callback.GetRequestCallback(param));
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
            okHttpClient = HTTPManager.get().getDefaultOkHttpClient(param);
            if (param.connectTimeOut != 0
                    && param.readTimeOut != 0) {
                okHttpClient = okHttpClient.newBuilder()
                        .connectTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        .readTimeout(param.readTimeOut, TimeUnit.SECONDS)
                        .writeTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        .build();
            }
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

    public static class HeadRequestBuilder extends GetRequestBuilder {

        public HeadRequestBuilder(WebParam param) {
            super(param);
        }
    }

    public static class OptionsRequestBuilder extends GetRequestBuilder {

        public OptionsRequestBuilder(WebParam param) {
            super(param);
        }
    }

    public static class PostRequestBuilder implements IProperties<PostRequestBuilder> {

        private WebParam param;
        private OkHttpClient okHttpClient;

        public PostRequestBuilder(WebParam param) {
            this.param = param;
        }

        @Override
        public PostRequestBuilder baseUrl(@NonNull String url) {
            param.baseUrl = url;
            return this;
        }

        @Override
        public PostRequestBuilder queryParam(@NonNull Map<String, String> requestParam) {
            param.queryParam = requestParam;
            return this;
        }

        @Override
        public PostRequestBuilder headerParam(@NonNull Map<String, String> headerParam) {
            param.headerParam = headerParam;
            return this;
        }

        @Override
        public PostRequestBuilder callback(@NonNull OnWebCallback callback) {
            param.callback = callback;
            return this;
        }

        @Override
        public PostRequestBuilder callback(@NonNull OnWebCallback callback, @NonNull Class<?> success, @NonNull Class<?> error) {
            param.callback = callback;
            param.model = success;
            param.error = error;
            return this;
        }

        @Override
        public PostRequestBuilder taskId(int taskId) {
            param.taskId = taskId;
            return this;
        }

        @Override
        public PostRequestBuilder timeOut(long connectTimeOut, long readTimeOut) {
            param.connectTimeOut = connectTimeOut;
            param.readTimeOut = readTimeOut;
            return this;
        }

        @Override
        public PostRequestBuilder progressDialog(Dialog dialog) {
            param.dialog = dialog;
            return this;
        }

        @Override
        public PostRequestBuilder cache(boolean isCache) {
            param.isCacheEnabled = isCache;
            return this;
        }

        @Override
        public PostRequestBuilder analyticsListener(@NonNull AnalyticsListener callback) {
            param.analyticsListener = callback;
            return this;
        }

        public PostRequestBuilder bodyParam(@NonNull Map<String, ?> requestParam) {
            param.requestParam = requestParam;
            return this;
        }

        @Override
        public void connect() {
            performPostRequest().subscribe(new Callback.PostRequestCallback(param));
        }

        public Observable<?> performPostRequest() {
            MediaType JSON_MEDIA_TYPE =
                    MediaType.parse("application/json; charset=utf-8");
            String baseUrl = ApiConfiguration.getBaseUrl();
            if (!TextUtils.isEmpty(param.baseUrl)) {
                baseUrl = param.baseUrl;
            }
            okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + this.param.url).newBuilder();
            if (this.param.queryParam != null && this.param.queryParam.size() > 0) {
                Set<? extends Map.Entry<String, String>> entries = this.param.queryParam.entrySet();
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
                case POST: {
                    requestBody = RequestBody.create(JSON_MEDIA_TYPE, new Gson().toJson(param.requestParam));
                    builder = builder.post(requestBody);
                    break;
                }
                case PUT: {
                    requestBody = RequestBody.create(JSON_MEDIA_TYPE, new Gson().toJson(param.requestParam));
                    builder = builder.put(requestBody);
                    break;
                }
                case DELETE: {
                    requestBody = RequestBody.create(JSON_MEDIA_TYPE, new Gson().toJson(param.requestParam));
                    builder = builder.delete(requestBody);
                    break;
                }
                case PATCH: {
                    requestBody = RequestBody.create(JSON_MEDIA_TYPE, new Gson().toJson(param.requestParam));
                    builder = builder.patch(requestBody);
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
            okHttpClient = HTTPManager.get().getDefaultOkHttpClient(this.param);
            if (this.param.connectTimeOut != 0
                    && this.param.readTimeOut != 0) {
                okHttpClient = okHttpClient.newBuilder()
                        .connectTimeout(this.param.connectTimeOut, TimeUnit.SECONDS)
                        .readTimeout(this.param.readTimeOut, TimeUnit.SECONDS)
                        .writeTimeout(this.param.connectTimeOut, TimeUnit.SECONDS)
                        .build();
            }

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

    public static class PutRequestBuilder extends PostRequestBuilder {

        public PutRequestBuilder(WebParam param) {
            super(param);
        }
    }

    public static class DeleteRequestBuilder extends PostRequestBuilder {

        public DeleteRequestBuilder(WebParam param) {
            super(param);
        }
    }

    public static class PatchRequestBuilder extends PostRequestBuilder {

        public PatchRequestBuilder(WebParam param) {
            super(param);
        }
    }

    public static class DownloadBuilder implements IProperties<DownloadBuilder> {

        private WebParam param;
        private OkHttpClient okHttpClient;

        public DownloadBuilder(WebParam param) {
            this.param = param;
        }

        @Override
        public DownloadBuilder baseUrl(@NonNull String url) {
            param.baseUrl = url;
            return this;
        }

        @Override
        public DownloadBuilder headerParam(@NonNull Map<String, String> headerParam) {
            param.headerParam = headerParam;
            return this;
        }

        @Override
        public DownloadBuilder callback(@NonNull OnWebCallback callback) {
            param.callback = callback;
            return this;
        }

        @Override
        public DownloadBuilder callback(@NonNull OnWebCallback callback, @NonNull Class<?> success, @NonNull Class<?> error) {
            param.callback = callback;
            param.model = success;
            param.error = error;
            return this;
        }

        @Override
        public DownloadBuilder taskId(int taskId) {
            param.taskId = taskId;
            return this;
        }

        @Override
        public DownloadBuilder timeOut(long connectTimeOut, long readTimeOut) {
            param.connectTimeOut = connectTimeOut;
            param.readTimeOut = readTimeOut;
            return this;
        }

        @Override
        public DownloadBuilder progressDialog(Dialog dialog) {
            param.dialog = dialog;
            return this;
        }

        @Override
        public DownloadBuilder cache(boolean isCache) {
            param.isCacheEnabled = isCache;
            return this;
        }

        @Override
        public DownloadBuilder analyticsListener(@NonNull AnalyticsListener callback) {
            param.analyticsListener = callback;
            return this;
        }

        @Override
        public DownloadBuilder queryParam(@NonNull Map<String, String> requestParam) {
            param.queryParam = requestParam;
            return this;
        }

        public DownloadBuilder progressListener(@NonNull ProgressListener callback) {
            param.progressListener = callback;
            return this;
        }

        public DownloadBuilder file(@NonNull File file) {
            param.file = file;
            return this;
        }

        @Override
        public void connect() {
            performDownloadRequest();
        }


        Observable<?> performDownloadRequest() {
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

            switch (param.httpType) {
                case DOWNLOAD: {
                    builder = builder.get();
                    break;
                }
            }
            okHttpClient = HTTPManager.get().getDefaultOkHttpClient(param);
            if (param.connectTimeOut != 0
                    && param.readTimeOut != 0) {
                okHttpClient = okHttpClient.newBuilder()
                        .connectTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        .readTimeout(param.readTimeOut, TimeUnit.SECONDS)
                        .writeTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        .build();
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
            return new RxObservable.DownloadANObservable<>(param, call)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public static class MultiPartBuilder implements IProperties<MultiPartBuilder> {
        private WebParam param;
        private OkHttpClient okHttpClient;

        public MultiPartBuilder(WebParam param) {
            this.param = param;
            param.debug = true;
        }

        @Override
        public MultiPartBuilder baseUrl(@NonNull String url) {
            param.baseUrl = url;
            return this;
        }

        @Override
        public MultiPartBuilder headerParam(@NonNull Map<String, String> headerParam) {
            param.headerParam = headerParam;
            return this;
        }

        @Override
        public MultiPartBuilder callback(@NonNull OnWebCallback callback) {
            param.callback = callback;
            return this;
        }

        @Override
        public MultiPartBuilder callback(@NonNull OnWebCallback callback, @NonNull Class<?> success, @NonNull Class<?> error) {
            param.callback = callback;
            param.model = success;
            param.error = error;
            return this;
        }

        @Override
        public MultiPartBuilder taskId(int taskId) {
            param.taskId = taskId;
            return this;
        }

        @Override
        public MultiPartBuilder timeOut(long connectTimeOut, long readTimeOut) {
            param.connectTimeOut = connectTimeOut;
            param.readTimeOut = readTimeOut;
            return this;
        }

        @Override
        public MultiPartBuilder progressDialog(Dialog dialog) {
            param.dialog = dialog;
            return this;
        }

        @Override
        public MultiPartBuilder cache(boolean isCache) {
            param.isCacheEnabled = isCache;
            return this;
        }

        @Override
        public MultiPartBuilder analyticsListener(@NonNull AnalyticsListener callback) {
            param.analyticsListener = callback;
            return this;
        }

        @Override
        public MultiPartBuilder queryParam(@NonNull Map<String, String> requestParam) {
            param.queryParam = requestParam;
            return this;
        }

        public MultiPartBuilder multipartParam(@NonNull Map<String, String> multipartParam) {
            param.multipartParam = multipartParam;
            return this;
        }

        public MultiPartBuilder multipartParamFile(@NonNull Map<String, File> multipartFile) {
            param.multipartParamFile = multipartFile;
            return this;
        }

        public MultiPartBuilder progressListener(@NonNull ProgressListener callback) {
            param.progressListener = callback;
            return this;
        }

        public MultiPartBuilder logging(boolean isLog) {
            param.debug = isLog;
            return this;
        }

        @Override
        public void connect() {
            performMultipartRequest().subscribe(new Callback.UploadRequestCallback(param));
        }

        public Observable<?> performMultipartRequest() {
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
                            RequestBody body = RequestBody.create(null, entry.getValue());
//                            multipartBuilder.addPart(Headers.of("Content-Disposition",
//                                    "form-data; name=\"" + entry.getKey() + "\""),
//                                    body);
                            multipartBuilder.addFormDataPart(entry.getKey(), entry.getKey(), body);
                        }
                        for (HashMap.Entry<String, File> entry : param.multipartParamFile.entrySet()) {
                            Uri uri = Uri.fromFile(entry.getValue());
                            ContentResolver cR = param.context.getContentResolver();
                            String mime = cR.getType(uri);
                            RequestBody fileBody = RequestBody.create(MediaType.parse(mime),
                                    entry.getValue());
//                            multipartBuilder.addPart(Headers.of("Content-Disposition",
//                                    "form-data; name=\"" + entry.getKey() + "\"; filename=\"" + fileName + "\""),
//                                    fileBody);
                            multipartBuilder.addFormDataPart(entry.getKey(), entry.getKey(), fileBody);
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
            okHttpClient = HTTPManager.get().getDefaultOkHttpClient(param);
            if (param.connectTimeOut != 0
                    && param.readTimeOut != 0) {
                okHttpClient = okHttpClient.newBuilder()
                        .connectTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        .readTimeout(param.readTimeOut, TimeUnit.SECONDS)
                        .writeTimeout(param.connectTimeOut, TimeUnit.SECONDS)
                        .build();
            }
            if (!param.debug) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
                okHttpClient = okHttpClient.newBuilder()
                        .addInterceptor(interceptor)
                        .build();
            }
            okHttpClient = okHttpClient.newBuilder()
                    .addNetworkInterceptor(new Interceptor() {
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
