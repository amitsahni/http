package test.retrofit;

import android.app.Application;

import webconnect.com.webconnect.ApiConfiguration;

/**
 * Created by clickapps on 31/8/17.
 */

public class RetrofitApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        new ApiConfiguration.Builder(this)
                .baseUrl(MainActivityModel.ENDPOINT_BASE)
                .timeOut(1000L, 2000L)
                .debug(true)
                .config();
    }
}
