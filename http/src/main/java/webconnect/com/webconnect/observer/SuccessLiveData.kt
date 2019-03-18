package webconnect.com.webconnect.observer;


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData;

object SuccessLiveData {

    internal val success = MutableLiveData<String>()

    @JvmStatic
    fun getSuccessLiveData(): LiveData<String> {
        return success
    }
}