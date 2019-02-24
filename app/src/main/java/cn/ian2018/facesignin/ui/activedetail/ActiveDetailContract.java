package cn.ian2018.facesignin.ui.activedetail;

import android.content.Context;

import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.bean.SignInResult;
import cn.ian2018.facesignin.ui.base.BaseView;
import rx.Observable;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2019/2/15
 */
public class ActiveDetailContract {
    interface ActiveDetailView extends BaseView {
        boolean isCanSign();
        void goSinOutActivity();
    }

    interface ActiveDetailPresenter {
        void signIn(Active.DataBean active);
        void initLocation(Context context);
    }

    interface ActiveDetailModel {
        Observable<SignInResult> signInResult(String account, int activeId, String inTime,
                                              String outTime, String location);
    }
}
