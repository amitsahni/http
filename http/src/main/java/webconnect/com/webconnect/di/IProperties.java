package webconnect.com.webconnect.di;

import android.app.Dialog;
import android.support.annotation.NonNull;

import java.util.Map;

import webconnect.com.webconnect.listener.AnalyticsListener;
import webconnect.com.webconnect.listener.OnWebCallback;

/**
 * Created by amit on 23/9/17.
 */

public interface IProperties<T> {

    T baseUrl(@NonNull String url);

    T headerParam(@NonNull Map<String, String> headerParam);

    T queryParam(@NonNull Map<String, String> queryParam);

    T callback(@NonNull OnWebCallback callback);

    T analyticsListener(@NonNull AnalyticsListener callback);

    T callback(@NonNull OnWebCallback callback,
               @NonNull Class<?> success, @NonNull Class<?> error);

    T taskId(int taskId);

    T timeOut(long connectTimeOut, long readTimeOut);

    T progressDialog(Dialog dialog);

    T cache(boolean isCache);

    void connect();
}
