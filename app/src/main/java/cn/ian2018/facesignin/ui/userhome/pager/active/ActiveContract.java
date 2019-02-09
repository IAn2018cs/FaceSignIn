package cn.ian2018.facesignin.ui.userhome.pager.active;

import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.ui.base.BaseView;
import rx.Observable;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/6
 */
public class ActiveContract {

    interface ActiveView extends BaseView {
    }

    interface ActiveModel {
        Observable<Active> getActive(String sensoroId);
    }

    interface ActivePresenter {
        void getActiveForNetwork(String sensoroId, boolean isScan);
    }
}
