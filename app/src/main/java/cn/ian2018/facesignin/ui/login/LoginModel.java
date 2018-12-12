package cn.ian2018.facesignin.ui.login;

import cn.ian2018.facesignin.bean.User;
import cn.ian2018.facesignin.network.retrofit.RetrofitClient;
import rx.Observable;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/12
 */
public class LoginModel implements LoginContract.LoginModel{

    @Override
    public Observable<User> login(String account, String password) {
        return RetrofitClient.getServiceApi()
                .login(account,password)
                .compose(RetrofitClient.<User>transformer());
    }
}
