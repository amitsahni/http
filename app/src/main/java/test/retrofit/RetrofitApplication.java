package test.retrofit;

import android.app.Application;
import android.util.Log;

import webconnect.com.webconnect.ApiConfiguration;
import webconnect.com.webconnect.observer.ErrorLiveData;
import webconnect.com.webconnect.observer.FailureLiveData;
import webconnect.com.webconnect.observer.SuccessLiveData;

/**
 * Created by clickapps on 31/8/17.
 */

public class RetrofitApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ApiConfiguration
                .baseUrl(MainActivityModel.Companion.getENDPOINT_BASE())
                .timeOut(1000L, 2000L)
                .logging(true)
                .config();

        SuccessLiveData.getSuccessLiveData().observeForever(s -> {
            Log.i(RetrofitApplication.class.getSimpleName(), "Success = " + s);
        });

        FailureLiveData.getFailureLiveData().observeForever(s -> {
            Log.i(RetrofitApplication.class.getSimpleName(), "Failure = " + s);
        });

        ErrorLiveData.getErrorLiveData().observeForever(s -> {
            Log.i(RetrofitApplication.class.getSimpleName(), "Error = ");
        });
    }
}
