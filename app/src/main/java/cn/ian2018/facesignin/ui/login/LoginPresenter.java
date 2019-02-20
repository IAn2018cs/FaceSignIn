package cn.ian2018.facesignin.ui.login;

import android.content.Context;
import android.util.Log;

import cn.ian2018.facesignin.MyApplication;
import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.User;
import cn.ian2018.facesignin.data.Constant;
import cn.ian2018.facesignin.data.SpUtil;
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
    public void login(final String account, final String password) {
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
            getView().showProgressDialog(R.string.landing);
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
                    // 记住密码
                    SpUtil.putString(Constant.ACCOUNT,account);
                    SpUtil.putString(Constant.PASS_WORD,password);
                    SpUtil.putBoolean(Constant.IS_REMBER_PWD,true);
                    // 保存用户信息
                    SpUtil.putInt(Constant.USER_TYPE, user.getData().getType());
                    SpUtil.putString(Constant.USER_NAME, user.getData().getName());
                    SpUtil.putInt(Constant.USER_GRADE, user.getData().getGradeCode());
                    SpUtil.putString(Constant.USER_CLASS, user.getData().getClassDescription());
                    SpUtil.putInt(Constant.USER_GROUP, user.getData().getGroup());
                    SpUtil.putString(Constant.USER_PHONE, user.getData().getPhone());
                    SpUtil.putInt(Constant.USER_INTERESTGROUP, user.getData().getInterestGroupCode());
                    String imageUrl = user.getData().getNewImage();
                    if (imageUrl.contains("http://")) {
                        SpUtil.putString(Constant.USER_IMAGE, imageUrl);
                    } else {
                        if (!imageUrl.equals("null")) {
                            SpUtil.putString(Constant.USER_IMAGE, "http://123.206.57.216:8080/StudentImage/" + imageUrl);
                        } else {
                            imageUrl = user.getData().getOldImage();
                            SpUtil.putString(Constant.USER_IMAGE, "http://123.206.57.216:8080/OldImage/" + imageUrl);
                        }
                    }

                    getView().loginSuccess(user.getData().getType());
                    Log.e(TAG, "onNext:  登录成功");
                }
            });
        }
    }
}
