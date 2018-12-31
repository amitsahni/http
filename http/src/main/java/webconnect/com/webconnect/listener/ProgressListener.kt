package webconnect.com.webconnect.listener

/**
 * Created by clickapps on 18/1/18.
 */

interface ProgressListener {
    fun onProgress(bytesRead: Long, contentLength: Long, progress: Float)
}
