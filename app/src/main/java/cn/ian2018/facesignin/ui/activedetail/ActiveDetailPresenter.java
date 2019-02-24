package cn.ian2018.facesignin.ui.activedetail;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.ian2018.facesignin.MyApplication;
import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.bean.SignInResult;
import cn.ian2018.facesignin.bean.SignItem;
import cn.ian2018.facesignin.data.Constant;
import cn.ian2018.facesignin.data.SpUtil;
import cn.ian2018.facesignin.data.db.MyDatabase;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import rx.Subscriber;

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
public class ActiveDetailPresenter extends BasePresenter<ActiveDetailContract.ActiveDetailView> implements ActiveDetailContract.ActiveDetailPresenter {

    private final ActiveDetailModel mActiveDetailModel;
    private final MyDatabase mDatabase;
    private String mInLocation = "";
    private int mClickCount;
    private int clickDebug;

    public ActiveDetailPresenter() {
        mActiveDetailModel = new ActiveDetailModel();
        mDatabase = MyDatabase.getInstance();
    }

    @Override
    public void initLocation(Context context) {
        // 声明LocationClient类
        LocationClient mLocationClient = new LocationClient(context);
        // 注册监听函数
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                String addr = bdLocation.getAddrStr();    //获取详细地址信息
                String locationDescribe = bdLocation.getLocationDescribe();    //获取位置描述信息

                mInLocation = addr + locationDescribe;
            }
        });

        // 配置定位信息
        LocationClientOption option = new LocationClientOption();
        //设置定位模式，默认高精度
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置返回经纬度坐标类型，默认gcj02  bd09ll：百度经纬度坐标；
        option.setCoorType("bd09ll");
        //设置发起定位请求的间隔，int类型，单位ms
        option.setScanSpan(60*1000*60);
        //设置是否使用gps，默认false
        option.setOpenGps(true);
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false);
        //是否需要地址信息，默认为不需要，即参数为false
        option.setIsNeedAddress(true);
        //是否需要位置描述信息，默认为不需要，即参数为false
        option.setIsNeedLocationDescribe(true);
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(option);

        // 开始定位
        mLocationClient.start();
    }

    @Override
    public void signIn(Active.DataBean active) {
        switch (active.getRule()) {
            // 日常活动
            case TYPE_DUTY:
            case TYPE_RUN:
            case TYPE_READ:
                // 如果当前签到时间距离上一次签到时间不超过24个小时，就不能再次签到
                if (mDatabase.isRecentSign(SpUtil.getString(Constant.ACCOUNT, ""), active.getId()) || clickDebug > 24) {
                    if (TimeCompareDay(active, active.getTime().replace("T", " ").substring(11, 19))) {
                        // TODO 如果能检测到云子 可以签到 为了优化用户体验，当连续点击15次，可以签到
                        // 如果是扫描获取的活动，可以直接签到
                        if (getView().isCanSign() || mClickCount > 14 || active.isScan()) {
                            signInForService(active);
                        } else {
                            getView().showToast(R.string.unable_sign_location_error);
                            mClickCount++;
                        }
                    } else {
                        getView().showToast(R.string.unable_sign_time_error);
                    }
                } else if (mDatabase.isSignOut(SpUtil.getString(Constant.ACCOUNT, ""), active.getId(), new SignItem()) == 0){
                    getView().showToast(R.string.unable_sign_unsignout_error);
                } else {
                    clickDebug++;
                    getView().showToast(R.string.unable_sign_already_error);
                }
                break;
            // 普通活动
            case TYPE_ORDINARY:
            case TYPE_TRAINING:
                int flag = mDatabase.isSign2(SpUtil.getString(Constant.ACCOUNT, ""), active.getId());
                switch (flag) {
                    // 如果还没有签到过
                    case 0:
                        // 如果符合时间
                        if (TimeCompare(active, active.getTime().replace("T", " ").substring(0, 19))) {
                            // 如果能检测到云子 可以签到
                            if (getView().isCanSign() || mClickCount > 14 || active.isScan()) {
                                signInForService(active);
                            } else {
                                getView().showToast(R.string.unable_sign_location_error);
                                mClickCount++;
                            }
                        } else {
                            getView().showToast(R.string.unable_sign_time_error);
                        }
                        break;
                    // 已经签到过
                    case 1:
                        getView().showToast(R.string.unable_sign_already_error);
                        break;
                    // 没有签离
                    case 2:
                        getView().showToast(R.string.unable_sign_unsignout_error);
                        break;
                }
                break;
        }
    }

    private void signInForService(Active.DataBean active){
        getView().showProgressDialog(R.string.sign_dialog_loading_mag);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        String time = df.format(new Date());
        mActiveDetailModel.signInResult(SpUtil.getString(Constant.ACCOUNT, ""),active.getId(),time,time,mInLocation)
                .subscribe(new Subscriber<SignInResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                getView().showToast(R.string.sign_fail);
                getView().closeProgressDialog();
            }

            @Override
            public void onNext(SignInResult signInResult) {
                getView().closeProgressDialog();
                if (signInResult.isSucessed()) {
                    saveSignInData(active, time, signInResult.getData());
                    // 到签离界面
                    getView().goSinOutActivity();
                } else {
                    getView().showToast(R.string.sign_fail);
                }
            }
        });
    }

    // 保存数据到本地数据库
    private void saveSignInData(Active.DataBean active, String inTime, int nid) {
        SignItem signItem = new SignItem();

        signItem.setNumber(SpUtil.getString(Constant.ACCOUNT,""));
        signItem.setActiveId(active.getId());
        signItem.setInTime(inTime);
        signItem.setOutTime(inTime);
        signItem.setNid(nid);

        mDatabase.saveSignItem(signItem);
    }

    // 判断是否到了签到时间  signTime 需要签到的时间
    private boolean TimeCompare(Active.DataBean active, String signTime) {
        // 设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        // 当前时间
        String presentTime = sdf.format(new Date());
        try {
            Date beginTime = sdf.parse(signTime);
            Date current = sdf.parse(presentTime);
            Date aEndTime = sdf.parse(active.getEndTime().replace("T", " ").substring(0, 19));
            // 可以提前10分钟签到     并且不能超过要活动结束时间
            if (((current.getTime() + 1000 * 60 * 10) >= beginTime.getTime()) && (current.getTime() < aEndTime.getTime())) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    // 判断日常活动是否在指定时间范围内
    private boolean TimeCompareDay(Active.DataBean active, String signTime) {
        // 设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        // 当前时间
        String presentTime = sdf.format(new Date());
        try {
            Date beginTime = sdf.parse(signTime);
            Date current = sdf.parse(presentTime);
            Date aEndTime = sdf.parse(active.getEndTime().replace("T", " ").substring(11, 19));
            // 可以提前10分钟签到     并且不能超过要活动结束时间
            if (((current.getTime() + 1000 * 60 * 10) >= beginTime.getTime()) && (current.getTime() < aEndTime.getTime())) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }
}
