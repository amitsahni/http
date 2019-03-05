package webconnect.com.webconnect.listener

@FunctionalInterface
interface OnFailureListener {
    fun onFailure(model: Exception, msg: String)
}