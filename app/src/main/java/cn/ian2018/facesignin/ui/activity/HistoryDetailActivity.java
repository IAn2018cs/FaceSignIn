package cn.ian2018.facesignin.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.HistoryActive;


/**
 * 历史活动详情
 */
public class HistoryDetailActivity extends AppCompatActivity {
    private HistoryActive historyActive;

    public static void start(Context context,HistoryActive active) {
        Intent starter = new Intent(context, HistoryDetailActivity.class);
        starter.putExtra("HistoryActive", active);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        initData();

        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        historyActive = (HistoryActive) intent.getSerializableExtra("HistoryActive");
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnabled(false);

        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout);

        // 设置标题
        toolbarLayout.setTitle(historyActive.gethActivityName());

        TextView tv_details = findViewById(R.id.tv_details);
        TextView tv_hintime = findViewById(R.id.tv_hintime);
        TextView tv_houttime = findViewById(R.id.tv_houttime);
        TextView tv_hlocation = findViewById(R.id.tv_hlocation);
        TextView tv_start_time = findViewById(R.id.tv_start_time);
        TextView tv_end_time = findViewById(R.id.tv_end_time);

        tv_start_time.setText(getResources().getString(R.string.start_time_text) + historyActive.gethTime().replace("T", " ").substring(0, 16));
        tv_end_time.setText(getResources().getString(R.string.end_time_text) + historyActive.getEndTime().replace("T", " ").substring(0, 16));
        tv_details.setText("    " + historyActive.getActivityDescription());
        tv_hintime.setText(getResources().getString(R.string.sign_in_time_text)+ historyActive.gethInTime().replace("T", " "));
        tv_houttime.setText(getResources().getString(R.string.sign_out_time_text)+ historyActive.gethOutTime().replace("T", " "));
        tv_hlocation.setText(getResources().getString(R.string.active_item_location_text) + historyActive.getLocation());
    }
}
