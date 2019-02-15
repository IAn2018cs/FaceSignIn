package cn.ian2018.facesignin.ui.activedetail;

import android.content.Context;
import android.content.Intent;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.ui.base.BaseActivity;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2019/2/15
 */
public class ActiveDetailActivity extends BaseActivity<ActiveDetailPresenter> implements ActiveDetailContract.ActiveDetailView {

    public static void start(Context context, Active.DataBean active, String yunziId) {
        Intent starter = new Intent(context, ActiveDetailActivity.class);
        starter.putExtra("active", active);
        starter.putExtra("yunziId", yunziId);
        context.startActivity(starter);
    }

    @Override
    protected ActiveDetailPresenter createPresenter() {
        return new ActiveDetailPresenter();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_active_detail);
    }
}
