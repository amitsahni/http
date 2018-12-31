package webconnect.com.webconnect;

import android.net.TrafficStats;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by clickapps on 29/12/17.
 */
@SuppressWarnings("unchecked")
public class RxObservable {


    static final class SimpleANObservable<T> extends Observable<T> {

        private WebParam param;
        private final Call originalCall;

        SimpleANObservable(WebParam param, Call call) {
            this.param = param;
            this.originalCall = call;
        }

        @Override
        protected void subscribeActual(Observer<? super T> observer) {
            Call call = originalCall.clone();
            observer.onSubscribe(new ANDisposable(call));
            final long startTime = System.currentTimeMillis();
            final long startBytes = TrafficStats.getTotalRxBytes();
            try {
                Response okHttpResponse = call.execute();
                final long timeTaken = System.currentTimeMillis() - startTime;
                ObserverModel observerModel = new ObserverModel();
                T object;
                if (okHttpResponse.isSuccessful()) {
                    if (okHttpResponse.body() != null) {
                        if (param.getAnalyticsListener() != null) {
                            param.getAnalyticsListener().onReceived(timeTaken, param.getRequestBodyContentlength(),
                                    okHttpResponse.body().contentLength(), okHttpResponse.cacheResponse() != null);
                        }
                        object = (T) ApiConfiguration.getGson().fromJson(okHttpResponse.body().string(), param.getModel());
                        Log.d(getClass().getSimpleName(), ApiConfiguration.getGson().toJson(object));
                    } else {
                        object = (T) "";
                    }
                    Log.d(getClass().getSimpleName(), object.toString());
                    observerModel.setModel(object);
                    observerModel.setType(1);
                    observer.onNext((T) observerModel);
                } else {
                    if (okHttpResponse.body() != null) {
                        if (param.getAnalyticsListener() != null) {
                            param.getAnalyticsListener().onReceived(timeTaken, param.getRequestBodyContentlength(),
                                    okHttpResponse.body().contentLength(), okHttpResponse.cacheResponse() != null);
                        }
                        object = (T) ApiConfiguration.getGson().fromJson(okHttpResponse.body().string(), param.getError());
                        Log.e(getClass().getSimpleName(), ApiConfiguration.getGson().toJson(object));
                        observerModel.setModel(object);
                        observerModel.setType(2);
                        observer.onNext((T) observerModel);
                    } else {
                        observer.onError(new Throwable(""));
                    }

                }
            } catch (Exception e) {
                observer.onError(e);
            } finally {
                observer.onComplete();
            }
        }

    }

    static final class DownloadANObservable<T> extends Observable<T> {

        private WebParam param;
        private final Call originalCall;

        DownloadANObservable(WebParam param, Call call) {
            this.param = param;
            this.originalCall = call;
        }

        @Override
        protected void subscribeActual(Observer<? super T> observer) {
            Call call = originalCall.clone();
            observer.onSubscribe(new ANDisposable(call));
            final long startTime = System.currentTimeMillis();
            final long startBytes = TrafficStats.getTotalRxBytes();

            try {
                Response okHttpResponse = call.execute();
                final long timeTaken = System.currentTimeMillis() - startTime;
                T object = null;
                if (okHttpResponse.isSuccessful()) {
                    if (okHttpResponse.body() != null) {
                        if (param.getAnalyticsListener() != null) {
                            param.getAnalyticsListener().onReceived(timeTaken, param.getRequestBodyContentlength(),
                                    okHttpResponse.body().contentLength(), okHttpResponse.cacheResponse() != null);
                        }
                        ResponseBody body = okHttpResponse.body();
                        OutputStream out = null;
                        try {
                            out = new FileOutputStream(param.getFile());
                            IOUtils.copy(body.byteStream(), out);
                            object = (T) param.getFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            IOUtils.closeQuietly(out);
                        }
                        observer.onNext(object);
                    }
                } else {
                    if (okHttpResponse.body() != null) {
                        observer.onError(new Throwable(okHttpResponse.body().string()));
                    } else {
                        observer.onError(new Throwable(""));
                    }

                }
            } catch (Exception e) {
                observer.onError(e);
            } finally {
                observer.onComplete();
            }
        }
    }

    private static final class ANDisposable implements Disposable {

        private final Call call;

        private ANDisposable(Call call) {
            this.call = call;
        }

        @Override
        public void dispose() {
            this.call.cancel();
        }

        @Override
        public boolean isDisposed() {
            return this.call.isCanceled();
        }
    }
}
