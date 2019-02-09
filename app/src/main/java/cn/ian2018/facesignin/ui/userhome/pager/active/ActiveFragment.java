package cn.ian2018.facesignin.ui.userhome.pager.active;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.ui.base.BaseFragment;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/5
 */
public class ActiveFragment extends BaseFragment<ActivePresenter> implements ActiveContract.ActiveView{
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_active, container, false);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {
        //getPresenter().getActiveForNetwork("0117C596E37E",false);
    }

    @Override
    protected ActivePresenter createPresenter() {
        return new ActivePresenter();
    }
}
