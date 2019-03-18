package webconnect.com.webconnect;

import android.content.Context;
import android.support.annotation.NonNull;


/**
 * The type Web connect.
 */
object WebConnect {
    @JvmStatic
    /**
     * With builder.
     *
     * @param url     the url
     * @return the builder
     */
    fun with(url: String): Builder {
        return Builder(url)
    }
}
