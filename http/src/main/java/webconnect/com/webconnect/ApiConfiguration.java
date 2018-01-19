package webconnect.com.webconnect;

import android.app.Application;
import android.support.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * Created by amit on 10/8/17.
 */

public class ApiConfiguration {
    static int cacheSize = 10 * 1024 * 1024;
    private static String sBASE_URL = "";
    private static long sCONNECT_TIMEOUT_MILLIS = 10 * 1000, sREAD_TIMEOUT_MILLIS = 20 * 1000;
    private static Gson sGSON = new GsonBuilder()
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .setLenient()
            .create();
    private static boolean sIsDEBUG = true;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Dispatcher dispatcher = new Dispatcher();

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    static String getBaseUrl() {
        return sBASE_URL;
    }

    static long getConnectTimeOut() {
        return sCONNECT_TIMEOUT_MILLIS;
    }

    static long getReadTimeOut() {
        return sREAD_TIMEOUT_MILLIS;
    }

    static Gson getGson() {
        return sGSON;
    }

    public static boolean isDebug() {
        return sIsDEBUG;
    }

    public static class Builder {
        private String baseUrl = "";
        private Application context;
        private long connectTimeOut = 10 * 1000, readTimeOut = 20 * 1000;
        private boolean isDebug = true;

        public Builder(Application context) {
            this.context = context;
        }

        public Builder baseUrl(@NonNull String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder timeOut(long connectTimeOut, long readTimeOut) {
            this.connectTimeOut = connectTimeOut;
            this.readTimeOut = readTimeOut;
            return this;
        }

        public Builder debug(boolean isDebug) {
            this.isDebug = isDebug;
            return this;
        }

        public void config() {
            sBASE_URL = baseUrl;
            sCONNECT_TIMEOUT_MILLIS = connectTimeOut;
            sREAD_TIMEOUT_MILLIS = readTimeOut;
            sIsDEBUG = isDebug;
            dispatcher.setMaxRequestsPerHost(2);
            dispatcher.setMaxRequests(10);
            okhttp3.logging.HttpLoggingInterceptor interceptor = new okhttp3.logging.HttpLoggingInterceptor();
            interceptor.setLevel(sIsDEBUG ?
                    okhttp3.logging.HttpLoggingInterceptor.Level.BODY : okhttp3.logging.HttpLoggingInterceptor.Level.NONE);
            okHttpClient = okHttpClient
                    .newBuilder()
                    .cache(new Cache(context.getCacheDir(), cacheSize))
                    .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                    .readTimeout(readTimeOut, TimeUnit.SECONDS)
                    .writeTimeout(connectTimeOut, TimeUnit.SECONDS)
                    .dispatcher(dispatcher)
                    .addInterceptor(interceptor)
                    .build();
            AndroidNetworking.initialize(context, okHttpClient);
        }
    }

}
