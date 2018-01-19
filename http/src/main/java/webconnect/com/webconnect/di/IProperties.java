package webconnect.com.webconnect.di;

import android.app.Dialog;
import android.support.annotation.NonNull;

import java.util.Map;

import webconnect.com.webconnect.listener.AnalyticsListener;
import webconnect.com.webconnect.listener.OnWebCallback;

/**
 * Created by amit on 23/9/17.
 */

public interface IProperties {

    IProperties baseUrl(@NonNull String url);

    IProperties headerParam(@NonNull Map<String, String> headerParam);

    IProperties queryParam(@NonNull Map<String, String> queryParam);

    IProperties callback(@NonNull OnWebCallback callback);

    IProperties analyticsListener(@NonNull AnalyticsListener callback);

    IProperties callback(@NonNull OnWebCallback callback,
                         @NonNull Class<?> success, @NonNull Class<?> error);

    IProperties taskId(int taskId);

    IProperties timeOut(long connectTimeOut, long readTimeOut);

    IProperties progressDialog(Dialog dialog);

    IProperties cache(boolean isCache);

    void connect();
}
