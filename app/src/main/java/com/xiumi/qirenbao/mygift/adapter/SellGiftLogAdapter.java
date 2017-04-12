package com.xiumi.qirenbao.mygift.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.mygift.bean.SellGiftLogBean;

import java.util.ArrayList;

/**
 * 作者 ：Created by DXR on 2017/3/21.
 * 合伙人-变现礼物记录
 */

public class SellGiftLogAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<SellGiftLogBean> selllist = new ArrayList<SellGiftLogBean>();

    public SellGiftLogAdapter(Context context, ArrayList<SellGiftLogBean> selllist) {
        this.context = context;
        this.selllist = selllist;
    }

    @Override
    public int getCount() {
        return selllist.size();
    }

    @Override
    public Object getItem(int position) {
        return selllist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SellGiftLogBean item = selllist.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sell_gift_list_item_layout,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.sell_title = (TextView) convertView.findViewById(R.id.sell_title);
            viewHolder.sell_time = (TextView) convertView.findViewById(R.id.sell_time);
            viewHolder.sell_num = (TextView) convertView.findViewById(R.id.sell_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(item.gift_lists)) {
            viewHolder.sell_title.setText(item.gift_lists);
        }

        if (!TextUtils.isEmpty(item.created_at)) {
            viewHolder.sell_time.setText(item.created_at);
        }
        if (!TextUtils.isEmpty(item.total_fee)) {
            viewHolder.sell_num.setText("+￥" + item.total_fee);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView sell_title;
        TextView sell_time;
        TextView sell_num;
    }
}
