package cn.ian2018.facesignin.utils;

import android.telephony.TelephonyManager;


import cn.ian2018.facesignin.MyApplication;
import cn.ian2018.facesignin.bean.PhoneInfo;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/11
 */

public class PhoneInfoUtil {
    private static PhoneInfo phoneInfo;

    /**
     * 获取手机信息
     */
    public static PhoneInfo getPhoneInfo() {
        phoneInfo = new PhoneInfo();
        TelephonyManager tm = (TelephonyManager) MyApplication.getContext().getSystemService(TELEPHONY_SERVICE);
        String mtyb = android.os.Build.BRAND;// 手机品牌
        phoneInfo.setPhoneBrand(mtyb);
        String mtype = android.os.Build.MODEL; // 手机型号
        phoneInfo.setPhoneBrandType(mtype);
        phoneInfo.setAndroidVersion("Android " + android.os.Build.VERSION.RELEASE);

        return phoneInfo;
    }
}
