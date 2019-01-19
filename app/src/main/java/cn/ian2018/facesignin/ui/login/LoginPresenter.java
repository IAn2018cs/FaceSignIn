package cn.ian2018.facesignin.ui.login;

import android.content.Context;
import android.util.Log;

import cn.ian2018.facesignin.MyApplication;
import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.User;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import cn.ian2018.facesignin.utils.MD5Util;
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

    private String TAG = "LoginPresenter";

    public LoginPresenter() {
        mModel = new LoginModel();
        mContext = MyApplication.getContext();
    }


    @Override
    public void login(String account, String password) {
        boolean isLogin = true;
        if (account.equals("")) {
            isLogin = false;
            getView().accountEmpty();
        }
        if (password.equals("")) {
            isLogin = false;
            getView().passwordEmpty();
        }
        // 如果账号密码不为空，检查是否正确
        if (isLogin) {
            // 对密码md5加密
            String MD5Pass = MD5Util.strToMD5(password);
            getView().showProgressDialog(mContext.getResources().getString(R.string.landing));
            mModel.login(account,MD5Pass).subscribe(new Subscriber<User>() {
                @Override
                public void onCompleted() {
                }
                @Override
                public void onError(Throwable e) {
                    getView().loginFail();
                    Log.e(TAG, "onError: " + e.getMessage());
                }

                @Override
                public void onNext(User user) {
                    Log.e(TAG, "onNext: " );
                }
            });
        }
    }
}
