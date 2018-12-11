package cn.ian2018.facesignin.network.retrofit;

import android.util.Log;

import cn.ian2018.facesignin.network.URLs;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Emitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RetrofitClient {
    private final static ServiceApi mServiceApi;

    static {
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.e("TAG",message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                // 访问后台接口的主路径
                .baseUrl(URLs.HOST_ADDRESS)
                // 添加解析转换工厂,Gson 解析，Xml解析，等等
                .addConverterFactory(GsonConverterFactory.create())
                // 添加 OkHttpClient,不添加默认就是 光杆 OkHttpClient
                .client(okHttpClient)
                // 支持RxJava Call -> Obsevrable 怎么做到的？ 1 2  采用了 Adapter 设计模式
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        // 创建一个 实例对象
        mServiceApi = retrofit.create(ServiceApi.class);
    }

    public static ServiceApi getServiceApi() {
        return mServiceApi;
    }

    public static <T> Observable.Transformer<Result<T>, T> transformer() {
        return new Observable.Transformer<Result<T>, T>() {
            @Override
            public Observable<T> call(Observable<Result<T>> resultObservable) {
                return resultObservable.flatMap(new Func1<Result<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(Result<T> tResult) {
                        // 解析不同的情况返回
                        if(tResult.isOk()){
                            // 返回成功
                            return createObservable(tResult.data);
                        }else {
                            // 返回失败
                            return Observable.error(new ErrorHandle.ServiceError("",tResult.getMsg()));
                        }
                    }
                }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private static <T> Observable<T> createObservable(final T data) {
        return Observable.create(new Action1<Emitter<T>>() {
            @Override
            public void call(Emitter<T> tEmitter) {
                tEmitter.onNext(data);
                tEmitter.onCompleted();
            }
        }, Emitter.BackpressureMode.NONE);
    }
}
