package com.xiumi.qirenbao.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.order.bean.PartnerOrderListBean;
import com.xiumi.qirenbao.order.partnership.DistributionRecordActivity;

import java.util.ArrayList;

/**
 * 作者 ：Created by DXR on 2017/3/18.
 * 已完结订单adapter
 */

public class OverOrderListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PartnerOrderListBean> overlist = new ArrayList<PartnerOrderListBean>();

    public OverOrderListAdapter(Context context, ArrayList<PartnerOrderListBean> orderlist) {
        this.context = context;
        this.overlist = orderlist;
    }

    @Override
    public int getCount() {
        return overlist.size();
    }

    @Override
    public Object getItem(int position) {
        return overlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PartnerOrderListBean item = overlist.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.over_order_list_item_new_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.out_title = (TextView) convertView.findViewById(R.id.out_title);
            viewHolder.out_time = (TextView) convertView.findViewById(R.id.out_time);
            viewHolder.master_name = (TextView) convertView.findViewById(R.id.master_name);
            viewHolder.out_service = (TextView) convertView.findViewById(R.id.out_service);
            viewHolder.over_company = (TextView) convertView.findViewById(R.id.over_company);
            viewHolder.record_log = (LinearLayout) convertView.findViewById(R.id.record_log);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 赋值
        if (!TextUtils.isEmpty(item.created_at)) {
            viewHolder.out_time.setText(item.created_at);
        }
        // 订单标题
        if (!TextUtils.isEmpty(item.name)) {
            viewHolder.out_title.setText(item.name);
        }
        // 跟进人
        if (item.service_user != null) {
            if (!TextUtils.isEmpty(item.service_user.name)) {
                viewHolder.master_name.setText(item.service_user.name);
            }
        } else {
            viewHolder.master_name.setText("暂无");
        }
        // 客服
        if (item.tel_service_user != null) {
            if (!TextUtils.isEmpty(item.tel_service_user.name)) {
                viewHolder.out_service.setText(item.tel_service_user.name);
            }
        } else {
            viewHolder.out_service.setText("暂无");
        }
        // 企业主
        if (item.company_user != null) {
            if (!TextUtils.isEmpty(item.company_user.name)) {
                viewHolder.over_company.setText(item.company_user.name);
            }
        }
        // 分配记录
        if (item.order_log != null) {
            if (item.order_log.size() > 0) {
                final ArrayList<String> name = new ArrayList<>();
                final ArrayList<String> time = new ArrayList<>();
                for (int i = 0; i < item.order_log.size(); i++) {
                    if (item.order_log.get(i).to_user != null) {
                        name.add(item.order_log.get(i).to_user.name);
                        time.add(item.order_log.get(i).created_at);
                    }
                }
                viewHolder.record_log.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 查看分配记录
                        Intent intent = new Intent(context, DistributionRecordActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("time", time);
                        intent.putExtra("title", item.name);
                        context.startActivity(intent);
                    }

                });
            }
        }
        return convertView;
    }

    // 各个布局的控件资源
    private class ViewHolder {

        TextView out_title;
        TextView out_time;
        TextView master_name;
        TextView out_service;
        TextView over_company;
        LinearLayout record_log; // 分配记录
    }
}
