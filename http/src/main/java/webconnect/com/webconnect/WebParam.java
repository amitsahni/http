package webconnect.com.webconnect;

import android.app.Dialog;
import android.content.Context;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import webconnect.com.webconnect.listener.AnalyticsListener;
import webconnect.com.webconnect.listener.OnWebCallback;
import webconnect.com.webconnect.listener.ProgressListener;


/**
 * The type Web param.
 */
public class WebParam implements Serializable {
    Context context;
    String url, baseUrl;
    HttpType httpType = HttpType.GET;
    Map<String, ?> requestParam = new LinkedHashMap<>();
    Map<String, String> queryParam = new LinkedHashMap<>();
    Map<String, String> multipartParam = new LinkedHashMap<>();
    Map<String, File> multipartParamFile = new LinkedHashMap<>();
    Map<String, String> headerParam = new LinkedHashMap<>();
    OnWebCallback callback;
    ProgressListener progressListener;
    AnalyticsListener analyticsListener;
    Dialog dialog;
    Class<?> model = Object.class;
    Class<?> error = Object.class;
    int taskId;
    long connectTimeOut = 0, readTimeOut = 0;
    boolean isCacheEnabled = false, debug = false;
    File file;
    long requestBodyContentlength = -1;

    public enum HttpType {
        GET,
        POST,
        PUT,
        PATCH,
        DELETE,
        HEAD,
        OPTIONS,
        MULTIPART,
        DOWNLOAD
    }
}
