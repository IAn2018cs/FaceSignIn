package cn.ian2018.facesignin.ui.userhome;

import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.TabItem;
import cn.ian2018.facesignin.event.ExitEvent;
import cn.ian2018.facesignin.service.SensorService;
import cn.ian2018.facesignin.ui.base.BaseActivity;
import cn.ian2018.facesignin.ui.userhome.pager.active.ActiveFragment;
import cn.ian2018.facesignin.ui.userhome.pager.mine.MineFragment;
import cn.ian2018.facesignin.ui.userhome.pager.quantify.QuantifyFragment;
import cn.ian2018.facesignin.utils.CheckVersionHelper;
import cn.ian2018.facesignin.utils.StatusBarUtils;
import cn.ian2018.facesignin.utils.ToastUtil;

/**
 * 普通用户主页
 * Created by 陈帅 on 2019/1/17/017.
 */
public class UserMainActivity extends BaseActivity<UserMainPresenter> implements UserMainContract.UserMainView {

    private ViewPager viewPager;
    private BottomBar bottomBar;
    private Toolbar toolbar;
    private List<TabItem> tabs;

    private boolean isExit;

    public static void start(Context context) {
        Intent starter = new Intent(context, UserMainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_main);
    }

    @Override
    protected UserMainPresenter createPresenter() {
        return new UserMainPresenter();
    }

    @Override
    protected void initData() {
        // 注册监听退出登录的事件
        EventBus.getDefault().register(this);

        getPresenter().checkUnSignOutActive(this);

        // 检查版本更新
        CheckVersionHelper checkVersionHelper = new CheckVersionHelper(this);
        checkVersionHelper.checkVersionCode();
        // 初始化名言信息
        getPresenter().initSaying();
    }

    @Override
    protected void initView() {
        viewPager =  findViewById(R.id.viewPager_top);
        bottomBar =  findViewById(R.id.bottomBar);

        toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.bottom_bar_title_sign));
        toolbar.setBackgroundColor(getResources().getColor(R.color.bottom_bar_color_sign));
        setSupportActionBar(toolbar);

        tabs = new ArrayList<>();
        tabs.add(new TabItem(R.mipmap.ic_assignment_turned_in, R.string.bottom_bar_title_sign, ActiveFragment.class));
        tabs.add(new TabItem(R.mipmap.ic_assignment, R.string.bottom_bar_title_quantify, QuantifyFragment.class));
        tabs.add(new TabItem(R.mipmap.ic_assignment_ind, R.string.bottom_bar_title_me, MineFragment.class));
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_active:
                        viewPager.setCurrentItem(0);
                        changeColorAndTitle(R.string.bottom_bar_title_sign, R.color.bottom_bar_color_sign);
                        break;
                    case R.id.tab_quantify:
                        viewPager.setCurrentItem(1);
                        changeColorAndTitle(R.string.bottom_bar_title_quantify, R.color.bottom_bar_color_quantify);
                        break;
                    case R.id.tab_mine:
                        viewPager.setCurrentItem(2);
                        changeColorAndTitle(R.string.bottom_bar_title_me, R.color.bottom_bar_color_me);
                        break;
                }
            }
        });

        final FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.selectTabAtPosition(position,true);
                switch (position) {
                    case 0:
                        changeColorAndTitle(R.string.bottom_bar_title_sign, R.color.bottom_bar_color_sign);
                        break;
                    case 1:
                        changeColorAndTitle(R.string.bottom_bar_title_quantify, R.color.bottom_bar_color_quantify);
                        break;
                    case 2:
                        changeColorAndTitle(R.string.bottom_bar_title_me, R.color.bottom_bar_color_me);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    // 修改标题和颜色
    private void changeColorAndTitle(int titleId, int colorId) {
        toolbar.setTitle(getString(titleId));
        toolbar.setBackgroundColor(getResources().getColor(colorId));
        StatusBarUtils.setWindowStatusBarColor(UserMainActivity.this, colorId);
    }



    // 接收退出登录的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ExitEvent event) {
        finish();
    }

    // viewpager适配器
    class FragmentAdapter extends FragmentPagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            try {
                return tabs.get(position).tagFragmentClz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabs.size();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopService(new Intent(this, SensorService.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            exitBy2Click();
        }
        return false;
    }

    // 双击退出程序
    private void exitBy2Click() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true; // 准备退出
            ToastUtil.show(R.string.again_to_return);
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }
}
