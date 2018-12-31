package webconnect.com.webconnect

/**
 * Created by amit on 10/8/17.
 */

import android.content.ContentValues.TAG
import android.net.TrafficStats
import android.util.Log
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import webconnect.com.webconnect.listener.AnalyticsListener
import webconnect.com.webconnect.listener.ProgressListener
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import android.provider.SyncStateContract.Helpers.update
import okio.*
import org.apache.commons.io.IOUtils
import webconnect.com.webconnect.ApiConfiguration.isDebug
import java.io.FileOutputStream
import java.io.OutputStream


/**
 * The type Call back.
 *
 * @param <T> the type parameter
</T> */
class Callback<T> {

    // RxAndroid
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

    // Enqueue
    internal class GetRequestCallbackEnqueue(private val param: WebParam) : okhttp3.Callback {
        var startTime = 0L

        init {
            startTime = System.currentTimeMillis()
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

        override fun onFailure(call: Call, e: IOException) {
            try {
                param.dialog?.let {
                    if (param.dialog?.isShowing!!) {
                        param.dialog?.dismiss()
                    }
                }
            } catch (e: Exception) {
                e.stackTrace
            }
            param.callback?.onError(e, getError(param, e), param.taskId)
        }

        override fun onResponse(call: Call, response: Response) {
            val timeTaken = System.currentTimeMillis() - startTime
            try {
                param.dialog?.let {
                    if (param.dialog?.isShowing!!) {
                        param.dialog?.dismiss()
                    }
                }
            } catch (e: Exception) {
                e.stackTrace
            }
            if (response.body() != null) {
                val responseString = response.body()?.string()!!
                if (response.isSuccessful) {
                    val obj = ApiConfiguration.getGson().fromJson(responseString, param.model)
                    param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, response.body()?.contentLength()!!, response.cacheResponse() != null)
                    param.callback?.onSuccess(obj, param.taskId)
                } else {
                    val obj = ApiConfiguration.getGson().fromJson(responseString, param.error)
                    param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, response.body()?.contentLength()!!, response.cacheResponse() != null)
                    param.callback?.onError(obj, "", param.taskId)
                }
            }
        }

    }

    // RxAndroid
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

    // Enqueue
    internal class PostRequestCallbackEnqueue(private val param: WebParam) : okhttp3.Callback {
        var startTime = 0L

        init {
            startTime = System.currentTimeMillis()
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

        override fun onFailure(call: Call, e: IOException) {
            try {
                param.dialog?.let {
                    if (param.dialog?.isShowing!!) {
                        param.dialog?.dismiss()
                    }
                }
            } catch (e: Exception) {
                e.stackTrace
            }
            param.callback?.onError(e, getError(param, e), param.taskId)
        }

        override fun onResponse(call: Call, response: Response) {
            val timeTaken = System.currentTimeMillis() - startTime
            try {
                param.dialog?.let {
                    if (param.dialog?.isShowing!!) {
                        param.dialog?.dismiss()
                    }
                }
            } catch (e: Exception) {
                e.stackTrace
            }
            if (response.body() != null) {
                val responseString = response.body()?.string()!!
                if (response.isSuccessful) {
                    val obj = ApiConfiguration.getGson().fromJson(responseString, param.model)
                    param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, response.body()?.contentLength()!!, response.cacheResponse() != null)
                    param.callback?.onSuccess(obj, param.taskId)
                } else {
                    val obj = ApiConfiguration.getGson().fromJson(responseString, param.error)
                    param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, response.body()?.contentLength()!!, response.cacheResponse() != null)
                    param.callback?.onError(obj, "", param.taskId)
                }
            }
        }

    }

    // RxAndroid
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

    // Enqueue
    internal class DownloadRequestCallbackEnqueue(private val param: WebParam) : okhttp3.Callback {
        var startTime = 0L

        init {
            startTime = System.currentTimeMillis()
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

        override fun onFailure(call: Call, e: IOException) {
            try {
                param.dialog?.let {
                    if (param.dialog?.isShowing!!) {
                        param.dialog?.dismiss()
                    }
                }
            } catch (e: Exception) {
                e.stackTrace
            }
            param.callback?.onError(e, getError(param, e), param.taskId)
        }

        override fun onResponse(call: Call, response: Response) {
            val timeTaken = System.currentTimeMillis() - startTime
            try {
                param.dialog?.let {
                    if (param.dialog?.isShowing!!) {
                        param.dialog?.dismiss()
                    }
                }
            } catch (e: Exception) {
                e.stackTrace
            }
            var `object`: Any? = null
            if (response.body() != null) {
                if (response.isSuccessful) {
                    val body = response.body()
                    var out: OutputStream? = null
                    try {
                        out = FileOutputStream(param.file!!)

                        IOUtils.copy(body!!.byteStream(), out)
                        `object` = param.file
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, response.body()?.contentLength()!!, response.cacheResponse() != null)
                    param.callback?.onSuccess(this.param.file, this.param.taskId)
                } else {
                    param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, response.body()?.contentLength()!!, response.cacheResponse() != null)
                    if (response.body() != null) {
                        param.callback?.onError(response.body()!!.string(), "", param.taskId)
                    } else {
                        param.callback?.onError(Throwable(""), "", param.taskId)
                    }
                }
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

        override fun onProgress(bytesDownloaded: Long, totalBytes: Long, progress: Float) {
            param.progressListener?.onProgress(bytesDownloaded, totalBytes, progress)
        }
    }

    internal class UploadProgressCallback(private val param: WebParam) : ProgressListener {

        override fun onProgress(bytesDownloaded: Long, totalBytes: Long, progress: Float) {
            param.progressListener?.onProgress(bytesDownloaded, totalBytes, progress)
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
