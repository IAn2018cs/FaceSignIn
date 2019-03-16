package cn.ian2018.facesignin.ui.login;

import cn.ian2018.facesignin.bean.User;
import cn.ian2018.facesignin.ui.base.BaseView;
import rx.Observable;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/12
 */
public class LoginContract {
    interface ILoginView extends BaseView {
        void accountEmpty();
        void passwordEmpty();
        void loginSuccess(int type);
        void loginFail();
    }

    interface ILoginPresenter {
        void login(String account, String password);
    }

    interface ILoginModel {
        Observable<User> login(String account, String password);
    }
}
