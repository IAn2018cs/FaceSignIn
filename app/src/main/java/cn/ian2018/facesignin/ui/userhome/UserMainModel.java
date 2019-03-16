package cn.ian2018.facesignin.ui.userhome;

import cn.ian2018.facesignin.bean.Saying;
import cn.ian2018.facesignin.network.retrofit.RetrofitClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 陈帅 on 2019/1/17/017.
 */
public class UserMainModel implements UserMainContract.IUserMainModel {

    @Override
    public Observable<Saying> getSaying() {
        return RetrofitClient.getServiceApi()
                .getSaying()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
