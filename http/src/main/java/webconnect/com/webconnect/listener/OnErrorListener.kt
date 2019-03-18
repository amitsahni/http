package webconnect.com.webconnect.listener

@FunctionalInterface
interface OnErrorListener<T> {
    fun onError(model: T)
}