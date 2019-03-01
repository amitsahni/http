package webconnect.com.webconnect.observer;

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData


object FailureLiveData {

        val failure = MutableLiveData<String>()

        fun getFailureLiveData(): LiveData<String> {
                return failure
        }
}

