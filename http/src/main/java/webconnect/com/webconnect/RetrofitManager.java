package webconnect.com.webconnect;

import android.text.TextUtils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Okio;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by clickapps on 28/12/17.
 */

public class RetrofitManager {
    int cacheSize = 10 * 1024 * 1024; // 10 MB
    private static RetrofitManager sManager = new RetrofitManager();
    private final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
    private Dispatcher dispatcher = new Dispatcher();
    final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

    private RetrofitManager() {
    }

    /**
     * Get web connect.
     *
     * @return the web connect
     */
    static RetrofitManager get() {
        if (sManager == null) {
            synchronized (RetrofitManager.class) {
                if (sManager == null) {
                    sManager = new RetrofitManager();
                }
            }
        }
        return sManager;
    }

    OkHttpClient.Builder getHttpBuilder(final WebParam webParam) {
        okHttpClientBuilder.interceptors().clear();
        interceptor.setLevel(ApiConfiguration.isDebug() ?
                HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        okHttpClientBuilder.addInterceptor(interceptor);
        Cache cache = new Cache(webParam.context.getCacheDir(), cacheSize);
        okHttpClientBuilder.cache(cache);
        dispatcher.setMaxRequestsPerHost(2);
        dispatcher.setMaxRequests(10);
        okHttpClientBuilder.dispatcher(dispatcher);
        okHttpClientBuilder.connectTimeout(webParam.connectTimeOut == 0 ? ApiConfiguration.getConnectTimeOut() : webParam.connectTimeOut, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.readTimeout(webParam.readTimeOut == 0 ? ApiConfiguration.getReadTimeOut() : webParam.connectTimeOut, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (webParam.isCacheEnabled) {
                    String cacheControl = originalResponse.header("Cache-Control");
                    if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                            cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
                        return originalResponse.newBuilder()
                                .header("Cache-Control", "public, max-age=" + +Integer.MAX_VALUE)
                                .build();
                    } else {
                        return originalResponse;
                    }
                } else {
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "no-cache")
                            .build();
                }
            }
        });
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request();

                if (webParam.headerParam != null && webParam.headerParam.size() > 0) {
                    for (Map.Entry<String, String> entry : webParam.headerParam.entrySet()) {
                        request = request.newBuilder().addHeader(entry.getKey(), entry.getValue()).build();
                    }
                }
                if (webParam.isCacheEnabled) {
                    request = request.newBuilder().addHeader("Cache-Control", "public, max-age=" + +Integer.MAX_VALUE).build();
                } else {
                    request = request.newBuilder().addHeader("Cache-Control", "no-cache").build();
                }
                Response originalResponse = chain.proceed(request);
                return originalResponse;
            }
        });
        return okHttpClientBuilder;
    }

    Retrofit.Builder getRetrofit(String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            baseUrl = ApiConfiguration.getBaseUrl();
        }
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(ApiConfiguration.getGson()));
        return builder;
    }

    IAPIService createService(final WebParam webParam) {
        return getRetrofit(webParam.baseUrl)
                .client(getHttpBuilder(webParam).build()).build().create(IAPIService.class);
    }

    <T> T createService(Class<T> service, final WebParam webParam) {
        return getRetrofit(webParam.baseUrl)
                .client(getHttpBuilder(webParam).build()).build().create(service);
    }

    /**
     * The type String converter factory.
     */
    private static final class StringConverterFactory extends Converter.Factory {
        /**
         * Create string converter factory.
         *
         * @return the string converter factory
         */
        private static StringConverterFactory create() {
            return new StringConverterFactory();
        }

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return new StringConverterFactory.ConfigurationServiceConverter();
        }

        /**
         * The type ApiConfiguration service converter.
         */
        class ConfigurationServiceConverter implements Converter<ResponseBody, String> {

            @Override
            public String convert(ResponseBody value) throws IOException {
                return IOUtils.toString(new InputStreamReader(value.byteStream()));
            }
        }
    }
}
