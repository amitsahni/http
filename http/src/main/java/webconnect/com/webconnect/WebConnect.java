package webconnect.com.webconnect;

import android.content.Context;
import android.support.annotation.NonNull;


/**
 * The type Web connect.
 */
public class WebConnect {
    private static volatile WebConnect sWebConnect;

    private WebConnect() {

    }

    /**
     * Get web connect.
     *
     * @return the web connect
     */
    public static WebConnect get() {
        if (sWebConnect == null) {
            synchronized (WebConnect.class) {
                if (sWebConnect == null) {
                    sWebConnect = new WebConnect();
                }
            }
        }
        return sWebConnect;
    }

    /**
     * With builder.
     *
     * @param context the context
     * @param url     the url
     * @return the builder
     */
    public static Builder with(@NonNull Context context, @NonNull String url) {
        return new Builder(context, url);
    }

    /**
     * With builder.
     *
     * @param url     the url
     * @return the builder
     */
    public static Builder with(@NonNull String url) {
        return new Builder(null, url);
    }
}
