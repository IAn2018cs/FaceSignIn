package cn.ian2018.facesignin.network.retrofit;

import cn.ian2018.facesignin.bean.AppVersion;
import cn.ian2018.facesignin.bean.User;
import cn.ian2018.facesignin.network.URLs;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 请求后台访问数据的 接口类
 */
public interface ServiceApi {
    // 接口涉及到解耦，userLogin 方法是没有任何实现代码的
    // 如果有一天要换 GoogleHttp

    @POST(URLs.LOGIN)// 登录接口 GET(相对路径)
    @FormUrlEncoded
    Observable<Result<User>> login(
            // @Query(后台需要解析的字段)
            @Field("Account") String account, @Field("Password") String password);

    @GET(URLs.UPDATE)// 检测更新
    Observable<Result<AppVersion>> checkVersion();
}
