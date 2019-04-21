package cn.ian2018.facesignin.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.ian2018.facesignin.MyApplication;
import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.data.Constant;
import cn.ian2018.facesignin.data.SpUtil;
import cn.ian2018.facesignin.event.AutoSignOut;
import cn.ian2018.facesignin.event.SensorGone;
import cn.ian2018.facesignin.event.SensorShow;
import cn.ian2018.facesignin.event.SensorUpdate;
import cn.ian2018.facesignin.utils.Logs;


/**
 * 云子检查服务——陈帅
 */
public class SensorService extends Service {

    private SensoroManager sensoroManager;
    private List<String> oldSerialNumber = new ArrayList<>();
    private String startTime = "";
    private boolean isCheck = false;
    private boolean isOpen = false;
    private static final String CHANNEL_ID = "facesign";

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isOpen = true;
        Logs.d("onCreate服务创建");

        // 绑定前台服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "facesign", NotificationManager.IMPORTANCE_MIN);
            NotificationManager manager = (NotificationManager) MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        //Intent notificationIntent = new Intent(this,MainActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.notification_bar_title));
                //.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        //设置 Notification 的 flags = FLAG_NO_CLEAR
        //FLAG_ONGOING_EVENT 表示该通知通知放置在正在运行,不能被手动清除,但能通过 cancel() 方法清除
        //等价于 builder.setOngoing(true);
        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logs.d("onStartCommand服务连接");
        SpUtil.remove(Constant.ACTIVE_MOVE_YUNZI_ID);
        SpUtil.remove(Constant.ACTIVE_MOVE_END_TIME);
        sensoroManager = SensoroManager.getInstance(this);
        setSDK();
        startSDK();
        checkTime();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isOpen = false;
        Logs.d("onDestroy服务销毁");
        if (sensoroManager != null) {
            sensoroManager.stopService();
            Logs.d("停止sensoro服务");
        }
        // 如果activity销毁，就将集合清空
        oldSerialNumber.clear();
    }

    // 开启SDK
    private void startSDK() {
        /**
         * 设置启用云服务 (上传传感器数据，如电量、UMM等)。如果不设置，默认为关闭状态。
         **/
        sensoroManager.setCloudServiceEnable(true);
        /**
         * 启动 SDK 服务
         **/
        try {
            Logs.d("开启sensoro服务");
            sensoroManager.startService();
        } catch (Exception e) {
            e.printStackTrace(); // 捕获异常信息
        }
    }

    // 设置SDK
    private void setSDK() {
        BeaconManagerListener beaconManagerListener = new BeaconManagerListener() {
            /**
             * 发现传感器
             */
            @Override
            public void onNewBeacon(Beacon beacon) {
                // 序列号
                String serialNumber = beacon.getSerialNumber();
                Logs.d("service serialNumber:" + serialNumber);
                // 如果存储的云子id中不包含此次发现的  就将新的添加到集合中，发送广播
                if (!oldSerialNumber.contains(serialNumber)) {
                    oldSerialNumber.add(serialNumber);
                    /*Logs.d("service发现新云子:" + serialNumber);
                    Intent intent = new Intent();
                    intent.putExtra("yunzi", serialNumber);
                    intent.setAction("GET_YUNZI_ID");
                    sendBroadcast(intent);*/
                }
                Logs.d("service发现新云子:" + serialNumber);
                EventBus.getDefault().post(new SensorShow(serialNumber));

                // 签到 签离界面需要的广播 云子更新
                EventBus.getDefault().post(new SensorUpdate(serialNumber));

                if (SpUtil.getString(Constant.ACTIVE_MOVE_YUNZI_ID,"").equals(beacon.getSerialNumber())) {
                    isCheck = false;
                }
            }

            /**
             * 一个传感器消失
             */
            @Override
            public void onGoneBeacon(Beacon beacon) {
                Logs.d("service一个云子消失了:" + beacon.getSerialNumber());
                if (oldSerialNumber.contains(beacon.getSerialNumber())) {
                    oldSerialNumber.remove(beacon.getSerialNumber());
                    EventBus.getDefault().post(new SensorGone(beacon.getSerialNumber()));
                }

                if (SpUtil.getString(Constant.ACTIVE_MOVE_YUNZI_ID,"").equals(beacon.getSerialNumber())) {
                    isCheck = true;
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());//("HH:mm:ss")(小时：分钟：秒)
                    startTime = df.format(new Date());
                }
            }

            /**
             * 传感器更新
             */
            @Override
            public void onUpdateBeacon(final ArrayList<Beacon> beacons) {
                for (Beacon beacon : beacons) {
                    Logs.d("service云子更新：" + beacon.getSerialNumber());
                    // 签到 签离界面需要的广播
                    EventBus.getDefault().post(new SensorUpdate(beacon.getSerialNumber()));
                }
            }
        };

        sensoroManager.setBeaconManagerListener(beaconManagerListener);
    }

    // 开启一个子线程，一直检测
    private void checkTime() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    while (isOpen) {
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        String endTime = df.format(new Date());
                        Logs.d("开始时间：" + startTime);
                        Logs.d("结束时间：" + endTime);

                        if (isCheck && !startTime.equals("")) {
                            long start = df.parse(startTime).getTime();
                            long end = df.parse(endTime).getTime();
                            // 如果超过10分钟未检测到云子  就自动签离  1000*60*10 10分钟
                            Log.d("SIGN_TAG","时间间隔"+ (end-start));
                            // TODO 设置时间
                            if (Math.abs(end-start) > 1000*60*10) {
                                // 自动签离
                                Log.d("SIGN_TAG","发送自动签离的广播");
                                EventBus.getDefault().post(new AutoSignOut());
                            }
                        }

                        // 判断是否到了活动结束时间
                        if (!SpUtil.getString(Constant.ACTIVE_MOVE_END_TIME,"").equals("")) {
                            String endTime1 = SpUtil.getString(Constant.ACTIVE_MOVE_END_TIME, "").replace("T", " ").substring(11, 19);
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                            String presentTime = sdf.format(new Date());//当前时间

                            long end = sdf.parse(endTime1).getTime();
                            long time = sdf.parse(presentTime).getTime();

                            if (time > end) {
                                // 自动签离
                                Log.d("SIGN_TAG","发送自动签离的广播");
                                EventBus.getDefault().post(new AutoSignOut());
                            }
                        }

                        // 每隔10秒检测一次
                        Thread.sleep(1000*10);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
