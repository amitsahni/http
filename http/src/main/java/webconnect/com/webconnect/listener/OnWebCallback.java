package webconnect.com.webconnect.listener;

/**
 * Created by clickapps on 18/1/18.
 */

import android.support.annotation.Nullable;

/**
 * The interface On web callback.
 */
public interface OnWebCallback {
    /**
     * On success.
     *
     * @param <T>    the type parameter
     * @param object the object
     * @param taskId the task id
     */
    <T> void onSuccess(@Nullable T object, int taskId);

    /**
     * On error.
     *
     * @param <T>    the type parameter
     * @param object the object
     * @param error  the error
     * @param taskId the task id
     */
    <T> void onError(@Nullable T object, String error, int taskId);
}
