package cn.ian2018.facesignin.ui.userhome.pager.quantify;

import cn.ian2018.facesignin.bean.HistorySignInfo;
import cn.ian2018.facesignin.network.retrofit.RetrofitClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/3/3
 */
public class QuantifyModel implements QuantifyContract.QuantifyModel {

    @Override
    public Observable<HistorySignInfo> getHistorySignInfo(String account) {
        return RetrofitClient.getServiceApi()
                .getHistorySignInfo(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
