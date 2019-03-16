package cn.ian2018.facesignin.ui.userhome.pager.active;

import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.network.retrofit.RetrofitClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/6
 */
public class ActiveModel implements ActiveContract.IActiveModel {

    @Override
    public Observable<Active> getActive(String sensoroId) {
        return RetrofitClient.getServiceApi()
                .getActive(sensoroId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
