package com.xiumi.qirenbao.mygift.adapter;

import android.content.Context;
import android.sax.TextElementListener;
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
 * 作者 ：Created by DXR on 2017/3/20.
 * 达人-我的礼物adapter
 */

public class MasterGiftAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MasterGiftBean> giftlist = new ArrayList<MasterGiftBean>();

    public MasterGiftAdapter(Context context, ArrayList<MasterGiftBean> giftlist) {
        this.context = context;
        this.giftlist = giftlist;
    }

    @Override
    public int getCount() {
        return giftlist.size();
    }

    @Override
    public Object getItem(int position) {
        return giftlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MasterGiftBean item = giftlist.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.master_gift_list_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.reward_name = (TextView) convertView.findViewById(R.id.reward_name);
            viewHolder.reward_sum = (TextView) convertView.findViewById(R.id.reward_sum);
            viewHolder.reward_time = (TextView) convertView.findViewById(R.id.reward_time);
            viewHolder.reward_type = (TextView) convertView.findViewById(R.id.reward_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (item.role_type.equals("NOTTEL")) {
            viewHolder.reward_type.setText("我打赏给团长");
        }else if (item.role_type.equals("TEL")) {
            viewHolder.reward_type.setText("我打赏给客服");
        }
        if (item.gift != null) {
            if (!TextUtils.isEmpty(item.gift.name)) {
                viewHolder.reward_name.setText(item.gift.name);
            }
        }
        if (!TextUtils.isEmpty(item.total)) {
            viewHolder.reward_sum.setText(item.total + "个");
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
        TextView reward_type;
    }
}
