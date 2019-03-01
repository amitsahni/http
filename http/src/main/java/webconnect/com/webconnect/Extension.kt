package webconnect.com.webconnect

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException

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
    }
}

fun Map<String, Any>.convertFormData(): String {
    val sb = StringBuilder()
    for ((key, value) in this) {
        if (!sb.isNotEmpty()) {
            sb.append("&")
        }
        sb.append(String.format("%s=%s", key, value.toString()))
    }
    return sb.toString()
}