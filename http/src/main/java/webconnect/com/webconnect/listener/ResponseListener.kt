package webconnect.com.webconnect.listener;

@FunctionalInterface
interface ResponseListener {
    fun response(string: String)
}
