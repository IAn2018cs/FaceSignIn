package cn.ian2018.facesignin.ui.activemove;

import cn.ian2018.facesignin.bean.SignOutResult;
import cn.ian2018.facesignin.network.retrofit.RetrofitClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/21
 */
public class ActiveMoveModel implements ActiveMoveContract.ActiveMoveModel {
    @Override
    public Observable<SignOutResult> signOutResult(int activeId, String outTime, String location) {
        return RetrofitClient.getServiceApi()
                .signOutResult(activeId,outTime,location)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
