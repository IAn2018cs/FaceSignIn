package cn.ian2018.facesignin.ui.activedetail;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.data.db.MyDatabase;
import cn.ian2018.facesignin.ui.base.BasePresenter;

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
    public void signIn(Active.DataBean active, String yunziId) {

    }
}
