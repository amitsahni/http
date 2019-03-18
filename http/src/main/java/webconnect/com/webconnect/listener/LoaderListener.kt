package webconnect.com.webconnect.listener

@FunctionalInterface
interface LoaderListener {
    fun loader(isShowing : Boolean)
}