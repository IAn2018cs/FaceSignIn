package cn.ian2018.facesignin.ui.userhome;

import android.content.Context;
import android.content.Intent;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.ui.base.BaseActivity;

/**
 * 普通用户主页
 * Created by 陈帅 on 2019/1/17/017.
 */
public class UserMainActivity extends BaseActivity<UserMainPresenter> implements UserMainContract.UserMainView {

    public static void start(Context context) {
        Intent starter = new Intent(context, UserMainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_main);
    }

    @Override
    protected UserMainPresenter createPresenter() {
        return new UserMainPresenter();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }


}
