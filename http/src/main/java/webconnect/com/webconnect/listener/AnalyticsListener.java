package webconnect.com.webconnect.listener;

/**
 * Created by clickapps on 18/1/18.
 */

public interface AnalyticsListener {
    void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache);
}
