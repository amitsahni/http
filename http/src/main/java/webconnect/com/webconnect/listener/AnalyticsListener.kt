package webconnect.com.webconnect.listener

/**
 * Created by clickapps on 18/1/18.
 */

interface AnalyticsListener {
    fun onReceived(timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean)
}
