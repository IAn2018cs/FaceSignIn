package cn.ian2018.facesignin.ui.setting.changepassword;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.SimpleResult;
import cn.ian2018.facesignin.data.Constant;
import cn.ian2018.facesignin.data.SpUtil;
import cn.ian2018.facesignin.event.ExitEvent;
import cn.ian2018.facesignin.network.retrofit.RetrofitClient;
import cn.ian2018.facesignin.ui.base.BaseFragment;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import cn.ian2018.facesignin.ui.login.LoginActivity;
import cn.ian2018.facesignin.utils.Logs;
import cn.ian2018.facesignin.utils.MD5Util;
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
public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {

    private EditText mOldPasswordEt, mNewPasswordEt, mPasswordConfirmEt;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    protected void initView(View view) {
        mOldPasswordEt = view.findViewById(R.id.et_old);
        mNewPasswordEt = view.findViewById(R.id.et_new);
        mPasswordConfirmEt = view.findViewById(R.id.et_new_true);
        Button changeButton = view.findViewById(R.id.pwd_change);
        changeButton.setOnClickListener(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        String oldPwd = mOldPasswordEt.getText().toString().trim();
        String newPwd = mNewPasswordEt.getText().toString().trim();
        String newPwdConfirm = mPasswordConfirmEt.getText().toString().trim();
        if (!oldPwd.equals("") && !newPwd.equals("") && !newPwdConfirm.equals("")) {
            String oldMD5Pwd = MD5Util.strToMD5(oldPwd);
            if (!oldMD5Pwd.equals(SpUtil.getString(Constant.PASS_WORD, ""))) {
                ToastUtil.show(R.string.change_password_password_error);
            } else if (newPwd.equals(oldPwd)) {
                ToastUtil.show(R.string.change_password_password_same);
            } else if (newPwdConfirm.equals(newPwd)) {
                changePassword(newPwd);
            } else {
                ToastUtil.show(R.string.change_password_password_inconsistent);
            }
        } else {
            ToastUtil.show(R.string.password_empty);
        }
    }

    private void changePassword(String newPwd) {
        showProgressDialog(R.string.change_password_dialog_msg);
        String MD5Pass = MD5Util.strToMD5(newPwd);
        RetrofitClient.getServiceApi().changePassword(SpUtil.getString(Constant.ACCOUNT, ""), MD5Pass)
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
                        Logs.e("修改密码失败：" + e.getMessage());
                    }

                    @Override
                    public void onNext(SimpleResult simpleResult) {
                        if (simpleResult.isSucessed()) {
                            EventBus.getDefault().post(new ExitEvent());
                            SpUtil.putBoolean(Constant.IS_REMBER_PWD, false);
                            LoginActivity.start(getContext());
                            closeProgressDialog();
                            if (getActivity() != null) {
                                getActivity().finish();
                            }
                            ToastUtil.show(R.string.change_password_success);
                        } else {
                            ToastUtil.show(R.string.change_password_fail);
                        }
                    }
                });
    }
}
