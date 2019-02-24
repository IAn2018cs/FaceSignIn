package cn.ian2018.facesignin.bean;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/24
 */
public class SignOutResult {

    /**
     * msg : 签离成功
     * sucessed : true
     */

    private String msg;
    private boolean sucessed;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSucessed() {
        return sucessed;
    }

    public void setSucessed(boolean sucessed) {
        this.sucessed = sucessed;
    }
}
