package test.retrofit

import android.app.Application
import android.arch.lifecycle.*
import android.content.Context
import android.util.Log
import webconnect.com.webconnect.WebConnect
import java.io.File
import java.util.*

/**
 * Created by clickapps on 22/12/17.
 */

class MainActivityModel(application: Application) : AndroidViewModel(application) {
    //    public static final String ENDPOINT_BASE = "http://api.dev.moh.clicksandbox1.com:8080/v1/";
    private val activity: Context

    //    private MainActivityModel(Activity activity) {
    //        this.activity = activity;
    //    }

    private val get = MutableLiveData<Any>()
    private val post = MutableLiveData<Any>()
    private val put = MutableLiveData<Any>()
    private val delete = MutableLiveData<Any>()
    private val error = MutableLiveData<Any>()

    init {
        activity = application
    }

    fun getGet(): LiveData<Any> {
        return get
    }

    fun getPost(): LiveData<Any> {
        return post
    }

    fun getPut(): LiveData<Any> {
        return put
    }

    fun getDelete(): LiveData<Any> {
        return delete
    }

    fun getError(): LiveData<Any> {
        return error
    }
    //    D/OkHttp: --> GET http://api.qa.leasing.clicksandbox.com/v1/app/leases
    //            01-17 12:10:53.411 6765-24325/com.brickspms D/OkHttp: slug: default
    //01-17 12:10:53.411 6765-24325/com.brickspms D/OkHttp: Auth-Token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0ZW5hbnRfaWQiOjEwODMsImlhdCI6IjIwMTgtMDEtMTIgMDc6NTg6NTAgVVRDIn0.mXkySHf71fa3vdLwUWaIqoqd5nUR2Z3dJ1INq5t4Clo
    //01-17 12:10:53.412 6765-24325/com.brickspms D/OkHttp:

    fun get() {
        val headerMap = LinkedHashMap<String, String>()
        headerMap["Authorization"] = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6NDM3ODMsIm5hbWUiOiJjZmNnZyBHZ2dnIGNjY2MgY2NjY2MiLCJlbWFpbCI6ImFsbWFycmlAbW9oLmdvdi5zYSIsIm1vYmlsZSI6IjUzMDgwMzA5MSIsInJvbGUiOiJlbXBsb3llZSIsImFjY2VzcyI6Im1vYmlsZSIsImRvbWFpbiI6ImFsbCIsImlhdCI6MTU0NjUwMzQ2MSwiZXhwIjoxNTQ5MDk1NDYxfQ.4OhtjSj5b0u7h57t3_9DEBXgkYsqo6nVLJ5eemDYg2o"
        headerMap["Authorization"] = "12"
        WebConnect.with(ENDPOINT_GET)
                .get()
                .queryParam(headerMap)
                .headerParam(headerMap)
                .timeOut(100L, 50L)
                .loader {
                    Log.i(javaClass.simpleName, "Loader showing = $this")
                }
                .response {

                }
                .success(ResponseModel::class.java) {
                    get.postValue(this)
                }
                .error(Error::class.java) {
                    error.postValue(this)
                }
                .failure { model, msg -> }
                .connect()
    }

//    fun post(): Map<String, String> {
//        val requestMap = LinkedHashMap<String, String>()
//        requestMap["locale"] = "en"
//        requestMap["name"] = "manager1"
//        requestMap["birth_date"] = "18/08/1987"
//        requestMap["gender"] = "male"
//        val headerMap = LinkedHashMap<String, String>()
//        headerMap["Authorization"] = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MTQsIm5hbWUiOiJHdXJ1IiwiZW1haWwiOiJndXJwcmVldDJAY2xpY2thcHBzLmNvIiwibW9iaWxlIjoiODI4NzYyMTIyOCIsImltYWdlIjoiL2RlZmF1bHRfbG9nby5qcGciLCJpYXQiOjE1MjExODAwOTIsImV4cCI6MTUyMzc3MjA5Mn0.Cc4dOzVC3NipXfVOJdRE29-GrtO5H0dgC3GSABiTYTA"
//        WebConnect.with(this.activity, ENDPOINT_POST)
//                .put()
//                .multipart()
//                .multipartParam(requestMap)
//                .timeOut(100L, 50L)
//                .headerParam(headerMap)
//                .connect()
//        return requestMap
//    }

//    fun put() {
//        val requestMap = LinkedHashMap<String, String>()
//        requestMap["locale"] = "Amit Singh"
//        requestMap["name"] = "manager"
//        requestMap["birth_date"] = "18/08/1987"
//        requestMap["gender"] = "male"
//        WebConnect.with(activity, ENDPOINT_PUT)
//                .put()
//                .formDataParam(requestMap)
//                .connect()
//    }
//
//    fun delete() {
//        val requestMap = LinkedHashMap<String, String>()
//        requestMap["name"] = "Amit Singh"
//        requestMap["job"] = "manager"
//        WebConnect.with(activity, ENDPOINT_PUT)
//                .download(File("/test"))
//                .get()
//                .connect()
//    }


    class MainActivityModelFactory(private val activity: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainActivityModel(activity) as T
        }
    }

    companion object {
        val ENDPOINT_GET = "offers"
        val ENDPOINT_POST = "users"
        val ENDPOINT_PUT = "users/740"
        val ENDPOINT_BASE = "https://reqres.in/api/"
    }

}
