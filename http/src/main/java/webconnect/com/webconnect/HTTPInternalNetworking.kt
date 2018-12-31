package webconnect.com.webconnect

import android.content.ContentValues
import android.util.Log
import java.io.IOException

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Okio
import okio.Source

/**
 * Created by clickapps on 28/12/17.
 */

class HTTPInternalNetworking {

    class ProgressResponseBody internal constructor(private val responseBody: ResponseBody, private val webParam: WebParam) : ResponseBody() {
        private var bufferedSource: BufferedSource? = null

        override fun contentType(): MediaType? {
            return responseBody.contentType()
        }

        override fun contentLength(): Long {
            return responseBody.contentLength()
        }

        override fun source(): BufferedSource? {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()))
            }
            return bufferedSource
        }

        private fun source(source: Source): Source {
            return object : ForwardingSource(source) {
                internal var totalBytesRead = 0L

                @Throws(IOException::class)
                override fun read(sink: Buffer, byteCount: Long): Long {
                    val bytesRead = super.read(sink, byteCount)
                    val length = responseBody.contentLength()
                    // if (length == -1L) return bytesRead
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                    var progress = 0.0f
                    try {
                        progress = (100 * totalBytesRead / length).toFloat()
                        Log.d(HTTPInternalNetworking::class.java.simpleName, "progress = " + progress)
                    } catch (e: Exception) {
                        Log.e(HTTPInternalNetworking::class.java.simpleName, "Exception = " + e.message)
                    }
                    if (webParam.progressListener != null)
                        webParam.progressListener!!.onProgress(totalBytesRead, responseBody.contentLength(), progress)
                    return bytesRead
                }
            }
        }
    }
}
