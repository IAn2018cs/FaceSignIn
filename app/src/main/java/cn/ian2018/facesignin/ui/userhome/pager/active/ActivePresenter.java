package cn.ian2018.facesignin.ui.userhome.pager.active;

import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import cn.ian2018.facesignin.utils.Logs;
import rx.Subscriber;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/6
 */
public class ActivePresenter extends BasePresenter<ActiveContract.ActiveView> implements ActiveContract.ActivePresenter {

    ActiveContract.ActiveModel mActiveModel;

    public ActivePresenter() {
        mActiveModel = new ActiveModel();
    }


    @Override
    public void getActiveForNetwork(String sensoroId, boolean isScan) {
        mActiveModel.getActive(sensoroId).subscribe(new Subscriber<Active>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Logs.e("getActiveForNetwork  :" + e.toString());
            }

            @Override
            public void onNext(Active active) {

            }
        });
    }
}
