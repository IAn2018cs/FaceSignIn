package cn.ian2018.facesignin.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.SimpleResult;
import cn.ian2018.facesignin.network.retrofit.RetrofitClient;
import cn.ian2018.facesignin.ui.base.BaseActivity;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import cn.ian2018.facesignin.utils.Logs;
import cn.ian2018.facesignin.utils.MD5Util;
import cn.ian2018.facesignin.utils.ToastUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/3/17
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    public static final int FORGET_CODE = 3;

    private EditText mAccountEt, mPasswordEt, mPasswordConfirmEt;

    public static void start(Activity activity) {
        Intent starter = new Intent(activity, ForgetPasswordActivity.class);
        activity.startActivityForResult(starter, FORGET_CODE);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.forget_password);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAccountEt = findViewById(R.id.et_number);
        mPasswordEt = findViewById(R.id.et_new);
        mPasswordConfirmEt = findViewById(R.id.et_new_true);
        Button changeButton = findViewById(R.id.pwd_change);
        changeButton.setOnClickListener(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_forget_password);
    }

    @Override
    public void onClick(View v) {
        String account = mAccountEt.getText().toString().trim();
        String password = mPasswordEt.getText().toString().trim();
        String passwordConfirm = mPasswordConfirmEt.getText().toString().trim();
        if (!account.equals("") && !password.equals("") && !passwordConfirm.equals("")) {
            if (passwordConfirm.equals(password)) {
                changePassword(account, password);
            } else {
                ToastUtil.show(R.string.change_password_password_inconsistent);
            }
        } else {
            ToastUtil.show(R.string.password_empty);
        }
    }

    private void changePassword(String account, String password) {
        showProgressDialog(R.string.change_password_dialog_msg);
        String MD5Password = MD5Util.strToMD5(password);
        RetrofitClient.getServiceApi().changePassword(account, MD5Password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SimpleResult>() {
                    @Override
                    public void onCompleted() {
                        closeProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        closeProgressDialog();
                        ToastUtil.show(R.string.change_password_fail);
                        Logs.e("修改密码 " + e.getMessage());
                    }

                    @Override
                    public void onNext(SimpleResult simpleResult) {
                        if (simpleResult.isSucessed()) {
                            ToastUtil.show(R.string.change_password_success);
                            Intent intent = new Intent();
                            intent.putExtra("account", account);
                            intent.putExtra("password", password);
                            setResult(FORGET_CODE, intent);
                            finish();
                        } else {
                            ToastUtil.show(R.string.change_password_fail);
                        }
                    }
                });
    }
}
