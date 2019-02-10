package cn.ian2018.facesignin.event;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/10
 */
public class SensorShow {
    private String yunziId;

    public SensorShow(String yunziId) {
        this.yunziId = yunziId;
    }

    public String getYunziId() {
        return yunziId;
    }

    public void setYunziId(String yunziId) {
        this.yunziId = yunziId;
    }
}
