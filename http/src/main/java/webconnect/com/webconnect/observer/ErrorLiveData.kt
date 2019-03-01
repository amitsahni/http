package webconnect.com.webconnect.observer


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

object ErrorLiveData {

    val error = MutableLiveData<String>()

    fun getErrorLiveData(): LiveData<String> {
        return error
    }
}
