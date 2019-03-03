package cn.ian2018.facesignin.ui.userhome.pager.quantify;

import cn.ian2018.facesignin.bean.HistorySignInfo;
import cn.ian2018.facesignin.ui.base.BaseView;
import rorbin.q.radarview.RadarData;
import rx.Observable;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/3/3
 */
public class QuantifyContract {
    interface QuantifyView extends BaseView {
        void showRadarData(RadarData data);
        void showEmptyRadarData();
        void closeRefresh();
        void setDesText(String des);
    }

    interface QuantifyModel {
        Observable<HistorySignInfo> getHistorySignInfo(String account);
    }

    interface QuantifyPresenter {
        void refreshHistorySignInfo();
    }
}
