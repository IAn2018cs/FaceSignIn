package cn.ian2018.facesignin.bean;

/**
 * Created by 陈帅 on 2019/1/17/017.
 */
public class SignInResult {
    /**
     * msg : 签到成功
     * data : 254
     * sucessed : true
     */

    private String msg;
    private int data;
    private boolean sucessed;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public boolean isSucessed() {
        return sucessed;
    }

    public void setSucessed(boolean sucessed) {
        this.sucessed = sucessed;
    }
}
