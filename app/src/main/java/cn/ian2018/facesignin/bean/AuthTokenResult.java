package cn.ian2018.facesignin.bean;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/3/17
 */
public class AuthTokenResult {

    /**
     * data : RcFs5mqrosy08vcgB6d8G6Fucw1-Jp6tn6EWE3t2:RTogau6CdUfVbgFpyKr1Wn0eJGY=:eyJzY29wZSI6ImhidWNlbnRyZSIsImRlYWRsaW5lIjoxNTUyNzkzMDQzfQ==
     * sucessed : true
     */

    private String data;
    private boolean sucessed;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSucessed() {
        return sucessed;
    }

    public void setSucessed(boolean sucessed) {
        this.sucessed = sucessed;
    }
}
