package cn.ian2018.facesignin.ui.activemove;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.event.AutoSignOut;
import cn.ian2018.facesignin.event.SensorGone;
import cn.ian2018.facesignin.event.SensorUpdate;
import cn.ian2018.facesignin.ui.base.BaseActivity;

/**
 * Description: 活动签离界面
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/21
 */
public class ActiveMoveActivity extends BaseActivity<ActiveMovePresenter> implements ActiveMoveContract.IActiveMoveView {

    private TextView mActivityNameTv;
    private TextView mLocationTv;
    private TextView mInTimeTv;
    private TextView mTotalTimeTv;
    private Active.DataBean mActive;
    private String mYunziId;
    private List<String> mSensorList = new ArrayList<>();
    private boolean isCanSignOut = false;

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
        EventBus.getDefault().register(this);

        getPresenter().initLocation(this);

        Intent intent = getIntent();
        mActive = (Active.DataBean) intent.getSerializableExtra("active");
        mYunziId = intent.getStringExtra("yunziId");

        mActivityNameTv.setText(mActive.getActivityName());
        mLocationTv.setText(mActive.getLocation());

        getPresenter().saveUnSignOutData(mActive, mYunziId);

        getPresenter().updateTime();

        getPresenter().checkSignOut();
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
                // 签离逻辑
                showSignOutConfirmDialog();
            }
        });
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_active_move);
    }

    // 云子更新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SensorUpdate event) {
        if (!mSensorList.contains(event.getYunziId())) {
            mSensorList.add(event.getYunziId());
        }
        isCanSignOut = mSensorList.contains(mYunziId);
    }

    // 云子消失
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SensorGone event) {
        if (mSensorList.contains(event.getYunziId())) {
            mSensorList.remove(event.getYunziId());
        }
        isCanSignOut = mSensorList.contains(mYunziId);
    }

    // 自动签离
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AutoSignOut event) {
        getPresenter().isClick = true;
        getPresenter().uploadSignOutInfo(null);
        finish();
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

    // 显示确认签离对话框
    private void showSignOutConfirmDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        //设置对话框左上角图标
        builder.setIcon(R.mipmap.ic_launcher);
        //设置对话框标题
        builder.setTitle(R.string.sign_out_dialog_title);
        //设置文本内容
        builder.setMessage(R.string.sign_out_dialog_msg);
        //设置积极的按钮
        builder.setPositiveButton(R.string.sign_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getPresenter().signOutClick();
            }
        });
        //设置消极的按钮
        builder.setNegativeButton(R.string.sign_dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        // 在页面销毁时，如果没有点击过签离按钮，就自动签离
        if (!getPresenter().isClick) {
            getPresenter().uploadSignOutInfo(null);
        }
    }

    @Override
    public void updateInTimeText(String time) {
        mInTimeTv.setText(time);
    }

    @Override
    public void updateTotalTimeText(String time) {
        mTotalTimeTv.setText(time);
    }

    @Override
    public boolean isCanSignOut() {
        return isCanSignOut;
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
