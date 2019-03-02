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
    internal var context: Context? = null
    internal var url: String = ""
    internal var baseUrl: String = ""
    internal var httpType = HttpType.GET
    internal var requestParam: Map<String, Any> = LinkedHashMap()
    internal var queryParam: QueryMap<String, String> = QueryMap()
    internal var query: Map<String, String> = LinkedHashMap()
    internal var multipartParam: Map<String, String> = LinkedHashMap()
    internal var multipartParamFile: Map<String, File> = LinkedHashMap()
    internal var multipartParamListFile: Map<String, List<File>> = LinkedHashMap()
    internal var headerParam: Map<String, String> = LinkedHashMap()
    internal var success: OnSuccessListener<Any>? = null
    internal var err: OnErrorListener<Any>? = null
    internal var failure: OnFailureListener? = null
    internal var responseListener: ResponseListener? = null
    internal var progressListener: ProgressListener? = null
    internal var analyticsListener: AnalyticsListener? = null
    internal var loaderListener: LoaderListener? = null
    internal var dialog: Dialog? = null
    internal var model: Class<*> = Any::class.java
    internal var error: Class<*> = Any::class.java
    internal var taskId: Int = 0
    internal var connectTimeOut: Long = 0L
    internal var readTimeOut: Long = 0L
    internal var isCacheEnabled = false
    internal var debug = false
    internal var isJson = false
    internal var file: File? = null
    internal var requestBodyContentlength: Long = -1L

    internal enum class HttpType {
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
