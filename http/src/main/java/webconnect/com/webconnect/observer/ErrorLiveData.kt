package webconnect.com.webconnect.observer


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

object ErrorLiveData {

    internal val error = MutableLiveData<String>()

    @JvmStatic
    fun getErrorLiveData(): LiveData<String> {
        return error
    }
}
