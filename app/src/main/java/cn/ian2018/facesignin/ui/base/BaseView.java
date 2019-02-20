package cn.ian2018.facesignin.ui.base;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/12
 */
public interface BaseView {
    void showProgressDialog(int msg);
    void closeProgressDialog();
    void showToast(int stringId);
    void showToast(String string);
}
