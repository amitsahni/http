package webconnect.com.webconnect.listener;

/**
 * Created by clickapps on 18/1/18.
 */

public interface ProgressListener {
    void update(long bytesRead, long contentLength);
}
