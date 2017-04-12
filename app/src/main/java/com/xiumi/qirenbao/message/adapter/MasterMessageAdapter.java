package com.xiumi.qirenbao.message.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.message.bean.MessageBean;
import com.xiumi.qirenbao.message.expert.MessageDetailActivity;

import java.util.ArrayList;

/**
 * 作者 ：Created by DXR on 2017/3/8.
 * 达人消息 adapter
 */

public class MasterMessageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MessageBean> messagelist = new ArrayList<MessageBean>();

    public MasterMessageAdapter(Context context, ArrayList<MessageBean> messagelist) {
        this.context = context;
        this.messagelist = messagelist;
    }

    @Override
    public int getCount() {
        return messagelist.size();
    }

    @Override
    public Object getItem(int position) {
        return messagelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final MessageBean item = messagelist.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.master_message_list_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.no_read = (ImageView) convertView.findViewById(R.id.no_read);
            viewHolder.message_title = (TextView) convertView.findViewById(R.id.message_title);
            viewHolder.message_time = (TextView) convertView.findViewById(R.id.message_time);
            viewHolder.message_content = (TextView) convertView.findViewById(R.id.message_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(item.title)) {
            viewHolder.message_title.setText(item.title);
        }
        if (!TextUtils.isEmpty(item.created_at)) {
            viewHolder.message_time.setText(item.created_at);
        }
        if (!TextUtils.isEmpty(item.content)) {
            viewHolder.message_content.setText(item.content);
        }
        if (!TextUtils.isEmpty(item.is_read + "")) {
            // 待图标，完善已读未读状态 0 未读 1 已读
            if (item.is_read == 0) {
                viewHolder.no_read.setAlpha(255);
            } else if (item.is_read == 1) {
                viewHolder.no_read.setAlpha(0);
            }
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView no_read;
        TextView message_title;
        TextView message_time;
        TextView message_content;
    }
}
