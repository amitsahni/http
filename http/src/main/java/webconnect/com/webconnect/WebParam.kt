package webconnect.com.webconnect

import android.app.Dialog
import android.content.Context
import webconnect.com.webconnect.listener.*
import webconnect.com.webconnect.model.ErrorModel
import webconnect.com.webconnect.model.SuccessModel
import java.io.File
import java.io.Serializable
import java.util.*


/**
 * The type Web param.
 */
class WebParam : Serializable {
    var context: Context? = null
    var url: String? = null
    var baseUrl: String? = null
    var httpType = HttpType.GET
    var requestParam: Map<String, Any> = LinkedHashMap()
    var queryParam: QueryMap<String, String> = QueryMap()
    var multipartParam: Map<String, String> = LinkedHashMap()
    var multipartParamFile: Map<String, File> = LinkedHashMap()
    var multipartParamListFile: Map<String, List<File>> = LinkedHashMap()
    var headerParam: Map<String, String> = LinkedHashMap()
    var callback: OnWebCallback? = null
    var success: OnSuccessListener<Any>? = null
    var err: OnErrorListener<Any>? = null
    var failure: OnFailureListener? = null
    var responseListener: ResponseListener? = null
    var progressListener: ProgressListener? = null
    var analyticsListener: AnalyticsListener? = null
    var loaderListener: LoaderListener? = null
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
