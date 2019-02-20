package cn.ian2018.facesignin.ui.activedetail;

import cn.ian2018.facesignin.bean.SignInResult;
import cn.ian2018.facesignin.network.retrofit.RetrofitClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2019/2/15
 */
public class ActiveDetailModel implements ActiveDetailContract.ActiveDetailModel {

    @Override
    public Observable<SignInResult> signInResult(String account, int activeId, String inTime, String outTime, String location) {
        return RetrofitClient.getServiceApi()
                .signInResult(account,activeId,inTime,outTime,location)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
