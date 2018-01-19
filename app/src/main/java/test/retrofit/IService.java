package test.retrofit;


import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by clickapps on 31/8/17.
 */

public interface IService {

    @GET(MainActivityModel.ENDPOINT_GET)
    Observable<Response<Object>> getPost(@Path("id") int id);


    @GET("{offers}")
    Observable<String> get(@Path(value = "offers", encoded = true) String pathSegment, @QueryMap Map<String, Object> map);

    @POST("{add}")
    Observable<String> post(@Path(value = "add", encoded = true) String pathSegment, @Body Map<String, Object> map);
}

