package cn.ian2018.facesignin.ui.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ian2018.facesignin.utils.ToastUtil;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/12
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {
    private P mPresenter;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createView(inflater, container);

        // 绑定presenter
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attach(this);
        }

        initView(view);

        initData();

        return view;
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container);

    protected abstract void initView(View view);

    protected abstract void initData();

    protected abstract P createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void showProgressDialog(int msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
        }
        mProgressDialog.setMessage(getString(msg));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showToast(int stringId) {
        ToastUtil.show(stringId);
    }

    @Override
    public void showToast(String string) {
        ToastUtil.show(string);
    }
}
