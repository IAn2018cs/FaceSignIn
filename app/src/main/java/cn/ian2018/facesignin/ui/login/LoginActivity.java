package cn.ian2018.facesignin.ui.login;

import android.content.Context;
import android.content.Intent;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.ui.base.BaseActivity;
import cn.ian2018.facesignin.utils.CheckVersionHelper;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/12
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.LoginView {

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void initData() {
        CheckVersionHelper checkVersionHelper = new CheckVersionHelper(this);
        checkVersionHelper.checkVersionCode();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_login);
    }
}
