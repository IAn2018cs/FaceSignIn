package cn.ian2018.facesignin.ui.userhome.pager.mine;

import cn.ian2018.facesignin.bean.RepairUserInfo;
import cn.ian2018.facesignin.ui.base.BaseView;
import rx.Observable;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2019/3/16
 */
public class MineContract {
    interface IMineView extends BaseView {
        void showRepairDialog(String name, final String phone);
    }

    interface IMineModel {
        Observable<RepairUserInfo> getRepairInfo();
    }

    interface IMinePresenter {
        void repair();
    }
}
