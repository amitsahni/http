package webconnect.com.webconnect

import android.app.Dialog
import android.content.Context

import java.io.File
import java.io.Serializable
import java.util.LinkedHashMap

import webconnect.com.webconnect.listener.AnalyticsListener
import webconnect.com.webconnect.listener.OnWebCallback
import webconnect.com.webconnect.listener.ProgressListener


/**
 * The type Web param.
 */
class WebParam : Serializable {
    var context: Context? = null
    var url: String? = null
    var baseUrl: String? = null
    var httpType = HttpType.GET
    var requestParam: Map<String, Any> = LinkedHashMap<String, Any>()
    var queryParam: Map<String, String> = LinkedHashMap()
    var multipartParam: Map<String, String> = LinkedHashMap()
    var multipartParamFile: Map<String, File> = LinkedHashMap()
    var headerParam: Map<String, String> = LinkedHashMap()
    var callback: OnWebCallback? = null
    var progressListener: ProgressListener? = null
    var analyticsListener: AnalyticsListener? = null
    var dialog: Dialog? = null
    var model: Class<*> = Any::class.java
    var error: Class<*> = Any::class.java
    var taskId: Int = 0
    var connectTimeOut: Long = 0L
    var readTimeOut: Long = 0L
    var isCacheEnabled = false
    var debug = false
    var isJson = false
    var file: File? = null
    var requestBodyContentlength: Long = -1L

    enum class HttpType {
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
