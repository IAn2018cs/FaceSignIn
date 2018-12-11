package cn.ian2018.facesignin.network.retrofit;

public class BaseResult {
    String bol;
    String msg;

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return bol;
    }

    public boolean isOk(){
        return "true".equals(bol);
    }
}
