package cn.ian2018.facesignin.network.retrofit;

import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.bean.AppVersion;
import cn.ian2018.facesignin.bean.AuthTokenResult;
import cn.ian2018.facesignin.bean.SimpleResult;
import cn.ian2018.facesignin.bean.HistorySignInfo;
import cn.ian2018.facesignin.bean.RepairUserInfo;
import cn.ian2018.facesignin.bean.Saying;
import cn.ian2018.facesignin.bean.SignInResult;
import cn.ian2018.facesignin.bean.SignOutResult;
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

    // 登录
    @POST(URLs.LOGIN)
    @FormUrlEncoded
    Observable<User> login(
            // @Query(后台需要解析的字段)
            @Field("Account") String account, @Field("Password") String password);

    // 检测更新
    @GET(URLs.UPDATE)
    Observable<AppVersion> checkVersion();

    // 获取名言
    @GET(URLs.GET_SAYING)
    Observable<Saying> getSaying();

    // 获取附近的活动
    @POST(URLs.GET_ACTIVE_BY_YUNZI_NO_DUTY)
    @FormUrlEncoded
    Observable<Active> getActive(@Field("sensoroId") String sensoroId);

    // 签到
    @POST(URLs.SIGN_IN)
    @FormUrlEncoded
    Observable<SignInResult> signInResult(
            @Field("account") String account, @Field("activityid") int activityId,
            @Field("intime") String inTime, @Field("outtime") String outTime,
            @Field("InLocation") String inLocation);

    // 签离
    @POST(URLs.SIGN_OUT)
    @FormUrlEncoded
    Observable<SignOutResult> signOutResult(
            @Field("nid") int id, @Field("outtime") String outTime,
            @Field("OutLocation") String location);

    // 获取历史签到
    @POST(URLs.GET_SING)
    @FormUrlEncoded
    Observable<HistorySignInfo> getHistorySignInfo(@Field("userId") String account);

    @GET(URLs.GET_REPAIR_USER)
    Observable<RepairUserInfo> getRepairInfo();

    // 上传反馈信息
    @POST(URLs.FEED_BACK)
    @FormUrlEncoded
    Observable<SimpleResult> uploadFeedbackInfo(
            @Field("account") String account, @Field("msg") String msg,
            @Field("PhoneBrand") String phoneBrand, @Field("PhoneBrandType") String phoneBrandType,
            @Field("AndroidVersion") String androidVersion, @Field("Anonymous") int anonymous);

    // 获取七牛云上传token
    @POST(URLs.GET_TOKEN)
    @FormUrlEncoded
    Observable<AuthTokenResult> getToken(@Field("Bucket") String bucket);

    // 上传修改的个人信息
    @POST(URLs.CHANGE_INFO)
    @FormUrlEncoded
    Observable<SimpleResult> uploadChangeInfo(
            @Field("Account") String account, @Field("Name") String name,
            @Field("GradeCode") int grade, @Field("ClassDescription") String classDes,
            @Field("Group") int group, @Field("Phone") String phone,
            @Field("ImageUrl") String image);
}
