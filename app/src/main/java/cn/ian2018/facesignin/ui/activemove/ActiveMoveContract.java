package cn.ian2018.facesignin.ui.activemove;

import android.content.Context;

import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.bean.SignOutResult;
import cn.ian2018.facesignin.ui.base.BaseView;
import rx.Observable;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/21
 */
public class ActiveMoveContract {
    interface IActiveMoveView extends BaseView {
        void updateInTimeText(String time);
        void updateTotalTimeText(String time);
        boolean isCanSignOut();
        void finishActivity();
    }

    interface IActiveMoveModel {
        Observable<SignOutResult> signOutResult(int activeId, String outTime, String location);
    }

    interface IActiveMovePresenter {
        void updateTime();
        void saveUnSignOutData(Active.DataBean dataBean, String yunziId);
        void signOutClick();
        void uploadSignOutInfo(String outTime);
        void initLocation(Context context);
        void checkSignOut();
    }
}
