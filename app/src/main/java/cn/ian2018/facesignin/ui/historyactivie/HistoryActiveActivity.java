package cn.ian2018.facesignin.ui.historyactivie;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.bean.HistoryActive;
import cn.ian2018.facesignin.ui.activity.HistoryDetailActivity;
import cn.ian2018.facesignin.ui.base.BaseActivity;
import cn.ian2018.facesignin.ui.base.BaseListViewAdapter;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/3/3
 */
public class HistoryActiveActivity extends BaseActivity<HistoryActivePresenter> implements HistoryActiveContract.HistoryActiveView,
        SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private SwipeRefreshLayout mSwipeLayout;
    private MyAdapter mAdapter;
    private List<HistoryActive> mActiveList = new ArrayList<>();

    public static void start(Context context) {
        Intent starter = new Intent(context, HistoryActiveActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected HistoryActivePresenter createPresenter() {
        return new HistoryActivePresenter();
    }

    @Override
    protected void initData() {
        mSwipeLayout.setRefreshing(true);
        getPresenter().refreshHistorySignInfo();
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.history_active_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ListView listView = findViewById(R.id.lv_history);
        mSwipeLayout = findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeLayout.setOnRefreshListener(this);

        mAdapter = new MyAdapter();
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_history_active);
    }

    @Override
    public void onRefresh() {
        getPresenter().refreshHistorySignInfo();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HistoryActive historyActive = mActiveList.get(position);
        HistoryDetailActivity.start(this,historyActive);
    }

    @Override
    public void closeRefresh() {
        if (mSwipeLayout != null) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void resetData(List<HistoryActive> list) {
        mActiveList = list;
        mAdapter.notifyDataSetChanged();
    }

    class MyAdapter extends BaseListViewAdapter<MyAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(HistoryActiveActivity.this).inflate(R.layout.item_active_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(int position, ViewHolder holder) {
            holder.tv_name.setText(getItem(position).gethActivityName());
            holder.tv_location.setText(getItem(position).getLocation().replace("T", " "));
            holder.tv_time.setText(getItem(position).gethInTime().replace("T", " ").substring(0, 16));
        }

        @Override
        public int getCount() {
            return mActiveList.size();
        }

        @Override
        public HistoryActive getItem(int position) {
            return mActiveList.get(position);
        }

        class ViewHolder extends BaseListViewAdapter.ViewHolder {
            TextView tv_name;
            TextView tv_location;
            TextView tv_time;
            public ViewHolder(View convertView) {
                super(convertView);
                tv_name = convertView.findViewById(R.id.tv_name);
                tv_location = convertView.findViewById(R.id.tv_location);
                tv_time = convertView.findViewById(R.id.tv_time);
            }
        }
    }
}
