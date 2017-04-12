package com.xiumi.qirenbao.team.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.order.bean.PartnerOrderListBean;

import java.util.ArrayList;

/**
 * Created by qianbailu on 2017/3/10.
 * 团队订单
 */

public class TeamOrderAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PartnerOrderListBean> orderlist = new ArrayList<PartnerOrderListBean>();
    private int isClose;
    public TeamOrderAdapter(Context context, ArrayList<PartnerOrderListBean> orderlist, int isClose) {
        this.context = context;
        this.orderlist = orderlist;
        this.isClose=isClose;
    }

    @Override
    public int getCount() {
        return orderlist.size();
    }

    @Override
    public Object getItem(int position) {
        return orderlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final PartnerOrderListBean item = orderlist.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.team_order_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.order_title = (TextView) convertView.findViewById(R.id.order_title);
            viewHolder.order_status = (TextView) convertView.findViewById(R.id.order_status);
            viewHolder.order_content = (TextView) convertView.findViewById(R.id.order_content);
            viewHolder.order_time = (TextView) convertView.findViewById(R.id.order_time);
            viewHolder.left = (LinearLayout) convertView.findViewById(R.id.left);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 赋值
        if (!TextUtils.isEmpty(item.created_at)) {
            viewHolder.order_time.setText(item.created_at);
        }
        viewHolder.order_content.setText(item.name);
        viewHolder.order_title.setText("订单"+item.id);
        if(isClose==0){
            viewHolder.order_status.setText("进行中");
        }else{
            viewHolder.order_status.setText("已完成");
            viewHolder.left.setBackgroundColor(context.getResources().getColor(R.color.shadow_black));
        }
        return convertView;
    }

    private class ViewHolder {
        TextView order_title;
        TextView order_status;
        TextView order_content;
        TextView order_time;
        LinearLayout left;
    }
}