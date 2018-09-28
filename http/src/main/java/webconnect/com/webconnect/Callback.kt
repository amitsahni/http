package webconnect.com.webconnect

/**
 * Created by amit on 10/8/17.
 */

import android.util.Log
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import webconnect.com.webconnect.listener.AnalyticsListener
import webconnect.com.webconnect.listener.ProgressListener
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.cert.CertificateException
import java.util.concurrent.TimeoutException

/**
 * The type Call back.
 *
 * @param <T> the type parameter
</T> */
class Callback<T> {

    internal class GetRequestCallback(private val param: WebParam) : Observer<Any> {

        override fun onSubscribe(@io.reactivex.annotations.NonNull d: Disposable) {
            try {
                param.dialog?.let {
                    if (!param.dialog?.isShowing!!) {
                        param.dialog?.show()
                    }
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }

        override fun onNext(@io.reactivex.annotations.NonNull response: Any) {
            if (response is ObserverModel) {
                response.also {
                    if (it.type == 1) {
                        param.callback?.onSuccess(it.model, param.taskId)
                    } else {
                        param.callback?.onError(it.model, "", param.taskId)
                    }
                }
            }
        }

        override fun onError(@NonNull e: Throwable) {
            param.callback?.onError(e, getError(param, e), param.taskId)
            onComplete()
        }

        override fun onComplete() {
            try {
                param.dialog?.let {
                    if (param.dialog?.isShowing!!) {
                        param.dialog?.dismiss()
                    }
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

    internal open class PostRequestCallback(private val param: WebParam) : Observer<Any> {

        override fun onSubscribe(@io.reactivex.annotations.NonNull d: Disposable) {
            try {
                param.dialog?.let {
                    if (!param.dialog?.isShowing!!) {
                        param.dialog?.show()
                    }
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }

        override fun onNext(@io.reactivex.annotations.NonNull response: Any) {
            if (response is ObserverModel) {
                response.also {
                    if (it.type == 1) {
                        param.callback?.onSuccess(it.model, param.taskId)
                    } else {
                        param.callback?.onError(it.model, "", param.taskId)
                    }
                }
            }
        }

        override fun onError(@NonNull e: Throwable) {
            param.callback?.onError(e, getError(param, e), param.taskId)
            onComplete()
        }

        override fun onComplete() {
            try {
                param.dialog?.let {
                    if (param.dialog?.isShowing!!) {
                        param.dialog?.dismiss()
                    }
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

    internal class DownloadRequestCallback(private val param: WebParam) : Observer<Any> {

        override fun onSubscribe(@NonNull d: Disposable) {
            try {
                param.dialog?.let {
                    if (!param.dialog?.isShowing!!) {
                        param.dialog?.show()
                    }
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }

        override fun onNext(@NonNull o: Any) {
            param.callback?.onSuccess(this.param.file, this.param.taskId)
        }

        override fun onError(@NonNull e: Throwable) {
            param.callback?.onError(e, getError(param, e), param.taskId)
            onComplete()
        }

        override fun onComplete() {
            try {
                param.dialog?.let {
                    if (param.dialog?.isShowing!!) {
                        param.dialog?.dismiss()
                    }
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

    internal class Analytics : AnalyticsListener {
        var TAG = "Analytics"

        override fun onReceived(timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) {
            if (ApiConfiguration.isDebug()) {
                Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis)
                Log.d(TAG, " bytesSent : " + bytesSent)
                Log.d(TAG, " bytesReceived : " + bytesReceived)
                Log.d(TAG, " isFromCache : " + isFromCache)
            }
        }
    }


    internal class UploadRequestCallback(param: WebParam) : PostRequestCallback(param)

    internal class ProgressCallback(private val param: WebParam) : ProgressListener {

        override fun onProgress(bytesDownloaded: Long, totalBytes: Long) {
            param.progressListener?.onProgress(bytesDownloaded, totalBytes)
        }
    }

    internal class UploadProgressCallback(private val param: WebParam) : ProgressListener {

        override fun onProgress(bytesDownloaded: Long, totalBytes: Long) {
            param.progressListener?.onProgress(bytesDownloaded, totalBytes)
        }
    }

    companion object {

        private fun getError(param: WebParam, t: Throwable): String {
            var errors = ""
            if (param.context == null) return errors

            if (t.javaClass.name.contains(UnknownHostException::class.java.name)) {
                errors = param.context?.getString(R.string.error_internet_connection).toString()
            } else if (t.javaClass.name.contains(TimeoutException::class.java.name)
                    || t.javaClass.name.contains(SocketTimeoutException::class.java.name)
                    || t.javaClass.name.contains(ConnectException::class.java.name)) {
                errors = param.context?.getString(R.string.error_server_connection).toString()
            } else if (t.javaClass.name.contains(CertificateException::class.java.name)) {
                errors = param.context?.getString(R.string.error_certificate_exception).toString()
            } else {
                errors = t.toString()
            }
            return errors
        }
    }

}
