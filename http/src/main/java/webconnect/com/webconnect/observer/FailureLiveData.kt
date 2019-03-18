package webconnect.com.webconnect.observer;

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData


object FailureLiveData {
        internal val failure = MutableLiveData<String>()

        @JvmStatic
        fun getFailureLiveData(): LiveData<String> {
                return failure
        }
}

