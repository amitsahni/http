package webconnect.com.webconnect.di

import android.app.Dialog
import webconnect.com.webconnect.QueryMap
import webconnect.com.webconnect.listener.AnalyticsListener

/**
 * Created by amit on 23/9/17.
 */

interface IProperties<T> {

    fun baseUrl(url: String): T

    fun headerParam(headerParam: Map<String, String>): T

    fun queryParam(queryParam: QueryMap<String, String>): T

    fun analyticsListener(callback: AnalyticsListener): T

    fun taskId(taskId: Int): T

    fun timeOut(connectTimeOut: Long, readTimeOut: Long): T

    fun cache(isCache: Boolean): T

    fun connect()
}
