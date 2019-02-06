package cn.ian2018.facesignin.ui.userhome.pager.active;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.ui.base.BaseFragment;
import cn.ian2018.facesignin.ui.base.BasePresenter;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/5
 */
public class ActiveFragment extends BaseFragment {
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_active, container, false);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return new ActivePresenter();
    }
}
