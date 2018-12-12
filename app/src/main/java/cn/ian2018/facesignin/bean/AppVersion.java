package cn.ian2018.facesignin.bean;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/12
 */
public class AppVersion {

    /**
     * data : {"appVersion":2,"appDescribe":"优化检测活动功能，美化动画效果，推荐立刻更新。","appUrl":"http://p40xgrp9e.bkt.clouddn.com/apk/app-debug.apk","showScansSensor":0}
     * falg : true
     */

    private DataBean data;
    private boolean falg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isFalg() {
        return falg;
    }

    public void setFalg(boolean falg) {
        this.falg = falg;
    }

    public static class DataBean {
        /**
         * appVersion : 2
         * appDescribe : 优化检测活动功能，美化动画效果，推荐立刻更新。
         * appUrl : http://p40xgrp9e.bkt.clouddn.com/apk/app-debug.apk
         * showScansSensor : 0
         */

        private int appVersion;
        private String appDescribe;
        private String appUrl;
        private int showScansSensor;

        public int getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(int appVersion) {
            this.appVersion = appVersion;
        }

        public String getAppDescribe() {
            return appDescribe;
        }

        public void setAppDescribe(String appDescribe) {
            this.appDescribe = appDescribe;
        }

        public String getAppUrl() {
            return appUrl;
        }

        public void setAppUrl(String appUrl) {
            this.appUrl = appUrl;
        }

        public int getShowScansSensor() {
            return showScansSensor;
        }

        public void setShowScansSensor(int showScansSensor) {
            this.showScansSensor = showScansSensor;
        }
    }
}
