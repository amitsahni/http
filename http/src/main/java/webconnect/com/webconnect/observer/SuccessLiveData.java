package webconnect.com.webconnect.observer;


import android.arch.lifecycle.MutableLiveData;

public class SuccessLiveData extends MutableLiveData<String> {

    private static SuccessLiveData sInstance;

    private SuccessLiveData() {

    }

    public static SuccessLiveData getInstance() {
        if (sInstance == null) {
            synchronized (SuccessLiveData.class) {
                if (sInstance == null) {
                    sInstance = new SuccessLiveData();
                }
            }
        }
        return sInstance;
    }
}
