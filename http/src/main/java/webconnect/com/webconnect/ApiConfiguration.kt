package webconnect.com.webconnect

import android.os.Environment
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by amit on 10/8/17.
 */

object ApiConfiguration {
    private var cacheSize = 10 * 1024 * 1024
    internal var baseUrl = ""
        private set
    private var connectTimeOut = (10 * 1000).toLong()
    private var readTimeOut = (20 * 1000).toLong()
    internal var isDebug = true
        private set
    internal var okHttpClient = OkHttpClient()
        private set

    fun baseUrl(baseUrl: String): ApiConfiguration {
        this.baseUrl = baseUrl
        return this
    }

    fun timeOut(connectTimeOut: Long, readTimeOut: Long): ApiConfiguration {
        this.connectTimeOut = connectTimeOut
        this.readTimeOut = readTimeOut
        return this
    }

    fun logging(boolean: Boolean): ApiConfiguration {
        this.isDebug = boolean
        return this
    }

    fun config() {
        val interceptor = okhttp3.logging.HttpLoggingInterceptor()
        interceptor.level = if (this.isDebug)
            okhttp3.logging.HttpLoggingInterceptor.Level.BODY
        else
            okhttp3.logging.HttpLoggingInterceptor.Level.NONE

        okHttpClient = okHttpClient
                .newBuilder()
                .cache(Cache(Environment.getDownloadCacheDirectory(), cacheSize.toLong()))
                .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .writeTimeout(connectTimeOut, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()
    }

}
