package cn.ian2018.facesignin.ui.activedetail;

import android.content.Context;

import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.ui.base.BaseView;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2019/2/15
 */
public class ActiveDetailContract {
    interface ActiveDetailView extends BaseView {
        boolean isCanSign();
    }

    interface ActiveDetailPresenter {
        void initLocation(Context context);
        void signIn(Active.DataBean active, String yunziId);
    }

    interface ActiveDetailModel {

    }
}
