package cn.ian2018.facesignin.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * Description: 改变状态栏颜色
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/11
 */

public class StatusBarUtils {
    public static void setWindowStatusBarColor(Activity activity, int color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(color));

                //底部导航栏
//                window.setNavigationBarColor(activity.getResources().getColor(color));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
