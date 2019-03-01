package webconnect.com.webconnect

import android.content.Context


/**
 * The type Web connect.
 */
object WebConnect1 {

    /**
     * With builder.
     *
     * @param context the context
     * @param url     the url
     * @return the builder
     */
    fun with(context: Context, url: String): Builder {
        return Builder(context, url)
    }

    /**
     * With builder.
     *
     * @param url     the url
     * @return the builder
     */
    fun with(url: String): Builder {
        return Builder(null, url)
    }
}
