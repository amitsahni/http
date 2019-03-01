package webconnect.com.webconnect.di

import android.app.Dialog
import webconnect.com.webconnect.QueryMap
import webconnect.com.webconnect.listener.*

/**
 * Created by amit on 23/9/17.
 */

interface IProperties<T> {

    fun baseUrl(url: String): T

    fun headerParam(headerParam: Map<String, String>): T

    fun queryParam(queryParam: QueryMap<String, String>): T

    @Deprecated("Will deprecate")
    fun callback(callback: OnWebCallback): T

    fun analyticsListener(callback: AnalyticsListener): T

    @Deprecated("Will deprecate")
    fun callback(callback: OnWebCallback,
                 success: Class<*>, error: Class<*>): T

    fun taskId(taskId: Int): T

    fun timeOut(connectTimeOut: Long, readTimeOut: Long): T

    fun progressDialog(dialog: Dialog): T

    fun cache(isCache: Boolean): T

    fun connect()
}
