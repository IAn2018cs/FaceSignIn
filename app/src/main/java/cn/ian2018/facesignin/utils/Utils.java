package cn.ian2018.facesignin.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import cn.ian2018.facesignin.MyApplication;


/**
 * 工具类
 */
public class Utils {

    /**
     * 判断网络情况
     * @param context 上下文
     * @return false 表示没有网络 true 表示有网络
     */
    public static boolean isNetworkAvalible(Context context) {
        // 获得网络状态管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 建立网络数组
            NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();

            if (net_info != null) {
                for (int i = 0; i < net_info.length; i++) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {

                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 如果没有网络，则弹出网络设置对话框
    public static void checkNetwork(final Activity activity) {
        if (!Utils.isNetworkAvalible(activity)) {
            TextView msg = new TextView(activity);
            msg.setText("     当前没有可以使用的网络，部分功能可能无法使用，请设置网络！");
            new AlertDialog.Builder(activity)
                    //.setIcon(R.mipmap.ic_launcher)
                    .setTitle("网络状态提示")
                    .setView(msg)
                    .setNegativeButton("朕知道了",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                    .setPositiveButton("开启网络",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    // 跳转到设置界面
                                    activity.startActivityForResult(new Intent(
                                                    Settings.ACTION_WIRELESS_SETTINGS),
                                            0);
                                }
                            }).create().show();
        }
        return;
    }

    // 判断服务是否在运行
    public static boolean ServiceIsWorked(String name) {
        ActivityManager myManager = (ActivityManager) MyApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>)myManager.getRunningServices(300);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // 获取本应用版本号
    public static int getVersionCode() {
        // 拿到包管理者
        PackageManager pm = MyApplication.getContext().getPackageManager();
        // 获取包的基本信息
        try {
            PackageInfo info = pm.getPackageInfo(MyApplication.getContext().getPackageName(), 0);
            // 返回应用的版本号
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 获取当前时间
    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
        return time;
    }

    // 获取当前日期
    public static String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String time = df.format(new Date());
        return time;
    }

    // 获取当前星期
    public static  int getWeek(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        return Integer.valueOf(mWay);
    }

    // 根据编号获取组别
    public static String getGroup(int groupCode) {
        String group = "";
        String[] groups = new String[]{"Android组","iOS组","Java组","PHP组","行政组","前端组","视频组",".NET组"};
        if (groupCode == 0) {
            group = "请修改组别";
        } else {
            group = groups[groupCode-1];
        }
        return group;
    }

    // 根据编号获取兴趣小组
    public static String getInterestGroup(int interest) {
        String group = "";
        String[] groups = new String[]{"前端实现","后台实现","大数据","物联网","微信小程序","区块链","人工智能"};
        if (interest == 0) {
            group = "无";
        } else {
            group = groups[interest-1];
        }
        return group;
    }

    // 根据用户类型获取rule
    public static int getRuleForUser() {
        int GENERAL_USER = 0;   // 普通用户
        final int ADMIN = 1;  // 超级管理员
        final int CHAIRMAN = 2;   // 主席
        final int LEADER = 3; // 组长
        final int HR = 4; // 人力
        final int REPAIR = 5; // 维修

        int rule = -1;
        int type = SpUtil.getInt(Constant.USER_TYPE, 0);
        switch (type) {
            // 活动
            case ADMIN:
            case CHAIRMAN:
                rule = 1;
                break;
            // 培训
            case LEADER:
            case HR:
                rule = 3;
                break;
        }

        return rule;
    }

    // 根据用户类型获取活动账号
    public static int getAccount() {
        int account=0;
        int type = SpUtil.getInt(Constant.USER_TYPE,0);
        switch (type) {
            // 管理员
            case 1:
                account = -1;
                break;
            // 主席
            case 2:
                account = 0;
                break;
            // 组长 人力
            case 3:
            case 4:
                account = SpUtil.getInt(Constant.USER_GROUP,0);
                break;
        }
        return account;
    }
}