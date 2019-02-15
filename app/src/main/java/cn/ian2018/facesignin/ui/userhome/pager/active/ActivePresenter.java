package cn.ian2018.facesignin.ui.userhome.pager.active;

import android.content.Intent;
import android.os.Handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.service.SensorService;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import cn.ian2018.facesignin.utils.Logs;
import cn.ian2018.facesignin.utils.Utils;
import rx.Subscriber;

import static cn.ian2018.facesignin.MyApplication.getContext;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/6
 */
public class ActivePresenter extends BasePresenter<ActiveContract.ActiveView> implements ActiveContract.ActivePresenter {

    ActiveContract.ActiveModel mActiveModel;
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
