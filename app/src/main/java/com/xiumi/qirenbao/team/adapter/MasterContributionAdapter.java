package com.xiumi.qirenbao.team.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.team.bean.ContributeBean;

import java.util.ArrayList;

/**
 * 作者 ：Created by DXR on 2017/3/20.
 * 达人-贡献adapter
 */

public class MasterContributionAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ContributeBean> contributelist = new ArrayList<ContributeBean>();
    private ArrayList<String> name = new ArrayList<>();

    public MasterContributionAdapter(Context context, ArrayList<ContributeBean> contributelist) {
        this.context = context;
        this.contributelist = contributelist;
    }

    public MasterContributionAdapter(Context context, ArrayList<ContributeBean> contributelist, ArrayList<String> name) {
        this.context = context;
        this.contributelist = contributelist;
        this.name = name;
    }

    @Override
    public int getCount() {
        return contributelist.size();
    }

    @Override
    public Object getItem(int position) {
        return contributelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ContributeBean item = contributelist.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.master_contribution_list_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.order_title = (TextView) convertView.findViewById(R.id.order_title);
            viewHolder.create_time = (TextView) convertView.findViewById(R.id.create_time);
            viewHolder.gift_name = (TextView) convertView.findViewById(R.id.gift_name);
            viewHolder.gift_price = (TextView) convertView.findViewById(R.id.gift_price);
            viewHolder.growth_value = (TextView) convertView.findViewById(R.id.growth_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.order_title.setText(MainActivity.name + "的项目");
        if (!TextUtils.isEmpty(item.commission_fee + "")) {
            viewHolder.gift_name.setText("佣金： " + item.commission_fee + "元");
        }
        if (!TextUtils.isEmpty(item.created_at)) {
            viewHolder.create_time.setText(item.created_at);
        }
        if (!TextUtils.isEmpty(item.total_fee + "")) {
            int growth = Math.round(item.total_fee);
            viewHolder.growth_value.setText("+" + String.valueOf(growth) + " 经验值");
        }
        switch (item.gift_for_executor_id) {
            case 0:
                viewHolder.gift_price.setText("");
                break;
            case 1:
                viewHolder.gift_price.setText("礼物：" + item.gift_for_executor_total + "个" + name.get(0));
                break;
            case 2:
                viewHolder.gift_price.setText("礼物：" + item.gift_for_executor_total + "个" + name.get(1));
                break;
            case 3:
                viewHolder.gift_price.setText("礼物：" + item.gift_for_executor_total + "个" + name.get(2));
                break;
            case 4:
                viewHolder.gift_price.setText("礼物：" + item.gift_for_executor_total + "个" + name.get(3));
                break;
            case 5:
                viewHolder.gift_price.setText("礼物：" + item.gift_for_executor_total + "个" + name.get(4));
                break;
            case 6:
                viewHolder.gift_price.setText("礼物：" + item.gift_for_executor_total + "个" + name.get(5));
                break;
            case 7:
                viewHolder.gift_price.setText("礼物：" + item.gift_for_executor_total + "个" + name.get(6));
                break;
            case 8:
                viewHolder.gift_price.setText("礼物：" + item.gift_for_executor_total + "个" + name.get(7));
                break;
            case 9:
                viewHolder.gift_price.setText("礼物：" + item.gift_for_executor_total + "个" + name.get(8));
                break;
            case 10:
                viewHolder.gift_price.setText("礼物：" + item.gift_for_executor_total + "个" + name.get(9));
                break;
            case 11:
                viewHolder.gift_price.setText("礼物：" + item.gift_for_executor_total + "个" + name.get(10));
                break;
            case 12:
                viewHolder.gift_price.setText("礼物：" + item.gift_for_executor_total + "个" + name.get(11));
                break;
            default:
                break;

        }

        return convertView;
    }

    private class ViewHolder {

        TextView order_title;
        TextView create_time;
        TextView gift_name;
        TextView gift_price;
        TextView growth_value;
    }
}
