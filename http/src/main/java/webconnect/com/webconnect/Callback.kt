package webconnect.com.webconnect

/**
 * Created by amit on 10/8/17.
 */

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Response
import org.apache.commons.io.IOUtils
import webconnect.com.webconnect.observer.ErrorLiveData
import webconnect.com.webconnect.observer.FailureLiveData
import webconnect.com.webconnect.observer.SuccessLiveData
import java.io.FileOutputStream
import java.io.IOException


/**
 * The type Call back.
 *
 * @param <T> the type parameter
</T> */
class Callback {

    // Enqueue
    internal class GetRequestCallbackEnqueue(private val param: WebParam) : okhttp3.Callback {
        private val startTime: Long = System.currentTimeMillis()

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
                    responseString.formatJson()?.let {
                        param.responseListener?.response(it)
                    }
                    param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, it.contentLength(), response.cacheResponse() != null)
                }
                if (response.isSuccessful) {
                    responseString.fromJson(param.model)?.let {
                        param.success?.onSuccess(it)
                    }
                    SuccessLiveData.success.postValue(responseString)
                } else {
                    responseString.fromJson(param.error)?.let {
                        param.err?.onError(it)
                    }
                    ErrorLiveData.error.postValue(responseString)
                }
            }
        }
    }

    // Enqueue
    internal class PostRequestCallbackEnqueue(private val param: WebParam) : okhttp3.Callback {
        private val startTime: Long = System.currentTimeMillis()

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
                    responseString.formatJson()?.let {
                        param.responseListener?.response(it)
                    }
                    param.analyticsListener?.onReceived(timeTaken, if (call.request().body() == null) -1 else call.request().body()?.contentLength()!!, it.contentLength(), response.cacheResponse() != null)
                }
                if (response.isSuccessful) {
                    responseString.fromJson(param.model)?.let {
                        param.success?.onSuccess(it)
                    }
                    SuccessLiveData.success.postValue(responseString)
                } else {
                    responseString.fromJson(param.error)?.let {
                        param.err?.onError(it)
                    }
                    ErrorLiveData.error.postValue(responseString)
                }
            }
        }

    }

    // Enqueue
    internal class DownloadRequestCallbackEnqueue(private val param: WebParam) : okhttp3.Callback {
        private val startTime = System.currentTimeMillis()

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
}
