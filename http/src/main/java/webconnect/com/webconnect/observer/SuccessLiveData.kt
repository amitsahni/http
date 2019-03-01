package webconnect.com.webconnect.observer;


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData;

object SuccessLiveData {

    val success = MutableLiveData<String>()

    fun getSuccessLiveData(): LiveData<String> {
        return success
    }
}