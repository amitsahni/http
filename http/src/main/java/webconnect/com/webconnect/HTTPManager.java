package webconnect.com.webconnect;

import android.support.annotation.NonNull;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by clickapps on 28/12/17.
 */

public class HTTPManager {
    int cacheSize = 10 * 1024 * 1024; // 10 MB
    private static volatile HTTPManager sManager;
    private Dispatcher dispatcher = new Dispatcher();
    final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    OkHttpClient okHttpClient = ApiConfiguration.getOkHttpClient();

    private HTTPManager() {
    }

    /**
     * Get web connect.
     *
     * @return the web connect
     */
    static HTTPManager get() {
        if (sManager == null) {
            synchronized (HTTPManager.class) {
                if (sManager == null) {
                    sManager = new HTTPManager();
                }
            }
        }
        return sManager;
    }

    String convertFormData(@NonNull Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }

    OkHttpClient getDefaultOkHttpClient(@NonNull WebParam webParam) {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            Cache cache = new Cache(ApiConfiguration.getContext().getCacheDir(), cacheSize);
            builder.cache(cache);
            builder.connectTimeout(ApiConfiguration.getConnectTimeOut(), TimeUnit.SECONDS);
            builder.writeTimeout(ApiConfiguration.getConnectTimeOut(), TimeUnit.SECONDS);
            builder.readTimeout(ApiConfiguration.getReadTimeOut(), TimeUnit.SECONDS);
            dispatcher.setMaxRequestsPerHost(2);
            dispatcher.setMaxRequests(10);
            builder.dispatcher(dispatcher);
            interceptor.setLevel(ApiConfiguration.isDebug() ?
                    HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            builder.addInterceptor(interceptor);
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }
}
