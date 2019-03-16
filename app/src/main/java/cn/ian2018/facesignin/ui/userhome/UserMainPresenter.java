package cn.ian2018.facesignin.ui.userhome;

import android.content.Context;

import cn.ian2018.facesignin.bean.Active;
import cn.ian2018.facesignin.bean.Saying;
import cn.ian2018.facesignin.data.Constant;
import cn.ian2018.facesignin.data.SpUtil;
import cn.ian2018.facesignin.data.db.MyDatabase;
import cn.ian2018.facesignin.ui.activemove.ActiveMoveActivity;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import cn.ian2018.facesignin.utils.Logs;
import rx.Subscriber;

/**
 * Created by 陈帅 on 2019/1/17/017.
 */
public class UserMainPresenter extends BasePresenter<UserMainContract.IUserMainView> implements UserMainContract.IUserMainPresenter {

    UserMainContract.IUserMainModel mUserModel;
    private final MyDatabase mDatabase;

    public UserMainPresenter() {
        mUserModel = new UserMainModel();
        mDatabase = MyDatabase.getInstance();
    }

    @Override
    public void initSaying() {
        mUserModel.getSaying().subscribe(new Subscriber<Saying>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Logs.e("getSaying: " + e.getMessage());
            }

            @Override
            public void onNext(Saying saying) {
                int size = saying.getData().size();
                // 如果本地数据库和服务器数据不同
                if (size != mDatabase.getSaying().size()) {
                    for (Saying.DataBean datum : saying.getData()) {
                        mDatabase.saveSaying(datum);
                    }
                }
            }
        });
    }

    @Override
    public void checkUnSignOutActive(Context context) {
        // 如果sp里有数据，说明有未签离的活动，需要跳转到签离界面
        if (!SpUtil.getString(Constant.SIGN_OUT_ACTIVE_NAME,"").equals("")) {
            String activeName = SpUtil.getString(Constant.SIGN_OUT_ACTIVE_NAME, "");
            String location = SpUtil.getString(Constant.SIGN_OUT_LOCATION, "");
            int activeId = SpUtil.getInt(Constant.SIGN_OUT_ACTIVE_ID, 0);
            String endTime = SpUtil.getString(Constant.SIGN_OUT_ENDTIME, "");

            String yunziId = SpUtil.getString(Constant.SIGN_OUT_YUNZIID, "");

            Active.DataBean active = new Active.DataBean();
            active.setId(activeId);
            active.setActivityName(activeName);
            active.setLocation(location);
            active.setEndTime(endTime);

            ActiveMoveActivity.start(context, active, yunziId);
        }
    }
}
