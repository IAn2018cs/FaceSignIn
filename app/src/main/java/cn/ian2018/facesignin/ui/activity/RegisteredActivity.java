package cn.ian2018.facesignin.ui.activity;

import android.app.Activity;
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
public class RegisteredActivity extends BaseActivity implements View.OnClickListener {

    public static final int REGISTERED_CODE = 2;

    private EditText mAccountEt;
    private EditText mPasswordEt;
    private EditText mPasswordConfirmEt;
    private EditText mNameEt;
    private EditText mGradeEt;
    private EditText mClassEt;

    public static void start(Activity activity) {
        Intent starter = new Intent(activity, RegisteredActivity.class);
        activity.startActivityForResult(starter, REGISTERED_CODE);
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
        toolbar.setTitle(R.string.register_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAccountEt = findViewById(R.id.et_account);
        mPasswordEt = findViewById(R.id.et_password);
        mPasswordConfirmEt = findViewById(R.id.et_password_two);
        mNameEt = findViewById(R.id.et_name);
        mGradeEt = findViewById(R.id.et_grade);
        mClassEt = findViewById(R.id.et_class);

        Button submitButton = findViewById(R.id.bt_submit);
        submitButton.setOnClickListener(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_registered);
    }

    @Override
    public void onClick(View v) {
        String account = mAccountEt.getText().toString().trim();
        String password = mPasswordEt.getText().toString().trim();
        String password2 = mPasswordConfirmEt.getText().toString().trim();
        String name = mNameEt.getText().toString().trim();
        String grade = mGradeEt.getText().toString().trim();
        String classDes = mClassEt.getText().toString().trim();
        int gradeCode;
        try {
            gradeCode = Integer.parseInt(grade);
        } catch (Exception e) {
            gradeCode = 18;
        }

        // 如果都不为空
        if (!account.equals("") && !password.equals("") && !password2.equals("")
                && !name.equals("") && !grade.equals("") && !classDes.equals("")) {
            // 两次密码一致
            if (password.equals(password2)) {
                // 注册
                registeredAccount(account, password, name, gradeCode, classDes);
            } else {
                ToastUtil.show(R.string.change_password_password_inconsistent);
            }
        } else {
            ToastUtil.show(R.string.registered_info_empty);
        }
    }

    private void registeredAccount(String account, String password, String name, int grade, String classDes) {
        showProgressDialog(R.string.registered_dialog_msg);
        // 对密码加密
        String md5Pass = MD5Util.strToMD5(password);
        RetrofitClient.getServiceApi().registeredInfo(account, md5Pass, name, grade, classDes, 1, "")
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
                        ToastUtil.show(R.string.registered_fail);
                        Logs.e("注册失败 " + e.getMessage());
                    }

                    @Override
                    public void onNext(SimpleResult simpleResult) {
                        if (simpleResult.isSucessed()) {
                            ToastUtil.show(R.string.registered_success);
                            Intent intent = new Intent();
                            intent.putExtra("account", account);
                            intent.putExtra("password", password);
                            setResult(REGISTERED_CODE, intent);
                            finish();
                        } else {
                            ToastUtil.show(R.string.registered_fail);
                        }
                    }
                });
    }
}
