package cn.ian2018.facesignin.ui.userhome.pager.quantify;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.AnalyzeSign;
import cn.ian2018.facesignin.bean.HistorySignInfo;
import cn.ian2018.facesignin.data.Constant;
import cn.ian2018.facesignin.data.SpUtil;
import cn.ian2018.facesignin.data.db.MyDatabase;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import cn.ian2018.facesignin.utils.ToastUtil;
import cn.ian2018.facesignin.utils.Utils;
import rorbin.q.radarview.RadarData;
import rx.Subscriber;

import static cn.ian2018.facesignin.MyApplication.getContext;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/3/3
 */
public class QuantifyPresenter extends BasePresenter<QuantifyContract.IQuantifyView> implements QuantifyContract.IQuantifyPresenter {

    private MyDatabase mDatabase;
    private Context mContext;
    QuantifyContract.IQuantifyModel mQuantifyModel;
    public boolean noData = true;

    public QuantifyPresenter() {
        mQuantifyModel = new QuantifyModel();
        mDatabase = MyDatabase.getInstance();
        mContext = getContext();
    }

    @Override
    public void refreshHistorySignInfo() {
        mQuantifyModel.getHistorySignInfo().subscribe(new Subscriber<HistorySignInfo>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                noData = true;
                getView().closeRefresh();
                getView().showEmptyRadarData();
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
                    noData = false;
                    mDatabase.deleteAnalyzeSign();
                    List<HistorySignInfo.DataBean> data = historySignInfo.getData();
                    for (HistorySignInfo.DataBean datum : data) {
                        if (!datum.getInTime().equals(datum.getOutTime())) {
                            AnalyzeSign analyzeSign = new AnalyzeSign();
                            analyzeSign.setNumber(datum.getStudentNum());
                            analyzeSign.setActiveName(datum.getActivityName());
                            analyzeSign.setRule(datum.getRule());
                            analyzeSign.setInTime(datum.getInTime());
                            analyzeSign.setOutTime(datum.getOutTime());
                            analyzeSign.setTime(datum.getTime());
                            analyzeSign.setEndTime(datum.getEndTime());
                            mDatabase.saveAnalyzeSign(analyzeSign);
                        }
                    }
                    analyzeData();
                } else {
                    noData = true;
                    getView().setDesText(mContext.getResources().getString(R.string.quantify_no_sign_data));
                    getView().showEmptyRadarData();
                }
            }
        });
    }

    private void analyzeData() {
        // 获取晨读签到次数(毅力)
        int morningFrequency = mDatabase.getMorningFrequency(SpUtil.getString(Constant.ACCOUNT, ""));
        // 获取活动和培训的签到次数(学识)
        int lectureFrequency = mDatabase.getLectureFrequency(SpUtil.getString(Constant.ACCOUNT, ""));
        // 获取准时签到的次数(守时)
        int earlyFrequency = mDatabase.getEarlyFrequency(SpUtil.getString(Constant.ACCOUNT, ""));
        // 获取值班的签到次数(自律)
        int onDutyFrequency = mDatabase.getOnDutyFrequency(SpUtil.getString(Constant.ACCOUNT, ""));
        // 获取跑操签到次数(活力)
        int runningFrequency = mDatabase.getRunningFrequency(SpUtil.getString(Constant.ACCOUNT, ""));

        List<Float> values = new ArrayList<>();
        Collections.addAll(values, (float) morningFrequency, (float) lectureFrequency, (float) earlyFrequency, (float)onDutyFrequency, (float)runningFrequency);
        RadarData data = new RadarData(values);

        getView().showRadarData(data);

        if (lectureFrequency == 0) {
            getView().setDesText(mContext.getResources().getString(R.string.quantify_no_sign_data));
        } else {
            getView().setDesText(mContext.getResources().getString(R.string.quantify_sign_times1) + lectureFrequency +
                    mContext.getResources().getString(R.string.quantify_sign_times2));
        }
    }
}
