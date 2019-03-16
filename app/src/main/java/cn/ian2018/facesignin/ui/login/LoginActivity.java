package cn.ian2018.facesignin.ui.login;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.ui.base.BaseActivity;
import cn.ian2018.facesignin.ui.userhome.UserMainActivity;
import cn.ian2018.facesignin.ui.widget.CustomVideoView;
import cn.ian2018.facesignin.utils.CheckVersionHelper;

import cn.ian2018.facesignin.data.Constant;
import cn.ian2018.facesignin.data.SpUtil;
import cn.ian2018.facesignin.utils.ToastUtil;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/12
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.ILoginView, View.OnClickListener {

    private static final int USER_ORDINARY = 0; // 普通用户
    private static final int USER_ADMIN = 1;    // 管理员

    private EditText et_account;
    private EditText et_password;
    private TextInputLayout text_input_account;
    private TextInputLayout text_input_pass;
    private CustomVideoView videoView;

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
        // 检测更新
        CheckVersionHelper checkVersionHelper = new CheckVersionHelper(this);
        checkVersionHelper.checkVersionCode();

        // 检测是否登陆
        if (!SpUtil.getBoolean(Constant.IS_REMBER_PWD,false)) {
            et_account.setText(SpUtil.getString(Constant.ACCOUNT,""));
        } else {
            loginSuccess(SpUtil.getInt(Constant.USER_TYPE,USER_ORDINARY));
        }
    }

    @Override
    protected void initView() {
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        Button bt_login = findViewById(R.id.bt_login);
        TextView tv_sign_up = findViewById(R.id.tv_sign_up);
        TextView tv_forget = findViewById(R.id.tv_forget);

        text_input_account = findViewById(R.id.text_input_account);
        text_input_pass = findViewById(R.id.text_input_pass);

        et_account.addTextChangedListener(new MyTextWatcher(text_input_account));
        et_password.addTextChangedListener(new MyTextWatcher(text_input_pass));

        bt_login.setOnClickListener(this);
        tv_sign_up.setOnClickListener(this);
        tv_forget.setOnClickListener(this);

        initVideo();
    }

    private void initVideo() {
        //加载视频资源控件
        videoView = findViewById(R.id.videoview);
        //设置播放加载路径
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        //播放
        videoView.start();
        //循环播放
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
    }


    @Override
    protected void setContentView() {
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initVideo();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        videoView.stopPlayback();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放资源
        if (videoView != null) {
            videoView.suspend();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                String account = et_account.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                getPresenter().login(account,password);
                break;
            case R.id.tv_sign_up:
                break;
            case R.id.tv_forget:
                break;
        }
    }

    @Override
    public void accountEmpty() {
        text_input_account.setError(getResources().getString(R.string.account_empty));
    }

    @Override
    public void passwordEmpty() {
        text_input_pass.setError(getResources().getString(R.string.password_empty));
    }

    @Override
    public void loginSuccess(int type) {
        closeProgressDialog();
        switch (type) {
            case USER_ORDINARY:
                UserMainActivity.start(this);
                break;
            case USER_ADMIN:
                break;
        }
        finish();
    }

    @Override
    public void loginFail() {
        closeProgressDialog();
        ToastUtil.show(R.string.login_fail);
    }
}
