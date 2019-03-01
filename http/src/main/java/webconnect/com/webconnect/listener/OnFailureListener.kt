package webconnect.com.webconnect.listener

import java.lang.Exception

interface OnFailureListener {

    fun onFailure(model: Exception, msg: String)
}