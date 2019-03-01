package webconnect.com.webconnect

/**
 * Created by amit on 10/8/17.
 */

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Response
import org.apache.commons.io.IOUtils
import webconnect.com.webconnect.listener.AnalyticsListener
import webconnect.com.webconnect.observer.ErrorLiveData
import webconnect.com.webconnect.observer.FailureLiveData
import webconnect.com.webconnect.observer.SuccessLiveData
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
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
class Callback {

    // Enqueue
    internal class GetRequestCallbackEnqueue(private val param: WebParam) : okhttp3.Callback {
        var startTime = 0L

        init {
            startTime = System.currentTimeMillis()
            param.loaderListener?.loader(true)
        }

        override fun onFailure(call: Call, e: IOException) {
            param.loaderListener?.loader(false)
            param.failure?.onFailure(e, getError(param, e))
            FailureLiveData.failure.postValue(getError(param, e))
        }

        override fun onResponse(call: Call, response: Response) {
            runOnUiThread {
                val timeTaken = System.currentTimeMillis() - startTime
                param.loaderListener?.loader(false)
                try {
                    param.dialog?.let {
                        if (param.dialog?.isShowing!!) {
                            param.dialog?.dismiss()
                        }
                    }
                } catch (e: Exception) {
                    e.stackTrace
                }
                response.body()?.let {
                    var responseString = ""
                    runBlocking(Dispatchers.IO) {
                        responseString = it.string()
                    }
                    if (response.isSuccessful) {
                        param.responseListener?.response(responseString)
                        val obj = responseString.fromJson(param.model)
                        param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, response.body()?.contentLength()!!, response.cacheResponse() != null)
                        param.success?.onSuccess(obj)
                        SuccessLiveData.success.postValue(responseString)
                    } else {
                        var obj: Any? = null
                        try {
                            param.responseListener?.response(responseString)
                            obj = responseString.fromJson(param.error)
                            param.err?.onError(obj)
                            ErrorLiveData.error.postValue(responseString)
                        } catch (e: Exception) {
                            param.failure?.onFailure(e, getError(param, e))
                            FailureLiveData.failure.postValue(getError(param, e))
                        }
                        param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, response.body()?.contentLength()!!, response.cacheResponse() != null)

                    }
                }

            }
        }
    }

    // Enqueue
    internal class PostRequestCallbackEnqueue(private val param: WebParam) : okhttp3.Callback {
        var startTime = 0L
        init {
            startTime = System.currentTimeMillis()
            param.loaderListener?.loader(true)
        }

        override fun onFailure(call: Call, e: IOException) {
            param.loaderListener?.loader(false)
            param.failure?.onFailure(e, getError(param, e))
            FailureLiveData.failure.postValue(getError(param, e))
        }

        override fun onResponse(call: Call, response: Response) {
            runOnUiThread {
                val timeTaken = System.currentTimeMillis() - startTime
                param.loaderListener?.loader(false)
                try {
                    param.dialog?.let {
                        if (param.dialog?.isShowing!!) {
                            param.dialog?.dismiss()
                        }
                    }
                } catch (e: Exception) {
                    e.stackTrace
                }
                response.body()?.let {
                    var responseString = ""
                    runBlocking(Dispatchers.IO) {
                        responseString = it.string()
                    }
                    param.responseListener?.response(responseString)
                    if (response.isSuccessful) {
                        val obj = responseString.fromJson(param.model)
                        param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, response.body()?.contentLength()!!, response.cacheResponse() != null)
                        param.success?.onSuccess(obj)
                        SuccessLiveData.success.postValue(responseString)
                    } else {
                        var obj: Any? = null
                        try {
                            obj = responseString.fromJson(param.error)
                            param.err?.onError(obj)
                            ErrorLiveData.error.postValue(responseString)
                        } catch (e: Exception) {
                            param.failure?.onFailure(e, getError(param, e))
                            FailureLiveData.failure.postValue(getError(param, e))
                        }
                        param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, response.body()?.contentLength()!!, response.cacheResponse() != null)

                    }
                }
            }
        }

    }


    // Enqueue
    internal class DownloadRequestCallbackEnqueue(private val param: WebParam) : okhttp3.Callback {
        var startTime = 0L

        init {
            startTime = System.currentTimeMillis()
            param.loaderListener?.loader(true)
        }

        override fun onFailure(call: Call, e: IOException) {
            param.loaderListener?.loader(false)
            param.failure?.onFailure(e, getError(param, e))
            FailureLiveData.failure.postValue(getError(param, e))
        }

        override fun onResponse(call: Call, response: Response) {
            runOnUiThread {
                val timeTaken = System.currentTimeMillis() - startTime
                param.loaderListener?.loader(false)
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
                response.body()?.let {
                    if (response.isSuccessful) {
                        runBlocking(Dispatchers.IO) {
                            val out: OutputStream?
                            try {
                                out = FileOutputStream(param.file!!)
                                IOUtils.copy(it.byteStream(), out)

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        `object` = param.file
                        param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, response.body()?.contentLength()!!, response.cacheResponse() != null)
                        param.success?.onSuccess(this.param.file!!)
                    } else {
                        param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, response.body()?.contentLength()!!, response.cacheResponse() != null)
                        if (response.body() != null) {
                            var error = ""
                            runBlocking(Dispatchers.IO) {
                                error = response.body()!!.string()
                            }
                            param.err?.onError(error)
                            ErrorLiveData.error.postValue(error)
                        } else {
                            param.err?.onError("")
                            ErrorLiveData.error.postValue("")
                        }
                    }
                }
            }
        }

    }

    internal class Analytics : AnalyticsListener {
        var TAG = "Analytics"

        override fun onReceived(timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) {
            if (ApiConfiguration.isDebug) {
                Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis)
                Log.d(TAG, " bytesSent : " + bytesSent)
                Log.d(TAG, " bytesReceived : " + bytesReceived)
                Log.d(TAG, " isFromCache : " + isFromCache)
            }
        }
    }


    companion object {

        private fun getError(param: WebParam, t: Throwable): String {
            var errors = ""
            var context: Context? = null
            if (param.context != null) {
                context = param.context
            } else {

            }

            if (t.javaClass.name.contains(UnknownHostException::class.java.name)) {
                errors = context?.getString(R.string.error_internet_connection).toString()
            } else if (t.javaClass.name.contains(TimeoutException::class.java.name)
                    || t.javaClass.name.contains(SocketTimeoutException::class.java.name)
                    || t.javaClass.name.contains(ConnectException::class.java.name)) {
                errors = context?.getString(R.string.error_server_connection).toString()
            } else if (t.javaClass.name.contains(CertificateException::class.java.name)) {
                errors = context?.getString(R.string.error_certificate_exception).toString()
            } else {
                errors = t.toString()
            }
            return errors
        }
    }

}
