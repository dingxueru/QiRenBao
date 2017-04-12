package com.xiumi.qirenbao.order.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.order.bean.PartnerOrderListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者 ：Created by DXR on 2017/3/30.
 * 分配记录 adapter
 */

public class DistributionRecordAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private String title;

    public DistributionRecordAdapter(Context context, ArrayList<String> name, ArrayList<String> time, String title) {
        this.context = context;
        this.name = name;
        this.time = time;
        this.title = title;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String record_name = name.get(position);
        String record_time = time.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.distribution_record_list_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.record_name = (TextView) convertView.findViewById(R.id.record_name);
            viewHolder.record_time = (TextView) convertView.findViewById(R.id.record_time);
            viewHolder.order_title = (TextView) convertView.findViewById(R.id.order_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(record_name)) {
            viewHolder.record_name.setText(record_name);
        }
        if (!TextUtils.isEmpty(record_time)) {
            viewHolder.record_time.setText(record_time);
        }
        if (!TextUtils.isEmpty(title)) {
            viewHolder.order_title.setText(title);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView record_name;
        TextView record_time;
        TextView order_title;
    }
}
