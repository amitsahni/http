package webconnect.com.webconnect.listener

interface OnSuccessListener<T : Any> {
    fun onSuccess(model: T)
}