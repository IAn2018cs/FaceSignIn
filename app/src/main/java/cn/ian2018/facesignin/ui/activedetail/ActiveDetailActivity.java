package cn.ian2018.facesignin.ui.activedetail;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.data.db.MyDatabase;
import cn.ian2018.facesignin.ui.base.BaseActivity;

import static cn.ian2018.facesignin.ui.userhome.pager.active.ActiveFragment.TYPE_DUTY;
import static cn.ian2018.facesignin.ui.userhome.pager.active.ActiveFragment.TYPE_ORDINARY;
import static cn.ian2018.facesignin.ui.userhome.pager.active.ActiveFragment.TYPE_READ;
import static cn.ian2018.facesignin.ui.userhome.pager.active.ActiveFragment.TYPE_RUN;
import static cn.ian2018.facesignin.ui.userhome.pager.active.ActiveFragment.TYPE_TRAINING;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2019/2/15
 */
public class ActiveDetailActivity extends BaseActivity<ActiveDetailPresenter> implements ActiveDetailContract.ActiveDetailView , View.OnClickListener {

    private TextView mActivityLocationTv;
    private TextView mActivityDesTv;
    private TextView mTimeTv;
    private TextView mEndTimeTv;
    private CollapsingToolbarLayout mToolbarLayout;
    private Active.DataBean mActive;
    private String mYunziId;

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
        getPresenter().initLocation(this);

        Intent intent = getIntent();
        mActive = (Active.DataBean) intent.getSerializableExtra("active");
        mYunziId = intent.getStringExtra("yunziId");

        // 设置标题和背景图
        mToolbarLayout.setTitle(mActive.getActivityName());

        mActivityLocationTv.setText(new StringBuilder()
                .append(getResources().getString(R.string.active_item_location_text))
                .append(mActive.getLocation()));
        mActivityDesTv.setText("    " + mActive.getActivityDes());
        switch (mActive.getRule()) {
            case TYPE_ORDINARY:
            case TYPE_TRAINING:
                mTimeTv.setText(new StringBuilder()
                        .append(getResources().getString(R.string.start_time_text))
                        .append(mActive.getTime().replace("T", " ").substring(0, 16)));
                mEndTimeTv.setText(new StringBuilder()
                        .append(getResources().getString(R.string.end_time_text))
                        .append(mActive.getEndTime().replace("T", " ").substring(0, 16)));
                break;
            case TYPE_DUTY:
            case TYPE_READ:
            case TYPE_RUN:
                mTimeTv.setText(new StringBuilder()
                        .append(getResources().getString(R.string.start_time_text))
                        .append(mActive.getTime().replace("T", " ").substring(11, 16)));
                mEndTimeTv.setText(new StringBuilder()
                        .append(getResources().getString(R.string.end_time_text))
                        .append(mActive.getEndTime().replace("T", " ").substring(11, 16)));
                break;
        }
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnabled(false);

        mToolbarLayout = findViewById(R.id.toolbar_layout);

        mActivityLocationTv = findViewById(R.id.tv_location);
        mActivityDesTv = findViewById(R.id.tv_des);
        mTimeTv = findViewById(R.id.tv_time);
        mEndTimeTv = findViewById(R.id.tv_end_time);
        Button signButton = findViewById(R.id.loginButton);

        signButton.setOnClickListener(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_active_detail);
    }

    @Override
    public void onClick(View v) {
        getPresenter().signIn(mActive,mYunziId);
    }
}
