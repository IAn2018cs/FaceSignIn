package cn.ian2018.facesignin.ui.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@ian2018.cn
 * Date:2019/3/3
 */
public abstract class BaseListViewAdapter<VH extends BaseListViewAdapter.ViewHolder> extends BaseAdapter {

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH holder;
        if (convertView == null) {
            holder = onCreateViewHolder(parent);
            convertView = holder.convertView;
            convertView.setTag(holder);
        } else {
            holder = (VH) convertView.getTag();
        }
        onBindViewHolder(position,holder);
        return holder.convertView;
    }

    public class ViewHolder {
        View convertView;
        public ViewHolder(View convertView) {
            this.convertView = convertView;
        }
    }

    public abstract VH onCreateViewHolder(ViewGroup parent);

    public abstract void onBindViewHolder(int position, VH holder);
}
