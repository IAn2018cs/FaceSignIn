package cn.ian2018.facesignin.ui.activemove;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.ui.base.BaseActivity;

/**
 * Description: 活动签离界面
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/21
 */
public class ActiveMoveActivity extends BaseActivity<ActiveMovePresenter> implements ActiveMoveContract.ActiveMoveView {

    private TextView mActivityNameTv;
    private TextView mLocationTv;
    private TextView mInTimeTv;
    private TextView mTotalTimeTv;
    private Active.DataBean mActive;
    private String mYunziId;

    public static void start(Context context, Active.DataBean active, String yunziId) {
        Intent starter = new Intent(context, ActiveMoveActivity.class);
        starter.putExtra("active", active);
        starter.putExtra("yunziId", yunziId);
        context.startActivity(starter);
    }

    @Override
    protected ActiveMovePresenter createPresenter() {
        return new ActiveMovePresenter();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mActive = (Active.DataBean) intent.getSerializableExtra("active");
        mYunziId = intent.getStringExtra("yunziId");

        mActivityNameTv.setText(mActive.getActivityName());
        mLocationTv.setText(mActive.getLocation());
    }

    @Override
    protected void initView() {
        mActivityNameTv = findViewById(R.id.tv_activeName);
        mLocationTv = findViewById(R.id.tv_location);
        mInTimeTv = findViewById(R.id.tv_inTime);
        mTotalTimeTv = findViewById(R.id.tv_total_time);
        Button moveButton = findViewById(R.id.moveButton);

        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 签离逻辑
                //showSignOutConfirmDialog();
            }
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_active_move);
    }

    // 重写返回键  使其回到桌面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // 通过隐示意图 开启桌面
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
