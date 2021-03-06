package cn.ian2018.facesignin.ui.userhome.pager.active;

import android.content.Intent;

import com.sensoro.cloud.SensoroManager;

import java.util.List;

import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.ui.base.BaseView;
import rx.Observable;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/6
 */
public class ActiveContract {

    interface IActiveView extends BaseView {
        void showScanAnim();
        void closeScanAnim();
        void updateActiveList(List<Active.DataBean> actives);
        void getActiveSuccess();
        void getActiveFail();
        void hideScanText();
    }

    interface IActiveModel {
        Observable<Active> getActive(String sensoroId);
    }

    interface IActivePresenter {
        void getActiveForNetwork(String sensoroId, boolean isScan);
        void checkSensor();
        void onActivityResult(int requestCode, Intent data, SensoroManager sensoroManager);
    }
}
