package cn.ian2018.facesignin.ui.login;

import cn.ian2018.facesignin.bean.User;
import cn.ian2018.facesignin.network.retrofit.BaseSubscriber;
import cn.ian2018.facesignin.ui.base.BasePresenter;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/12
 */
public class LoginPresenter extends BasePresenter<LoginContract.LoginView> implements LoginContract.LoginPresenter {

    private LoginContract.LoginModel mModel;

    public LoginPresenter() {
        mModel = new LoginModel();
    }


    @Override
    public void login(String account, String password) {
        mModel.login(account,password).subscribe(new BaseSubscriber<User>() {
            @Override
            public void onNext(User user) {

            }

            @Override
            protected void onError(String errorCode, String errorMessage) {

            }
        });
    }
}
