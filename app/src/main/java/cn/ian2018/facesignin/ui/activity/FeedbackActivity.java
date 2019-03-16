package cn.ian2018.facesignin.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.FeedbackResult;
import cn.ian2018.facesignin.bean.PhoneInfo;
import cn.ian2018.facesignin.data.Constant;
import cn.ian2018.facesignin.data.SpUtil;
import cn.ian2018.facesignin.network.retrofit.RetrofitClient;
import cn.ian2018.facesignin.ui.base.BaseActivity;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import cn.ian2018.facesignin.utils.PhoneInfoUtil;
import cn.ian2018.facesignin.utils.ToastUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2019/3/16
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = "FeedbackActivity";
    private EditText mFeedbackEdit;
    private CheckBox mAnonymousCb;

    public static void start(Context context) {
        Intent starter = new Intent(context, FeedbackActivity.class);
        context.startActivity(starter);
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
        toolbar.setTitle(R.string.mine_feedback_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFeedbackEdit = findViewById(R.id.feedback_edit);
        mAnonymousCb = findViewById(R.id.cb_anonymous);

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_feed_back);
    }

    @Override
    public void onClick(View v) {
        String message = mFeedbackEdit.getText().toString().trim();
        boolean checked = mAnonymousCb.isChecked();
        if (!TextUtils.isEmpty(message)) {
            showProgressDialog(R.string.feedback_loading_msg);
            // 上传反馈信息
            upFeedBack(message, checked);
        } else {
            ToastUtil.show(R.string.feedback_msg_empty);
        }
    }

    private void upFeedBack(String message, boolean checked) {
        PhoneInfo phoneInfo = PhoneInfoUtil.getPhoneInfo();
        RetrofitClient.getServiceApi()
                .uploadFeedbackInfo(SpUtil.getString(Constant.ACCOUNT, ""), message,
                        phoneInfo.getPhoneBrand(), phoneInfo.getPhoneBrandType(), phoneInfo.getAndroidVersion(), checked ? 1 : 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FeedbackResult>() {
                    @Override
                    public void onCompleted() {
                        closeProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(R.string.feedback_upload_fail);
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(FeedbackResult result) {
                        if (result.isSucessed()) {
                            ToastUtil.show(R.string.feedback_upload_success);
                            mFeedbackEdit.setText("");
                            finish();
                        } else {
                            ToastUtil.show(R.string.feedback_upload_fail);
                            Log.e(TAG, "onNext: 提交反馈失败 ");
                        }
                    }
                });
    }
}
