package com.xiumi.qirenbao.mygift.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.mygift.bean.GrowthLogBean;

import java.util.ArrayList;

/**
 * 作者 ：Created by DXR on 2017/3/21.
 * 经验增长记录
 */

public class GrowthLogAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<GrowthLogBean> growthlist = new ArrayList<GrowthLogBean>();

    public GrowthLogAdapter(Context context, ArrayList<GrowthLogBean> growthlist) {
        this.context = context;
        this.growthlist = growthlist;
    }

    @Override
    public int getCount() {
        return growthlist.size();
    }

    @Override
    public Object getItem(int position) {
        return growthlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final GrowthLogBean item = growthlist.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.growth_log_list_item_layout,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.reward_name = (TextView) convertView.findViewById(R.id.reward_name);
            viewHolder.reward_value = (TextView) convertView.findViewById(R.id.reward_value);
            viewHolder.reward_time = (TextView) convertView.findViewById(R.id.reward_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(item.title)) {
            viewHolder.reward_name.setText(item.title);
        }

        if (!TextUtils.isEmpty(item.growths_value_change)) {
            viewHolder.reward_value.setText( "+" + item.growths_value_change + " 经验值");
        }

        if (!TextUtils.isEmpty(item.created_at)) {
            viewHolder.reward_time.setText(item.created_at);
        }
        return convertView;
    }
    private class ViewHolder {
        TextView reward_name;
        TextView reward_value;
        TextView reward_time;
    }

}
