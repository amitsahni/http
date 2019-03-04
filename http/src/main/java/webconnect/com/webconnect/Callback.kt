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
import webconnect.com.webconnect.listener.OnSuccessListener
import webconnect.com.webconnect.model.SuccessModel
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
        val startTime: Long = System.currentTimeMillis()

        init {
            param.loaderListener?.loader(true)
        }

        override fun onFailure(call: Call, e: IOException) {
            param.loaderListener?.loader(false)
            param.failure?.onFailure(e, e.message.toString())
            FailureLiveData.failure.postValue(e.message.toString())
        }

        override fun onResponse(call: Call, response: Response) {
            val timeTaken = System.currentTimeMillis() - startTime
            param.loaderListener?.loader(false)
            response.body()?.also {
                var responseString = ""
                runBlocking(Dispatchers.IO) {
                    responseString = it.string()
                    param.responseListener?.response(responseString.formatJson())
                    param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, it.contentLength(), response.cacheResponse() != null)
                }
                if (response.isSuccessful) {
                    val obj = responseString.fromJson(param.model)
                    param.success?.onSuccess(obj)
                    SuccessLiveData.success.postValue(responseString)
                } else {
                    val obj = responseString.fromJson(param.error)
                    param.err?.onError(obj)
                    ErrorLiveData.error.postValue(responseString)
                }
            }
        }
    }

    // Enqueue
    internal class PostRequestCallbackEnqueue(private val param: WebParam) : okhttp3.Callback {
        val startTime: Long = System.currentTimeMillis()

        init {
            param.loaderListener?.loader(true)
        }

        override fun onFailure(call: Call, e: IOException) {
            param.loaderListener?.loader(false)
            param.failure?.onFailure(e, e.message.toString())
            FailureLiveData.failure.postValue(e.message.toString())
        }

        override fun onResponse(call: Call, response: Response) {
            val timeTaken = System.currentTimeMillis() - startTime
            param.loaderListener?.loader(false)
            response.body()?.also {
                var responseString = ""
                runBlocking(Dispatchers.IO) {
                    responseString = it.string()
                    param.responseListener?.response(responseString.formatJson())
                    param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, it.contentLength(), response.cacheResponse() != null)
                }
                if (response.isSuccessful) {
                    val obj = responseString.fromJson(param.model)
                    param.success?.onSuccess(obj)
                    SuccessLiveData.success.postValue(responseString)
                } else {
                    val obj = responseString.fromJson(param.error)
                    param.err?.onError(obj)
                    ErrorLiveData.error.postValue(responseString)
                }
            }
        }

    }

    // Enqueue
    internal class DownloadRequestCallbackEnqueue(private val param: WebParam) : okhttp3.Callback {
        var startTime = System.currentTimeMillis()

        init {
            param.loaderListener?.loader(true)
        }

        override fun onFailure(call: Call, e: IOException) {
            param.loaderListener?.loader(false)
            param.failure?.onFailure(e, e.message.toString())
            FailureLiveData.failure.postValue(e.message.toString())
        }

        override fun onResponse(call: Call, response: Response) {
            val timeTaken = System.currentTimeMillis() - startTime
            param.loaderListener?.loader(false)
            response.body()?.also {
                if (response.isSuccessful) {
                    runBlocking(Dispatchers.IO) {
                        try {
                            val out = FileOutputStream(param.file!!)
                            IOUtils.copy(it.byteStream(), out)
                            param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, it.contentLength(), response.cacheResponse() != null)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    param.success?.onSuccess(param.file!!)
                } else {
                    var error = ""
                    runBlocking(Dispatchers.IO) {
                        error = it.string()
                    }
                    param.err?.onError(error)
                    ErrorLiveData.error.postValue(error)
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
}
