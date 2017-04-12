package com.xiumi.qirenbao.mygift.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.mygift.bean.MasterGiftBean;

import java.util.ArrayList;

/**
 * 作者 ：Created by DXR on 2017/3/21.
 * 合伙人送出的礼物
 */

public class PartnerSendGiftAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<MasterGiftBean> sendlist = new ArrayList<MasterGiftBean>();

    public PartnerSendGiftAdapter(Context context, ArrayList<MasterGiftBean> sendlist) {
        this.context = context;
        this.sendlist = sendlist;
    }

    @Override
    public int getCount() {
        return sendlist.size();
    }

    @Override
    public Object getItem(int position) {
        return sendlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MasterGiftBean item = sendlist.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.send_gift_list_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.send_name = (TextView) convertView.findViewById(R.id.send_name);
            viewHolder.send_sum = (TextView) convertView.findViewById(R.id.send_sum);
            viewHolder.send_time = (TextView) convertView.findViewById(R.id.send_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (item.gift != null) {
            if (!TextUtils.isEmpty(item.gift.name)) {
                viewHolder.send_name.setText(item.gift.name);
            }
        }
        if (!TextUtils.isEmpty(item.total)) {
            viewHolder.send_sum.setText("我送出" + item.total + "个");
        }
        if (!TextUtils.isEmpty(item.created_at)) {
            viewHolder.send_time.setText(item.created_at);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView send_name;
        TextView send_sum;
        TextView send_time;
    }
}
