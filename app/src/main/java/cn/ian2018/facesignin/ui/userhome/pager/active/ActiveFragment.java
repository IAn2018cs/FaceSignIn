package cn.ian2018.facesignin.ui.userhome.pager.active;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sensoro.cloud.SensoroManager;
import com.skyfishjy.library.RippleBackground;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.bean.Saying;
import cn.ian2018.facesignin.data.db.MyDatabase;
import cn.ian2018.facesignin.event.SensorShow;
import cn.ian2018.facesignin.ui.activity.ScanActivity;
import cn.ian2018.facesignin.ui.base.BaseFragment;
import cn.ian2018.facesignin.utils.Logs;
import cn.ian2018.facesignin.utils.ToastUtil;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/5
 */
public class ActiveFragment extends BaseFragment<ActivePresenter> implements ActiveContract.ActiveView{

    private static final int BLUETOOTH_CODE = 0;
    private static final int SCAN_CODE = 1;

    private static final int TYPE_ORDINARY = 1; // 普通活动
    private static final int TYPE_DUTY = 2; // 值班
    private static final int TYPE_TRAINING = 3; // 培训
    private static final int TYPE_RUN = 4; // 跑步
    private static final int TYPE_READ = 5; // 晨读
    private List<Active.DataBean> mActiveList = new ArrayList<>();
    private ListView listView;
    private MyDatabase db;
    private List<Saying.DataBean> sayingList;
    private SensoroManager sensoroManager;
    private TextView tv_scan;
    private RippleBackground rippleBackground;
    private ImageView foundDevice;
    private TextView tv_saying;
    private MyAdapter mAdapter;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_active, container, false);
    }

    @Override
    protected void initView(View view) {
        rippleBackground = view.findViewById(R.id.content);
        foundDevice = view.findViewById(R.id.foundDevice);
        tv_saying = view.findViewById(R.id.tv_saying);

        tv_scan = view.findViewById(R.id.tv_scan);
        tv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), ScanActivity.class),SCAN_CODE);
            }
        });

        listView = view.findViewById(R.id.lv_active);
        mAdapter = new MyAdapter();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Active active = mActiveList.get(position);
//                Intent intent = new Intent(getContext(), DetailActivity.class);
//                intent.putExtra("active", active);
//                intent.putExtra("yunziId", yunziId);
//                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        sensoroManager = SensoroManager.getInstance(getContext());
        db = MyDatabase.getInstance();
        sayingList = db.getSaying();

        // 注册云子相关事件
        EventBus.getDefault().register(this);

        checkBluetooth();
    }

    // 检查蓝牙是否可用
    private void checkBluetooth() {
        if (!sensoroManager.isBluetoothEnabled()) {
            Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(bluetoothIntent, BLUETOOTH_CODE);
        } else {
            getPresenter().checkSensor();
        }
    }

    // 发现云子
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SensorShow info) {
        Logs.d("发现云子:" + info.getYunziId());
        // 根据云子id从网络获取具体活动信息
        getPresenter().getActiveForNetwork(info.getYunziId(),false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BLUETOOTH_CODE:
                // 蓝牙可用
                if (sensoroManager.isBluetoothEnabled()) {
                    getPresenter().checkSensor();
                } else {
                    ToastUtil.show("请打开蓝牙");
                }
                break;
            case SCAN_CODE:
                //处理扫描结果（在界面上显示）
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        try {
                            String result = bundle.getString(CodeUtils.RESULT_STRING);
                            // 解析后操作
                            if (result.substring(0,4).equals("http")) {
                                result = result.substring(13,25);
                            } else {
                                result = result.substring(0,12);
                            }
                            // 获取活动
                            showScanAnim();
                            getPresenter().getActiveForNetwork(result,true);
                        } catch (StringIndexOutOfBoundsException e) {
                            ToastUtil.showLong("请确保您扫描的是云子上的二维码");
                        }
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        ToastUtil.show("解析二维码失败");
                    }
                }
        }
    }

    @Override
    protected ActivePresenter createPresenter() {
        return new ActivePresenter();
    }

    @Override
    public void showScanAnim() {
        // 产生一个随机数
        int number = (int) (Math.random() * 100) + 1;
        if (sayingList.size() > 0) {
            int position = number % sayingList.size();
            tv_saying.setText(sayingList.get(position).getContent());
        }
        rippleBackground.setVisibility(View.VISIBLE);
        rippleBackground.startRippleAnimation();
    }

    // 找到云子的动画
    private void foundDevice() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList = new ArrayList<>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        foundDevice.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    @Override
    public void closeScanAnim() {
        foundDevice();
        rippleBackground.postDelayed(new Runnable() {
            @Override
            public void run() {
                rippleBackground.stopRippleAnimation();
                foundDevice.setVisibility(View.GONE);
                rippleBackground.setVisibility(View.GONE);
            }
        }, 2000);
    }

    @Override
    public void updateActiveList(List<Active.DataBean> actives) {
        // 如果有活动，就把TextView隐藏
        tv_scan.setVisibility(View.GONE);
        // 刷新列表
        mActiveList = actives;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getActiveSuccess() {
        foundDevice.setImageResource(R.mipmap.ic_phone2);
        closeScanAnim();
    }

    @Override
    public void getActiveFail() {
        foundDevice.setImageResource(R.mipmap.ic_error);
        closeScanAnim();
        // 延迟2秒再显示textview  为的是等动画结束
        tv_scan.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_scan.setVisibility(View.VISIBLE);
            }
        },2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mActiveList.clear();
    }

    // listview适配器
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mActiveList.size();
        }

        @Override
        public Active.DataBean getItem(int position) {
            return mActiveList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_active_list, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
                viewHolder.tv_location = convertView.findViewById(R.id.tv_location);
                viewHolder.tv_time = convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (getItem(position).getBackTo() == 1) {
                viewHolder.tv_name.setText(getItem(position).getActivityName() + " 补班");
            } else {
                viewHolder.tv_name.setText(getItem(position).getActivityName());
            }
            viewHolder.tv_location.setText("地点: "+getItem(position).getLocation());
            switch (getItem(position).getRule()) {
                case TYPE_ORDINARY:
                case TYPE_TRAINING:
                    viewHolder.tv_time.setText(getItem(position).getTime().replace("T", " ").substring(0, 16));
                    break;
                case TYPE_DUTY:
                case TYPE_READ:
                case TYPE_RUN:
                    viewHolder.tv_time.setText("时间: "+getItem(position).getTime().replace("T", " ").substring(11, 16)
                            + " - " + getItem(position).getEndTime().replace("T", " ").substring(11, 16));
                    break;
            }

            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_location;
        TextView tv_time;
    }
}
