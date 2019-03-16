package cn.ian2018.facesignin.ui.userhome;

import android.content.Context;

import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.bean.Saying;
import cn.ian2018.facesignin.ui.base.BaseView;
import rx.Observable;

/**
 * Created by 陈帅 on 2019/1/17/017.
 */
public class UserMainContract {
    interface IUserMainView extends BaseView {

    }

    interface IUserMainModel {
        Observable<Saying> getSaying();
    }

    interface IUserMainPresenter {
        void initSaying();
        void checkUnSignOutActive(Context context);

    }
}
