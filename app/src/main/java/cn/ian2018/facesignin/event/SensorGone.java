package cn.ian2018.facesignin.event;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2019/2/19
 */
public class SensorGone {
    private String yunziId;

    public SensorGone(String yunziId) {
        this.yunziId = yunziId;
    }

    public String getYunziId() {
        return yunziId;
    }

    public void setYunziId(String yunziId) {
        this.yunziId = yunziId;
    }
}
