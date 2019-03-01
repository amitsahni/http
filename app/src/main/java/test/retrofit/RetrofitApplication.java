package test.retrofit;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
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
        new ApiConfiguration.Builder(this)
                .baseUrl(MainActivityModel.Companion.getENDPOINT_BASE())
                .timeOut(1000L, 2000L)
                .debug(false)
                .config();

        ErrorLiveData.getInstance().observeForever(s -> {
            Log.i(RetrofitApplication.class.getSimpleName(), "Error = " + s);
        });

        SuccessLiveData.getInstance().observeForever(s -> {
            Log.i(RetrofitApplication.class.getSimpleName(), "Success = " + s);
        });

        FailureLiveData.getInstance().observeForever(s -> {
            Log.i(RetrofitApplication.class.getSimpleName(), "Failure = " + s);
        });
    }
}
