package cn.ian2018.facesignin.ui.login;

import android.content.Context;

import cn.ian2018.facesignin.MyApplication;
import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.User;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import cn.ian2018.facesignin.utils.ToastUtil;
import rx.Subscriber;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/12
 */
public class LoginPresenter extends BasePresenter<LoginContract.LoginView> implements LoginContract.LoginPresenter {

    private LoginContract.LoginModel mModel;

    private Context mContext;

    public LoginPresenter() {
        mModel = new LoginModel();
        mContext = MyApplication.getContext();
    }


    @Override
    public void login(String account, String password) {
        getView().showProgressDialog(mContext.getResources().getString(R.string.landing));
        mModel.login(account,password).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(User user) {

            }
        });
    }
}
