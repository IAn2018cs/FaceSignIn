package cn.ian2018.facesignin.ui.userhome;

import cn.ian2018.facesignin.bean.Saying;
import cn.ian2018.facesignin.ui.base.BaseView;
import rx.Observable;

/**
 * Created by 陈帅 on 2019/1/17/017.
 */
public class UserMainContract {
    interface UserMainView extends BaseView {

    }

    interface UserMainModel {
        Observable<Saying> getSaying();
    }

    interface UserMainPresenter {
        void initSaying();
    }
}
