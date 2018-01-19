package webconnect.com.webconnect;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.OPTIONS;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by amit on 10/9/17.
 */

public interface IAPIService {
    String sEND_POINT = "{path_segment}";

    @GET(sEND_POINT)
    Observable<Response<Object>> get(@Path(value = "path_segment", encoded = true) String pathSegment, @QueryMap Map<String, Object> map);

    @HEAD(sEND_POINT)
    Observable<Response<Object>> head(@Path(value = "path_segment", encoded = true) String pathSegment, @QueryMap Map<String, Object> map);

    @OPTIONS(sEND_POINT)
    Observable<Response<Object>> options(@Path(value = "path_segment", encoded = true) String pathSegment, @QueryMap Map<String, Object> map);

    @POST(sEND_POINT)
    Observable<Response<Object>> post(@Path(value = "path_segment", encoded = true) String pathSegment, @Body Map<String, Object> map);

    @HTTP(method = "DELETE", path = sEND_POINT, hasBody = true)
    Observable<Response<Object>> delete(@Path(value = "path_segment", encoded = true) String pathSegment, @Body Map<String, Object> map);

    @PUT(sEND_POINT)
    Observable<Response<Object>> put(@Path(value = "path_segment", encoded = true) String pathSegment, @Body Map<String, Object> map);

    @PATCH(sEND_POINT)
    Observable<Response<Object>> patch(@Path(value = "path_segment", encoded = true) String pathSegment, @Body Map<String, Object> map);

    @Multipart
    @POST(sEND_POINT)
    Observable<Response<Object>> multipart(@Path(value = "path_segment", encoded = true) String pathSegment, @PartMap Map<String, RequestBody> map);

}
