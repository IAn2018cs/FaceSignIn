package cn.ian2018.facesignin.ui.activemove;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.ian2018.facesignin.MyApplication;
import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.bean.SignItem;
import cn.ian2018.facesignin.bean.SignOutResult;
import cn.ian2018.facesignin.data.Constant;
import cn.ian2018.facesignin.data.SpUtil;
import cn.ian2018.facesignin.data.db.MyDatabase;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import rx.Subscriber;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/21
 */
public class ActiveMovePresenter extends BasePresenter<ActiveMoveContract.IActiveMoveView> implements ActiveMoveContract.IActiveMovePresenter {

    private ActiveMoveModel mActiveMoveModel;
    private MyDatabase mDatabase;

    private String mOutLocation = "";
    private UpdateTimeHandler mUpdateTimeHandler;

    private Active.DataBean mActive;
    private SignItem mSignItem;

    private Context mContext;

    private int clickCount;

    public boolean isClick = false;

    public ActiveMovePresenter() {
        mContext = MyApplication.getContext();
        mActiveMoveModel = new ActiveMoveModel();
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

                mOutLocation = addr + locationDescribe;
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
    public void checkSignOut() {
        // TODO 检查是否过了签离时间
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
            // 获取签到时的日期
            String inTime = mSignItem.getInTime().substring(0,10);
            // 获取活动结束的时间
            String endTime1 = SpUtil.getString(Constant.ACTIVE_MOVE_END_TIME, "").replace("T", " ").substring(11, 19);
            // 获取当前日期
            String dayTime = sdf1.format(new Date());

            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
            // 获取当前时间
            String hourTime = sdf2.format(new Date());
            // 转换成毫秒 活动结束的时间
            long end = sdf2.parse(endTime1).getTime();
            // 转换成毫秒 现在的时间
            long time = sdf2.parse(hourTime).getTime();

            // 如果超过一天
            if ((sdf1.parse(dayTime).getTime() - sdf1.parse(inTime).getTime()) >= 1000 * 60 * 60 * 24) {
                uploadSignOutInfo(inTime + " " + endTime1);
            } else if ((sdf1.parse(dayTime).getTime() == sdf1.parse(inTime).getTime()) && time > end) {
                uploadSignOutInfo(inTime + " " + endTime1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // 清除未签离的活动数据
    private void clearUnSignOutData() {
        SpUtil.remove(Constant.SIGN_OUT_YUNZIID);
        SpUtil.remove(Constant.SIGN_OUT_ACTIVE_NAME);
        SpUtil.remove(Constant.SIGN_OUT_LOCATION);
        SpUtil.remove(Constant.SIGN_OUT_ACTIVE_ID);
        SpUtil.remove(Constant.SIGN_OUT_ENDTIME);

        SpUtil.remove(Constant.ACTIVE_MOVE_END_TIME);
        SpUtil.remove(Constant.ACTIVE_MOVE_YUNZI_ID);
    }

    // 保存未签离的数据
    @Override
    public void saveUnSignOutData(Active.DataBean dataBean, String yunziId) {
        mActive = dataBean;
        SpUtil.putString(Constant.SIGN_OUT_YUNZIID, yunziId);
        SpUtil.putString(Constant.SIGN_OUT_ACTIVE_NAME, dataBean.getActivityName());
        SpUtil.putString(Constant.SIGN_OUT_LOCATION, dataBean.getLocation());
        SpUtil.putInt(Constant.SIGN_OUT_ACTIVE_ID, dataBean.getId());
        SpUtil.putString(Constant.SIGN_OUT_ENDTIME, dataBean.getEndTime());

        SpUtil.putString(Constant.ACTIVE_MOVE_YUNZI_ID, yunziId);
        SpUtil.putString(Constant.ACTIVE_MOVE_END_TIME, dataBean.getEndTime());
    }

    @Override
    public void signOutClick() {
        // TODO 如果可以签离 为了优化用户体验，当连续点击15次，可以签到
        if (getView().isCanSignOut() || mActive.isScan() || clickCount > 14) {
            isClick = true;
            getView().showProgressDialog(R.string.sign_out_dialog_loading_mag);
            uploadSignOutInfo(null);
        } else {
            clickCount ++;
            getView().showToast(R.string.sign_out_fail_location_erro);
        }
    }

    @Override
    public void uploadSignOutInfo(String time) {
        String outTime;
        if (time == null) {
            // ("HH:mm:ss")(小时：分钟：秒)
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
            outTime = df.format(new Date());
        } else {
            outTime = time;
        }

        mActiveMoveModel.signOutResult(mSignItem.getNid(),outTime,mOutLocation).subscribe(new Subscriber<SignOutResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (getView() == null)  return;
                getView().closeProgressDialog();
                getView().showToast(R.string.sign_out_fail + e.getMessage());
            }

            @Override
            public void onNext(SignOutResult signOutResult) {
                if (getView() == null)  return;
                getView().closeProgressDialog();
                if (signOutResult.isSucessed()) {
                    getView().showToast(R.string.sign_out_success);
                    clearUnSignOutData();
                    mDatabase.updateSignOutTime(mSignItem.getNid(), outTime);

                    getView().finishActivity();
                } else {
                    getView().showToast(R.string.sign_out_fail + signOutResult.getMsg());
                }
            }
        });
    }

    @Override
    public void updateTime() {
        mUpdateTimeHandler = new UpdateTimeHandler(this);
        mSignItem = new SignItem();
        // 查询是否签离
        int resultCode = mDatabase.isSignOut(SpUtil.getString(Constant.ACCOUNT, ""), mActive.getId(), mSignItem);
        getView().updateInTimeText(mContext.getResources().getString(R.string.sign_in_time_text) + mSignItem.getInTime().substring(11));
        if (resultCode == 0) {
            updateTimeLooper();
        }
    }

    // 通过handler循环更新时间
    private void updateTimeLooper() {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            // 获取当前时间
            long now = System.currentTimeMillis();
            // 计算从签到时间开始，经过了多长时间
            long total = now - df.parse(mSignItem.getInTime()).getTime();
            // 将毫秒转换成时间格式
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(total);
            // 计算天数
            long days = total / (1000 * 60 * 60 * 24);
            // 计算小时数
            final long hours = (total - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            // 转换成时间格式
            final String totalTime = df.format(calendar.getTime());
            getView().updateTotalTimeText(hours + ":" + totalTime.substring(totalTime.indexOf(":") + 1));

            mUpdateTimeHandler.sendEmptyMessageDelayed(0,1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class UpdateTimeHandler extends Handler {
        private WeakReference<ActiveMovePresenter> mWeakReference;

        public UpdateTimeHandler(ActiveMovePresenter view) {
            mWeakReference = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            ActiveMovePresenter presenter = mWeakReference.get();
            if (presenter != null) {
                presenter.updateTimeLooper();
            }
        }
    }
}
