package com.xiumi.qirenbao.myWallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.myWallet.bean.IncomeBean;
import java.util.ArrayList;

/**
 * Created by qianbailu on 2017/3/18.
 * 钱包的收支记录
 */

public class IncomeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<IncomeBean> incomeBeen = new ArrayList<IncomeBean>();

    public IncomeAdapter(Context context, ArrayList<IncomeBean> incomeBeen) {
        this.context = context;
        this.incomeBeen = incomeBeen;
    }

    @Override
    public int getCount() {
        return incomeBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return incomeBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final IncomeBean item = incomeBeen.get(position);
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

        viewHolder.name.setText(item.log_title);
        viewHolder.time.setText(item.updated_at);
        if(item.change_type.equals("REDUCE")){
            viewHolder.change_num.setText("¥ -"+item.change_num);
        }else{
            viewHolder.change_num.setText("¥ +"+item.change_num);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView change_num;
        TextView time;
    }
}
