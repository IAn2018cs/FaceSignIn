package cn.ian2018.facesignin.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;

import java.util.concurrent.Executors;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.data.Constant;
import cn.ian2018.facesignin.ui.login.LoginActivity;
import cn.ian2018.facesignin.utils.Logs;


/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {

    private RelativeLayout sl_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        initData();

        // 初始化UI
        initUI();

        // 初始化动画
        initAnimation();
    }

    private void initData() {
        // 初始化人脸识别引擎
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FaceEngine faceEngine = new FaceEngine();
                int activeCode = faceEngine.active(SplashActivity.this, Constant.APP_ID, Constant.SDK_KEY);
                if (activeCode == ErrorInfo.MOK) {
                    Logs.d("FaceEngine 激活成功");
                } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                    Logs.d("FaceEngine 已经激活");
                } else {
                    Logs.e("FaceEngine 激活失败");
                }
            }
        });

    }

    // 初始化动画
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        sl_root.startAnimation(alphaAnimation);
        sl_root.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 进入应用程序主界面,activity跳转过程
                enterHome();
            }
        }, 2000);
    }

    // 初始化UI
    private void initUI() {
        sl_root = findViewById(R.id.sl_root);
    }


    // 进入应用
    private void enterHome() {
        LoginActivity.start(this);
        finish();
    }
}

