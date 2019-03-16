package cn.ian2018.facesignin.ui.userhome.pager.mine;

import cn.ian2018.facesignin.bean.RepairUserInfo;
import cn.ian2018.facesignin.network.retrofit.RetrofitClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2019/3/16
 */
public class MineModel implements MineContract.IMineModel {

    @Override
    public Observable<RepairUserInfo> getRepairInfo() {
        return RetrofitClient.getServiceApi()
                .getRepairInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
