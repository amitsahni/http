package webconnect.com.webconnect;

/**
 * Created by amit on 10/8/17.
 */

import android.util.Log;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import webconnect.com.webconnect.listener.AnalyticsListener;
import webconnect.com.webconnect.listener.ProgressListener;

/**
 * The type Call back.
 *
 * @param <T> the type parameter
 */
public class Callback<T> {

    static class GetRequestCallback implements Observer<Object> {

        private WebParam param;

        public GetRequestCallback(WebParam param) {
            this.param = param;
        }

        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
            if (param.dialog != null &&
                    !param.dialog.isShowing()) {
                param.dialog.show();
            }
        }

        @Override
        public void onNext(@io.reactivex.annotations.NonNull Object response) {
            if (param.callback != null) {
                param.callback.onSuccess(response, param.taskId);
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            if (param.callback != null) {
                param.callback.onError(e, getError(param, e), param.taskId);
            }
        }

        @Override
        public void onComplete() {
            if (param.dialog != null &&
                    param.dialog.isShowing()) {
                param.dialog.dismiss();
            }
        }
    }

    static class PostRequestCallback implements Observer<Object> {

        private WebParam param;

        public PostRequestCallback(WebParam param) {
            this.param = param;
        }

        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
            if (param.dialog != null &&
                    !param.dialog.isShowing()) {
                param.dialog.show();
            }
        }

        @Override
        public void onNext(@io.reactivex.annotations.NonNull Object response) {
            if (param.callback != null) {
                param.callback.onSuccess(response, param.taskId);
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            if (param.callback != null) {
                param.callback.onError(e, getError(param, e), param.taskId);
            }
        }

        @Override
        public void onComplete() {
            if (param.dialog != null &&
                    param.dialog.isShowing()) {
                param.dialog.dismiss();
            }
        }
    }

    static class DownloadRequestCallback implements Observer<Object> {

        private WebParam param;

        public DownloadRequestCallback(WebParam param) {
            this.param = param;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            if (param.dialog != null &&
                    !param.dialog.isShowing()) {
                param.dialog.show();
            }
        }

        @Override
        public void onNext(@NonNull Object o) {
            if (param.callback != null) {
                param.callback.onSuccess(this.param.file, this.param.taskId);
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            if (param.callback != null) {
                param.callback.onError(e, getError(param, e), param.taskId);
            }
        }

        @Override
        public void onComplete() {
            if (param.dialog != null &&
                    param.dialog.isShowing()) {
                param.dialog.dismiss();
            }
        }
    }

    static class Analytics implements AnalyticsListener {
        String TAG = "Analytics";

        @Override
        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
            if (ApiConfiguration.isDebug()) {
                Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                Log.d(TAG, " bytesSent : " + bytesSent);
                Log.d(TAG, " bytesReceived : " + bytesReceived);
                Log.d(TAG, " isFromCache : " + isFromCache);
            }
        }
    }


    static class UploadRequestCallback extends PostRequestCallback {

        public UploadRequestCallback(WebParam param) {
            super(param);
        }
    }

    static class ProgressCallback implements ProgressListener {

        private WebParam param;

        public ProgressCallback(WebParam param) {
            this.param = param;
        }

        @Override
        public void onProgress(long bytesDownloaded, long totalBytes) {
            if (param.progressListener != null) {
                param.progressListener.onProgress(bytesDownloaded, totalBytes);
            }
        }
    }

    static class UploadProgressCallback implements ProgressListener {

        private WebParam param;

        public UploadProgressCallback(WebParam param) {
            this.param = param;
        }

        @Override
        public void onProgress(long bytesDownloaded, long totalBytes) {
            if (param.progressListener != null) {
                param.progressListener.onProgress(bytesDownloaded, totalBytes);
            }
        }
    }

    private static String getError(WebParam param, Throwable t) {
        String errors = "";
        if (param.context == null) return errors;

        if (t.getClass().getName().contains(UnknownHostException.class.getName())) {
            errors = param.context.getString(R.string.error_internet_connection);
        } else if (t.getClass().getName().contains(TimeoutException.class.getName())
                || t.getClass().getName().contains(SocketTimeoutException.class.getName())
                || t.getClass().getName().contains(ConnectException.class.getName())) {
            errors = param.context.getString(R.string.error_server_connection);
        } else if (t.getClass().getName().contains(CertificateException.class.getName())) {
            errors = param.context.getString(R.string.error_certificate_exception);
        } else {
            errors = t.toString();
        }
        return errors;
    }

}
