package com.xiumi.qirenbao.myWallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.myWallet.MyWalletActivity;
import com.xiumi.qirenbao.myWallet.bean.WithdrawBean;

import java.util.ArrayList;

/**
 * Created by qianbailu on 2017/3/18.
 * 我的钱包提现
 */

public class WithdrawAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<WithdrawBean> withdrawBeen= new ArrayList<WithdrawBean>();

    public WithdrawAdapter(Context context, ArrayList<WithdrawBean> withdrawBeen) {
        this.context = context;
        this.withdrawBeen = withdrawBeen;
    }

    @Override
    public int getCount() {
        return withdrawBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return withdrawBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final WithdrawBean item = withdrawBeen.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.wallet_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.change_num = (TextView) convertView.findViewById(R.id.change_num);
            viewHolder.name = (TextView) convertView.findViewById(R.id.log_title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(item.status.equals("0")){
            //0是未完成
            viewHolder.name.setText("处理中");
            viewHolder.name.setTextColor(context.getResources().getColor(R.color.title_bg));
        }else{
            //1是完成
            viewHolder.name.setText("已完成");
        }
        viewHolder.time.setText(item.updated_at);
        viewHolder.change_num.setText("¥ -"+item.total_fee+".00");
        if(MyWalletActivity.isGetBack==1){
            viewHolder.change_num.setTextColor(context.getResources().getColor(R.color.chart1));
        }
        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView change_num;
        TextView time;
    }
}
