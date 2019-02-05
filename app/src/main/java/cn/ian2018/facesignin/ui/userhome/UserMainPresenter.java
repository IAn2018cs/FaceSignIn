package cn.ian2018.facesignin.ui.userhome;

import cn.ian2018.facesignin.bean.Saying;
import cn.ian2018.facesignin.data.db.MyDatabase;
import cn.ian2018.facesignin.ui.base.BasePresenter;
import cn.ian2018.facesignin.utils.Logs;
import rx.Subscriber;

/**
 * Created by 陈帅 on 2019/1/17/017.
 */
public class UserMainPresenter extends BasePresenter<UserMainContract.UserMainView> implements UserMainContract.UserMainPresenter {

    UserMainContract.UserMainModel mUserModel;
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
}
