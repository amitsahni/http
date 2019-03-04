package webconnect.com.webconnect

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import webconnect.com.webconnect.model.ErrorModel
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.cert.CertificateException
import java.util.concurrent.TimeoutException

fun runOnUiThread(f: () -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    handler.post {
        f()
    }
}

object G {
    val gson = GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .setLenient()
            .create()
}

fun gson(): Gson {
    return G.gson
}

fun Any.toJson(): String {
    return try {
        gson().toJson(this)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

inline fun <reified T> String.fromJson() {
    try {
        gson().fromJson<T>(this, T::class.java)
    } catch (e: JsonSyntaxException) {
        e.printStackTrace()
    }
}

fun String.fromJson(model: Class<*>): Any {
    return try {
        gson().fromJson(this, model)
    } catch (e: JsonSyntaxException) {
        e.printStackTrace()
        Any()
    }
}

fun String.formatJson(): String {
    return try {
        gson().fromJson(this, Any::class.java).toString()
    } catch (e: JsonSyntaxException) {
        e.printStackTrace()
        ""
    }
}

fun Map<String, Any>.convertFormData(): String {
    val sb = StringBuilder()
    for ((key, value) in this) {
        if (!sb.isEmpty()) {
            sb.append("&")
        }
        sb.append(String.format("%s=%s", key, value.toString()))
    }
    return sb.toString()
}

fun Context.getHTTPError(t: Throwable): String {
    return if (t.javaClass.name.contains(UnknownHostException::class.java.name)) {
        getString(R.string.error_internet_connection).toString()
    } else if (t.javaClass.name.contains(TimeoutException::class.java.name)
            || t.javaClass.name.contains(SocketTimeoutException::class.java.name)
            || t.javaClass.name.contains(ConnectException::class.java.name)) {
        getString(R.string.error_server_connection).toString()
    } else if (t.javaClass.name.contains(CertificateException::class.java.name)) {
        getString(R.string.error_certificate_exception).toString()
    } else {
        t.toString()
    }
}