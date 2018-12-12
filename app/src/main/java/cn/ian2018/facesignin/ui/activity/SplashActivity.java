package cn.ian2018.facesignin.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.ui.login.LoginActivity;


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

        // 初始化UI
        initUI();

        // 初始化动画
        initAnimation();

        // 开启一个子线程
        newThread();
    }

    // 初始化动画
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2500);
        sl_root.startAnimation(alphaAnimation);
    }

    // 初始化UI
    private void initUI() {
        sl_root = (RelativeLayout) findViewById(R.id.sl_root);
    }

    // 开启一个子线程
    private void newThread() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2600);
                } catch (InterruptedException e)   {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 进入应用程序主界面,activity跳转过程
                        enterHome();
                    }
                });
            }
        }.start();
    }

    // 进入应用
    private void enterHome() {
        LoginActivity.start(this);
        finish();
    }
}

