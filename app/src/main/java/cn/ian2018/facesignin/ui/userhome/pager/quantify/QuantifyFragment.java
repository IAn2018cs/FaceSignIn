package cn.ian2018.facesignin.ui.userhome.pager.quantify;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ian2018.facesignin.R;
import cn.ian2018.facesignin.ui.base.BaseFragment;
import cn.ian2018.facesignin.ui.historyactivie.HistoryActiveActivity;
import cn.ian2018.facesignin.utils.ToastUtil;
import rorbin.q.radarview.RadarData;
import rorbin.q.radarview.RadarView;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/2/5
 */
public class QuantifyFragment extends BaseFragment<QuantifyPresenter> implements QuantifyContract.QuantifyView,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private SwipeRefreshLayout mSwipeLayout;
    private RadarView mRadarView;
    private TextView mDesTv;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_quantify, container, false);
    }

    @Override
    protected void initView(View view) {
        mSwipeLayout = view.findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeLayout.setOnRefreshListener(this);

        mDesTv = view.findViewById(R.id.tv_des);

        mRadarView = view.findViewById(R.id.radarView);
        mRadarView.setVertexTextSize(getResources().getDimension(R.dimen.radar_text_size));
        mRadarView.setEmptyHint(getResources().getString(R.string.quantify_loading_text));

        // 设置线条颜色
        List<Integer> layerColor = new ArrayList<>();
        Collections.addAll(layerColor, 0x3300bcd4, 0x3303a9f4, 0x335677fc, 0x333f51b5, 0x33673ab7);
        mRadarView.setLayerColor(layerColor);

        // 设置查看详细签到数据点击事件
        TextView goDetailTv = view.findViewById(R.id.tv_detail);
        goDetailTv.setOnClickListener(this);

        // 设置查看排名点击事件
        TextView goRankInfoTv = view.findViewById(R.id.tv_rank);
        goRankInfoTv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mSwipeLayout.setRefreshing(true);
        getPresenter().refreshHistorySignInfo();
    }

    @Override
    protected QuantifyPresenter createPresenter() {
        return new QuantifyPresenter();
    }

    @Override
    public void showRadarData(RadarData data) {
        List<String> vertexText = new ArrayList<>();
        String[] stringArray = getResources().getStringArray(R.array.quantify_string_list);
        Collections.addAll(vertexText, stringArray[0], stringArray[1], stringArray[2], stringArray[3], stringArray[4]);
        mRadarView.setVertexText(vertexText);

        mRadarView.addData(data);

        mRadarView.animeValue(2000);
    }

    @Override
    public void showEmptyRadarData() {
        mRadarView.setEmptyHint(getResources().getString(R.string.quantify_no_data));
    }

    @Override
    public void closeRefresh() {
        if (mSwipeLayout != null) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void setDesText(String des) {
        if (mDesTv != null) {
            mDesTv.setText(des);
        }
    }

    @Override
    public void onRefresh() {
        getPresenter().refreshHistorySignInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_detail:
                if (!getPresenter().noData) {
                    HistoryActiveActivity.start(getContext());
                } else {
                    ToastUtil.show(R.string.no_history_data);
                }
                break;
            case R.id.tv_rank:
                break;
        }
    }
}
