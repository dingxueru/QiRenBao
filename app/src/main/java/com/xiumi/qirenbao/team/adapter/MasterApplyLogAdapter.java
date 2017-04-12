package com.xiumi.qirenbao.team.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.team.bean.MasterApplyLogBean;
import com.xiumi.qirenbao.utils.ImageUtil;

import java.util.ArrayList;

/**
 * 作者 ：Created by DXR on 2017/3/11.
 * 达人申请列表 adapter
 */

public class MasterApplyLogAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MasterApplyLogBean> applylist = new ArrayList<MasterApplyLogBean>();

    public MasterApplyLogAdapter(Context context, ArrayList<MasterApplyLogBean> applylist) {
        this.context = context;
        this.applylist = applylist;
    }

    @Override
    public int getCount() {
        return applylist.size();
    }

    @Override
    public Object getItem(int position) {
        return applylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final MasterApplyLogBean item = applylist.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.master_apply_list_item_alyout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.team_name = (TextView) convertView.findViewById(R.id.team_name);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            viewHolder.lead_img = (ImageView) convertView.findViewById(R.id.lead_img);
            viewHolder.lead_name = (TextView) convertView.findViewById(R.id.lead_name);
            viewHolder.sex = (ImageView) convertView.findViewById(R.id.sex);
            viewHolder.work_duty = (TextView) convertView.findViewById(R.id.work_duty);
            viewHolder.growth_value = (TextView) convertView.findViewById(R.id.growth_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(item.team.name)) {
            viewHolder.team_name.setText(item.team.name);
        }
        if (!TextUtils.isEmpty(item.apply_result + "")) {
            // 完善 申请状态设置  0 待处理 1 成功 2 拒绝
            if (item.apply_result == 0) {
                viewHolder.status.setText("处理中");
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.orangenormal));
                viewHolder.status.setBackgroundResource(R.drawable.master_apply_status_border);
            }
            if (item.apply_result == 1) {
                viewHolder.status.setText("已同意");
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.blue));
                viewHolder.status.setBackgroundResource(R.drawable.master_apply_agree_border);
            }
            if (item.apply_result == 2) {
                viewHolder.status.setText("已拒绝");
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.chart1));
                viewHolder.status.setBackgroundResource(R.drawable.master_apply_refuse_border);
            }
        }
        if (!TextUtils.isEmpty(item.team.user.name)) {
            viewHolder.lead_name.setText(item.team.user.name);
        }
        if (!TextUtils.isEmpty(item.team.user.user_info.avatar)) {
            ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + item.team.user.user_info.avatar, viewHolder.lead_img);
        }
        if (!TextUtils.isEmpty(item.team.user.user_info.sex)) {
            if (item.team.user.user_info.sex.equals("2")) {
                viewHolder.sex.setImageDrawable(context.getResources().getDrawable(R.drawable.women));
            } else {
                viewHolder.sex.setImageDrawable(context.getResources().getDrawable(R.drawable.men));
            }
        }
        if (!TextUtils.isEmpty(item.team.user.user_info.work_title)) {
            viewHolder.work_duty.setText(item.team.user.user_info.work_title);
        }
        if (!TextUtils.isEmpty(item.team.user.user_info.growth_value)) {
            viewHolder.growth_value.setText(item.team.user.user_info.growth_value);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView team_name;
        TextView status;
        ImageView lead_img;
        TextView lead_name;
        ImageView sex;
        TextView work_duty;
        TextView growth_value;
    }
}
