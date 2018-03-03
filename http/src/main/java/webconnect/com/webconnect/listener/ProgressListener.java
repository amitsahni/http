package webconnect.com.webconnect.listener;

/**
 * Created by clickapps on 18/1/18.
 */

public interface ProgressListener {
    void onProgress(long bytesRead, long contentLength);
}
