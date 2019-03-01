package webconnect.com.webconnect

import android.os.Handler
import android.os.Looper

fun runOnUiThread(f: () -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    handler.post {
        f()
    }
}