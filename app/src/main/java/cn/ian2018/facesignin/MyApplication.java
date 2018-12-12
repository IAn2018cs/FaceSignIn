package cn.ian2018.facesignin;

import android.app.Application;
import android.content.Context;

import cn.ian2018.facesignin.utils.Logs;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/11
 */
public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        // 捕获全局未捕获的异常
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                // 输出异常
                ex.printStackTrace();
                Logs.e("捕获到的异常：" + ex.toString());
                System.exit(0);
            }
        });
    }

    public static Context getContext () {
        return mContext;
    }
}
