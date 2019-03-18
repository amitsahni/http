package webconnect.com.webconnect.listener

@FunctionalInterface
interface OnSuccessListener<T : Any> {
    fun onSuccess(model: T)
}