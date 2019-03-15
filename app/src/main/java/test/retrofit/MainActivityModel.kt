package test.retrofit

import android.app.Application
import android.arch.lifecycle.*
import android.content.Context
import android.os.Environment
import android.util.Log
import webconnect.com.webconnect.WebConnect
import webconnect.com.webconnect.getHTTPError
import java.io.File
import java.util.*
import kotlin.collections.HashMap

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
        WebConnect.with(ENDPOINT_GET)
                .get()
                .queryParam(headerMap)
                .headerParam(headerMap)
                .timeOut(100L, 50L)
                .loader {
                    Log.i(javaClass.simpleName, "Loader showing = $this")
                }
                .success(ResponseModel::class.java) {
                    Log.d(javaClass.simpleName, this.toString())
                    get.postValue(this)
                }
                .error(Error::class.java) {
                    error.postValue(this)
                }
                .failure { model, msg ->
                    activity.getHTTPError(model)
                }
                .connect()
    }

    fun post(): Map<String, String> {
        val requestMap = LinkedHashMap<String, String>()
        requestMap["locale"] = "en"
        requestMap["name"] = "manager1"
        requestMap["birth_date"] = "18/08/1987"
        requestMap["gender"] = "male"
        val headerMap = LinkedHashMap<String, String>()
        headerMap["Authorization"] = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MTQsIm5hbWUiOiJHdXJ1IiwiZW1haWwiOiJndXJwcmVldDJAY2xpY2thcHBzLmNvIiwibW9iaWxlIjoiODI4NzYyMTIyOCIsImltYWdlIjoiL2RlZmF1bHRfbG9nby5qcGciLCJpYXQiOjE1MjExODAwOTIsImV4cCI6MTUyMzc3MjA5Mn0.Cc4dOzVC3NipXfVOJdRE29-GrtO5H0dgC3GSABiTYTA"
        WebConnect.with(ENDPOINT_POST)
                .post()
                .bodyParam(requestMap)
                .formDataParam(requestMap)
                .timeOut(100L, 50L)
                .headerParam(headerMap)
                .progressListener { time, b, bi ->
                    Log.d(javaClass.simpleName, "Time = $time, b = $b , b1 = $bi")
                }
                .response {

                }
                .connect()
        return requestMap
    }

    fun put() {
        val requestMap = LinkedHashMap<String, String>()
        requestMap["locale"] = "Amit Singh"
        requestMap["name"] = "manager"
        requestMap["birth_date"] = "18/08/1987"
        requestMap["gender"] = "male"
        WebConnect.with(ENDPOINT_PUT)
                .put()
                .formDataParam(requestMap)
                .connect()
    }

    fun delete() {
        val requestMap = LinkedHashMap<String, String>()
        requestMap["name"] = "Amit Singh"
        requestMap["job"] = "manager"
        WebConnect.with(ENDPOINT_GET)
                .download(File("/test"))
                .get()
                .connect()
    }

    fun upload() {
        val map = HashMap<String, Any>()
        val temp = HashMap<String, String>()
        temp["code_country"] = "00966"
        temp["mobile"] = "566566563"
        map["mobile_number_attributes"] = temp
        map["name"] = "Hello"
        val header = HashMap<String, String>()
        val image = HashMap<String, File>()
        image["avatar"] = File(Environment.getExternalStorageDirectory(), "temp2.jpg")
        header["Authorization"] = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6NywibmFtZSI6IkFiYyIsImVtYWlsIjoiYWJjQGdtYWlsLmNvbSIsImlhdCI6MTU1MjY0ODM2NCwiZXhwIjoxNTU1MjQwMzY0fQ.0Kb8YjJdCcGGNAJLWJoktXpInoXDjEuUneGx2opMqfI"
        WebConnect.with("v1/customer_profiles")
                .multipart()
                .put()
                .baseUrl("http://api.iw.dev.clicksandbox.com/")
                .headerParam(header)
                .multipartBodyParam(map)
                //.multipartParamFile(image, getApplication())
                .response {
                    Log.i(javaClass.simpleName, this)
                }
                .connect()
    }


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
