package com.xiumi.qirenbao.mygift.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.mygift.bean.ReceiveGiftsCountBean;
import com.xiumi.qirenbao.mygift.partnership.PartnerGiftActivity;
import com.xiumi.qirenbao.reward.bean.GiftBean;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 作者 ：Created by DXR on 2017/3/24.
 * 合伙人-现有未变现礼物
 */

public class PartnerAllGiftAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GiftBean> giftlist = new ArrayList<GiftBean>();
    // 收到的礼物统计 list
    private ArrayList<String> numlist = new ArrayList<String>();

    public PartnerAllGiftAdapter(Context context, ArrayList<GiftBean> giftlist, ArrayList<String> numlist) {
        this.context = context;
        this.giftlist = giftlist;
        this.numlist = numlist;
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
        final ViewHolder viewHolder;
        final GiftBean item = giftlist.get(position);
        final String num = numlist.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.partner_gift_list_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.gift_icon = (ImageView) convertView.findViewById(R.id.gift_icon);
            viewHolder.gift_num = (TextView) convertView.findViewById(R.id.gift_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(item.icon)) {
            ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + item.icon, viewHolder.gift_icon);
        }
        viewHolder.gift_num.setText(num);
        return convertView;

    }

    private class ViewHolder {
        ImageView gift_icon;
        TextView gift_num;
    }
}
