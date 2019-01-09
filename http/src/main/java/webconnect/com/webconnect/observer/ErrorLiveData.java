package webconnect.com.webconnect.observer;


import android.arch.lifecycle.MutableLiveData;

public class ErrorLiveData extends MutableLiveData<String> {

    private static volatile ErrorLiveData sInstance;

    private ErrorLiveData() {

    }

    public static ErrorLiveData getInstance() {
        if (sInstance == null) {
            synchronized (ErrorLiveData.class) {
                if (sInstance == null) {
                    sInstance = new ErrorLiveData();
                }
            }
        }
        return sInstance;
    }
}
