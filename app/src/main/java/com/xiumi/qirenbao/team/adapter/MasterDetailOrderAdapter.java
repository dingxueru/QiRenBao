package com.xiumi.qirenbao.team.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.order.bean.PartnerOrderListBean;
import com.xiumi.qirenbao.utils.StringUtils;

import java.util.ArrayList;

/**
 * 作者 ：Created by DXR on 2017/3/20.
 * 达人-团员详情-订单
 */

public class MasterDetailOrderAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PartnerOrderListBean> orderlist = new ArrayList<PartnerOrderListBean>();

    public MasterDetailOrderAdapter(Context context, ArrayList<PartnerOrderListBean> orderlist) {
        this.context = context;
        this.orderlist = orderlist;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.member_order_list_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.order_title = (TextView) convertView.findViewById(R.id.order_title);
            viewHolder.order_status = (TextView) convertView.findViewById(R.id.order_status);
            viewHolder.order_content = (TextView) convertView.findViewById(R.id.order_content);
            viewHolder.order_time = (TextView) convertView.findViewById(R.id.order_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 赋值
        if (!TextUtils.isEmpty(item.created_at)) {
            viewHolder.order_time.setText(item.created_at);
        }
        if (!TextUtils.isEmpty(item.status + "")) {
            if (item.status == 1) {
                viewHolder.order_status.setText("待处理");
            } else if (item.status == 2) {
                viewHolder.order_status.setText("待打赏");
            } else if (item.status == 3) {
                viewHolder.order_status.setText("已拒绝");
            } else if (item.status == 4) {
                viewHolder.order_status.setText("待提交");
                if (StringUtils.isEmpty(item.order_comment.p2t_labels) || StringUtils.isEmpty(item.order_comment.p2c_labels)) {
                    viewHolder.order_status.setText("待评价");
                } else {
                    viewHolder.order_status.setText("已完成");
                }
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView order_title;
        TextView order_status;
        TextView order_content;
        TextView order_time;
    }
}
