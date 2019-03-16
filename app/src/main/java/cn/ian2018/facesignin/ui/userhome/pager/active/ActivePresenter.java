package cn.ian2018.facesignin.ui.userhome.pager.active;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sensoro.cloud.SensoroManager;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.service.SensorService;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import cn.ian2018.facesignin.utils.Logs;
import cn.ian2018.facesignin.utils.Utils;
import rx.Subscriber;

import static cn.ian2018.facesignin.MyApplication.getContext;
import static cn.ian2018.facesignin.ui.userhome.pager.active.ActiveFragment.BLUETOOTH_CODE;
import static cn.ian2018.facesignin.ui.userhome.pager.active.ActiveFragment.SCAN_CODE;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/6
 */
public class ActivePresenter extends BasePresenter<ActiveContract.IActiveView> implements ActiveContract.IActivePresenter {

    ActiveContract.IActiveModel mActiveModel;
    private List<Active.DataBean> mActiveList = new ArrayList<>();

    public ActivePresenter() {
        mActiveModel = new ActiveModel();
    }


    @Override
    public void getActiveForNetwork(String sensoroId, boolean isScan) {
        mActiveModel.getActive(sensoroId).subscribe(new Subscriber<Active>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Logs.e("getActiveForNetwork  :" + e.toString());
            }

            @Override
            public void onNext(Active active) {
                if (active.isSucessed() && active.getData().size() > 0) {
                    for (Active.DataBean datum : active.getData()) {
                        // TODO 如果显示 并且 是普通活动或值班是当前星期：&& (week==0 || week == Utils.getWeek())
                        // 暂时先把当前星期判断去掉
                        if (datum.getDisplay() == 1) {
                            try {
                                // 获取当前时间，判断该活动是否已经失效，不失效时才添加到集合中
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String presentTime = sdf.format(new java.util.Date());
                                if (sdf.parse(presentTime).getTime() <= sdf.parse(datum.getEndTime().replace("T", " ").substring(0, 19)).getTime()) {
                                    if (getActiveForNid(datum.getId()) != null) {
                                        Active.DataBean activeForId = getActiveForNid(datum.getId());
                                        activeForId.setActivityName(datum.getActivityName());
                                        activeForId.setTime(datum.getTime());
                                        activeForId.setActivityDes(datum.getActivityDes());
                                        activeForId.setLocation(datum.getLocation());
                                        activeForId.setEndTime(datum.getEndTime());
                                        activeForId.setRule(datum.getRule());
                                        activeForId.setBackTo(datum.getBackTo());
                                        Logs.d("活动信息更新:" + datum.getActivityName());
                                    } else {
                                        datum.setScan(isScan); // 是否通过扫描二维码获取的
                                        mActiveList.add(datum);
                                        Logs.d("添加一个活动:" + datum.getActivityName());
                                    }
                                } else {
                                    Active.DataBean activeForId = getActiveForNid(datum.getId());
                                    mActiveList.remove(activeForId);
                                    Logs.d("这个活动过期了:" + datum.getActivityName());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Logs.e("该活动被删除：" + datum.getActivityName());
                        }
                    }
                    // 更新界面
                    getView().updateActiveList(mActiveList);
                    getView().getActiveSuccess();
                } else {
                    Logs.d("这个云子上没有活动：" + sensoroId);
                }
            }
        });
    }

    // 根据Nid获取对应的活动对象
    private Active.DataBean getActiveForNid(long nid) {
        for (Active.DataBean active : mActiveList) {
            if (active.getId() == nid) {
                return active;
            }
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, Intent data, SensoroManager sensoroManager) {
        switch (requestCode) {
            case BLUETOOTH_CODE:
                // 蓝牙可用
                if (sensoroManager.isBluetoothEnabled()) {
                    checkSensor();
                } else {
                    getView().showToast(R.string.open_bluetooth_text);
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
                            getView().showScanAnim();
                            getActiveForNetwork(result,true);
                        } catch (StringIndexOutOfBoundsException e) {
                            getView().showToast(R.string.sure_scan_on_sensor_text);
                        }
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        getView().showToast(R.string.parsing_qr_failed_text);
                    }
                }
        }
    }

    // 检查云子
    @Override
    public void checkSensor() {
        getView().showScanAnim();
        // 开启子线程一直检测获取云子的时间
        checkTimeThread();
        // 如果服务没有运行，开启服务
        if (!Utils.ServiceIsWorked(SensorService.class.getName())) {
            getContext().startService(new Intent(getContext(), SensorService.class));
        }
    }

    // 60s后检测是否获取到活动信息
    private void checkTimeThread() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mActiveList.size() == 0) {
                    getView().getActiveFail();
                }
            }
        },60000);
    }
}
