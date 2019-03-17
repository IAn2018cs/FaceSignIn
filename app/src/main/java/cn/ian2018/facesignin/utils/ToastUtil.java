package cn.ian2018.facesignin.utils;

import android.widget.Toast;

import cn.ian2018.facesignin.MyApplication;


/**
 * 土司工具
 */
public class ToastUtil {
    private static Toast sToast;

    /**
     * @param msg 打印文本内容
     */
    public static void show(String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            sToast.setDuration(Toast.LENGTH_SHORT);
            sToast.setText(msg);
        }
        sToast.show();
    }

    public static void show(int msgRes) {
        if (sToast == null) {
            sToast = Toast.makeText(MyApplication.getContext(), msgRes, Toast.LENGTH_SHORT);
        } else {
            sToast.setDuration(Toast.LENGTH_SHORT);
            sToast.setText(msgRes);
        }
        sToast.show();
    }

    /**
     * @param msg 打印文本内容
     */
    public static void showLong(String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_LONG);
        } else {
            sToast.setDuration(Toast.LENGTH_LONG);
            sToast.setText(msg);
        }
        sToast.show();
    }

    public static void showLong(int msgRes) {
        if (sToast == null) {
            sToast = Toast.makeText(MyApplication.getContext(), msgRes, Toast.LENGTH_LONG);
        } else {
            sToast.setDuration(Toast.LENGTH_LONG);
            sToast.setText(msgRes);
        }
        sToast.show();
    }
}
