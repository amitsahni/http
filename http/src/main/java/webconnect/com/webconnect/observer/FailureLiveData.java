package webconnect.com.webconnect.observer;


import android.arch.lifecycle.MutableLiveData;

public class FailureLiveData extends MutableLiveData<String> {

    private static FailureLiveData sInstance;

    private FailureLiveData() {

    }

    public static FailureLiveData getInstance() {
        if (sInstance == null) {
            synchronized (FailureLiveData.class) {
                if (sInstance == null) {
                    sInstance = new FailureLiveData();
                }
            }
        }
        return sInstance;
    }
}
