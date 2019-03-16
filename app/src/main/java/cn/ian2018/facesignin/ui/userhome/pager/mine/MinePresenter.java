package cn.ian2018.facesignin.ui.userhome.pager.mine;

import android.util.Log;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.RepairUserInfo;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import rx.Subscriber;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2019/3/16
 */
public class MinePresenter extends BasePresenter<MineContract.IMineView> implements MineContract.IMinePresenter {

    MineContract.IMineModel mMineModel;
    private String TAG = "MinePresenter";

    public MinePresenter() {
        mMineModel = new MineModel();
    }

    @Override
    public void repair() {
        getView().showProgressDialog(R.string.mine_repair_loading_msg);
        mMineModel.getRepairInfo().subscribe(new Subscriber<RepairUserInfo>() {
            @Override
            public void onCompleted() {
                getView().closeProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                getView().showToast(R.string.mine_repair_loading_fail);
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(RepairUserInfo repairUserInfo) {
                if (repairUserInfo.isSucessed()) {
                    getView().showRepairDialog(repairUserInfo.getData().getName(), repairUserInfo.getData().getPhone());
                } else {
                    getView().showToast(R.string.mine_repair_loading_fail);
                    Log.e(TAG, "onNext:  没有信息");
                }
            }
        });
    }
}
