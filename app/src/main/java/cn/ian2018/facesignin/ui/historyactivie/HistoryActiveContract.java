package cn.ian2018.facesignin.ui.historyactivie;

import java.util.List;

import cn.ian2018.facesignin.bean.HistoryActive;
import cn.ian2018.facesignin.bean.HistorySignInfo;
import cn.ian2018.facesignin.ui.base.BaseView;
import rx.Observable;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/3/3
 */
public class HistoryActiveContract {
    interface IHistoryActiveView extends BaseView {
        void closeRefresh();
        void resetData(List<HistoryActive> list);
    }

    interface IHistoryActiveModel {
        Observable<HistorySignInfo> getHistorySignInfo();
    }

    interface IHistoryActivePresenter {
        void refreshHistorySignInfo();
    }
}
