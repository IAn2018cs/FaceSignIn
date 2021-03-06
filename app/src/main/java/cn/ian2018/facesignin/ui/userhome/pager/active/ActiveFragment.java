package cn.ian2018.facesignin.ui.userhome.pager.active;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sensoro.cloud.SensoroManager;
import com.skyfishjy.library.RippleBackground;

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
import cn.ian2018.facesignin.ui.activedetail.ActiveDetailActivity;
import cn.ian2018.facesignin.ui.activity.ScanActivity;
import cn.ian2018.facesignin.ui.base.BaseFragment;
import cn.ian2018.facesignin.ui.base.BaseListViewAdapter;
import cn.ian2018.facesignin.utils.Logs;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/5
 */
public class ActiveFragment extends BaseFragment<ActivePresenter> implements ActiveContract.IActiveView {

    public static final int BLUETOOTH_CODE = 0;
    public static final int SCAN_CODE = 1;

    public static final int TYPE_ORDINARY = 1; // 普通活动
    public static final int TYPE_DUTY = 2; // 值班
    public static final int TYPE_TRAINING = 3; // 培训
    public static final int TYPE_RUN = 4; // 跑步
    public static final int TYPE_READ = 5; // 晨读

    private List<Active.DataBean> mActiveList = new ArrayList<>();
    private List<Saying.DataBean> sayingList;
    private SensoroManager sensoroManager;
    private TextView tv_scan;
    private RippleBackground rippleBackground;
    private ImageView foundDevice;
    private TextView tv_saying;
    private MyAdapter mAdapter;
    private String yunziId;

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
                startActivityForResult(new Intent(getContext(), ScanActivity.class), SCAN_CODE);
            }
        });

        ListView listView = view.findViewById(R.id.lv_active);
        mAdapter = new MyAdapter();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Active.DataBean active = mActiveList.get(position);
                ActiveDetailActivity.start(getContext(), active, yunziId);
            }
        });
    }

    @Override
    protected void initData() {
        sensoroManager = SensoroManager.getInstance(getContext());
        MyDatabase db = MyDatabase.getInstance();
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
        yunziId = info.getYunziId();
        // 根据云子id从网络获取具体活动信息
        getPresenter().getActiveForNetwork(info.getYunziId(), false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getPresenter().onActivityResult(requestCode, data, sensoroManager);
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
        }, 2000);
    }

    @Override
    public void hideScanText() {
        if (tv_scan != null) {
            tv_scan.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mActiveList.clear();
    }

    // listview适配器
    public class MyAdapter extends BaseListViewAdapter<MyAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_active_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(int position, ViewHolder holder) {
            if (getItem(position).getBackTo() == 1) {
                holder.tv_name.setText(getItem(position).getActivityName() + " 补班");
            } else {
                holder.tv_name.setText(getItem(position).getActivityName());
            }
            holder.tv_location.setText(new StringBuilder()
                    .append(getResources().getString(R.string.active_item_location_text))
                    .append(getItem(position).getLocation()));
            switch (getItem(position).getRule()) {
                case TYPE_ORDINARY:
                case TYPE_TRAINING:
                    holder.tv_time.setText(getItem(position).getTime().replace("T", " ").substring(0, 16));
                    break;
                case TYPE_DUTY:
                case TYPE_READ:
                case TYPE_RUN:
                    holder.tv_time.setText(new StringBuilder()
                            .append(getResources().getString(R.string.active_item_time_text))
                            .append(getItem(position).getTime().replace("T", " ").substring(11, 16))
                            .append(" - ")
                            .append(getItem(position).getEndTime().replace("T", " ").substring(11, 16)));
                    break;
            }
        }

        @Override
        public int getCount() {
            return mActiveList.size();
        }

        @Override
        public Active.DataBean getItem(int position) {
            return mActiveList.get(position);
        }

        class ViewHolder extends BaseListViewAdapter.ViewHolder {
            TextView tv_name;
            TextView tv_location;
            TextView tv_time;

            public ViewHolder(View convertView) {
                super(convertView);
                tv_name = convertView.findViewById(R.id.tv_name);
                tv_location = convertView.findViewById(R.id.tv_location);
                tv_time = convertView.findViewById(R.id.tv_time);
            }
        }

    }


}
