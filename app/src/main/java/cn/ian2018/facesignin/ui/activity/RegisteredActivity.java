package cn.ian2018.facesignin.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
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

    private String mAccount;
    private String mPassword;
    private String mName;
    private int mGradeCode;
    private String mClassDes;

    private boolean isNetError = false;

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
        mAccount = mAccountEt.getText().toString().trim();
        mPassword = mPasswordEt.getText().toString().trim();
        String password2 = mPasswordConfirmEt.getText().toString().trim();
        mName = mNameEt.getText().toString().trim();
        String grade = mGradeEt.getText().toString().trim();
        mClassDes = mClassEt.getText().toString().trim();
        try {
            mGradeCode = Integer.parseInt(grade);
        } catch (Exception e) {
            mGradeCode = 18;
        }

        // 如果都不为空
        if (!mAccount.equals("") && !mPassword.equals("") && !password2.equals("")
                && !mName.equals("") && !grade.equals("") && !mClassDes.equals("")) {
            // 两次密码一致
            if (mPassword.equals(password2)) {
                // TODO 检测账号是否冲突
                // 弹出要去注册人脸对话框
                showRegisterFaceDialog();
            } else {
                ToastUtil.show(R.string.change_password_password_inconsistent);
            }
        } else {
            ToastUtil.show(R.string.registered_info_empty);
        }
    }

    private void showRegisterFaceDialog() {
        if (!isNetError) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            //设置对话框左上角图标
            builder.setIcon(R.mipmap.ic_launcher);
            //设置对话框标题
            builder.setTitle(R.string.register_face_dialog_title);
            //设置文本内容
            builder.setMessage(R.string.register_face_dialog_msg);
            //设置积极的按钮
            builder.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    RegisterAndRecognizeActivity.startForResult(RegisteredActivity.this, mAccount, mName);
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
        } else {
            registeredAccount(mAccount, mPassword, mName, mGradeCode, mClassDes);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getBooleanExtra("register_flag", false)) {
            // 注册
            registeredAccount(mAccount, mPassword, mName, mGradeCode, mClassDes);
        } else {
            ToastUtil.show(R.string.registered_fail);
        }
    }

    // 网络请求 注册
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
                        isNetError = true;
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
