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
 * 合伙人-收到的打赏
 */

public class PartnerReceiveGiftAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<MasterGiftBean> receivelist = new ArrayList<MasterGiftBean>();

    public PartnerReceiveGiftAdapter(Context context, ArrayList<MasterGiftBean> receivelist) {
        this.context = context;
        this.receivelist = receivelist;
    }

    @Override
    public int getCount() {
        return receivelist.size();
    }

    @Override
    public Object getItem(int position) {
        return receivelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MasterGiftBean item = receivelist.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.master_gift_list_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.reward_name = (TextView) convertView.findViewById(R.id.reward_name);
            viewHolder.reward_sum = (TextView) convertView.findViewById(R.id.reward_sum);
            viewHolder.reward_time = (TextView) convertView.findViewById(R.id.reward_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (item.gift != null) {
            if (!TextUtils.isEmpty(item.gift.name)) {
                viewHolder.reward_name.setText(item.gift.name);
            }
        }
        if (!TextUtils.isEmpty(item.total)) {
            viewHolder.reward_sum.setText("我收到" + item.total + "个");
        }
        if (!TextUtils.isEmpty(item.created_at)) {
            viewHolder.reward_time.setText(item.created_at);
        }
        return convertView;
    }
    private class ViewHolder {
        TextView reward_name;
        TextView reward_sum;
        TextView reward_time;
    }

}
