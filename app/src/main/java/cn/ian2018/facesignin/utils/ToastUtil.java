package cn.ian2018.facesignin.utils;

import android.widget.Toast;

import cn.ian2018.facesignin.MyApplication;


/**
 * 土司工具
 */
public class ToastUtil {
    /**
     * @param msg 打印文本内容
     */
    public static void show(String msg) {
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void show(int msgRes) {
        Toast.makeText(MyApplication.getContext(), msgRes, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param msg 打印文本内容
     */
    public static void showLong(String msg) {
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showLong(int msgRes) {
        Toast.makeText(MyApplication.getContext(), msgRes, Toast.LENGTH_LONG).show();
    }
}
