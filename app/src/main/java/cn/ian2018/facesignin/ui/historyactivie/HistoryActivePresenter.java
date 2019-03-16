package cn.ian2018.facesignin.ui.historyactivie;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.ian2018.facesignin.MyApplication;
import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.HistoryActive;
import cn.ian2018.facesignin.bean.HistorySignInfo;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import cn.ian2018.facesignin.utils.ToastUtil;
import cn.ian2018.facesignin.utils.Utils;
import rx.Subscriber;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/3/3
 */
public class HistoryActivePresenter extends BasePresenter<HistoryActiveContract.IHistoryActiveView> implements HistoryActiveContract.IHistoryActivePresenter {

    HistoryActiveContract.IHistoryActiveModel mHistoryActiveModel;
    private Context mContext;

    public HistoryActivePresenter() {
        mHistoryActiveModel = new HistoryActiveModel();
        mContext = MyApplication.getContext();
    }

    @Override
    public void refreshHistorySignInfo() {
        mHistoryActiveModel.getHistorySignInfo().subscribe(new Subscriber<HistorySignInfo>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                getView().closeRefresh();
                if (Utils.isNetworkAvalible(mContext)) {
                    ToastUtil.show(mContext.getResources().getString(R.string.quantify_get_data_fail) + e.getMessage());
                } else {
                    ToastUtil.show(mContext.getResources().getString(R.string.network_error));
                }
            }

            @Override
            public void onNext(HistorySignInfo historySignInfo) {
                getView().closeRefresh();
                if (historySignInfo.isSucessed()) {
                    List<HistorySignInfo.DataBean> data = historySignInfo.getData();
                    List<HistoryActive> historyActiveList = new ArrayList<>();
                    for (HistorySignInfo.DataBean datum : data) {
                        // 如果是完成签离的活动才展示
                        if (!datum.getInTime().equals(datum.getOutTime())) {
                            HistoryActive historyActive = new HistoryActive();
                            historyActive.sethInTime(datum.getInTime());
                            historyActive.sethOutTime(datum.getOutTime());
                            historyActive.setActivityDescription(datum.getActivityDes());
                            historyActive.sethActivityName(datum.getActivityName());
                            historyActive.sethLocation(datum.getLocation());
                            historyActive.sethTime(datum.getTime());
                            historyActive.setEndTime(datum.getEndTime());
                            historyActiveList.add(historyActive);
                        }
                    }
                    getView().resetData(historyActiveList);
                }
            }
        });
    }
}
