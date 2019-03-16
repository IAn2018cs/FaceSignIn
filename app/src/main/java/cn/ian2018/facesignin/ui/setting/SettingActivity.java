package cn.ian2018.facesignin.ui.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.data.Constant;
import cn.ian2018.facesignin.data.SpUtil;
import cn.ian2018.facesignin.event.ExitEvent;
import cn.ian2018.facesignin.ui.base.BaseActivity;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import cn.ian2018.facesignin.ui.login.LoginActivity;
import cn.ian2018.facesignin.utils.CheckVersionHelper;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2019/3/16
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private CheckVersionHelper mCheckVersionHelper;

    public static void start(Context context) {
        Intent starter = new Intent(context, SettingActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        mCheckVersionHelper = new CheckVersionHelper(this);
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.mine_setting_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout changeInfoLayout = findViewById(R.id.ll_change_info);
        LinearLayout changePasswordLayout = findViewById(R.id.ll_change_password);
        LinearLayout updateLayout = findViewById(R.id.ll_update);
        LinearLayout escLayout = findViewById(R.id.ll_esc);

        changeInfoLayout.setOnClickListener(this);
        changePasswordLayout.setOnClickListener(this);
        updateLayout.setOnClickListener(this);
        escLayout.setOnClickListener(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 修改个人信息
            case R.id.ll_change_info:
                break;
            // 修改密码
            case R.id.ll_change_password:
                break;
            // 检测更新
            case R.id.ll_update:
                mCheckVersionHelper.checkVersionCode(true);
                break;
            // 注销
            case R.id.ll_esc:
                showEscDialog();
                break;
        }
    }

    // 显示注销确认对话框
    private void showEscDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        //设置对话框左上角图标
        builder.setIcon(R.mipmap.ic_launcher);
        //设置对话框标题
        builder.setTitle(R.string.dialog_logout_title);
        //设置文本内容
        builder.setMessage(R.string.dialog_logout_msg);
        //设置积极的按钮
        builder.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EventBus.getDefault().post(new ExitEvent());
                LoginActivity.start(SettingActivity.this);
                SpUtil.putBoolean(Constant.IS_REMBER_PWD, false);
                finish();
            }
        });
        //设置消极的按钮
        builder.setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
