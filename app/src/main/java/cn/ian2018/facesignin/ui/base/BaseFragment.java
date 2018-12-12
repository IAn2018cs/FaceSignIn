package cn.ian2018.facesignin.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/12
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {
    private P mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createView();

        // 绑定presenter
        mPresenter = createPresenter();
        mPresenter.attach(this);

        initView(view);

        initData();

        return view;
    }

    protected abstract View createView();

    protected abstract void initView(View view);

    protected abstract void initData();

    protected abstract P createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detach();
    }

    public P getPresenter() {
        return mPresenter;
    }
}
