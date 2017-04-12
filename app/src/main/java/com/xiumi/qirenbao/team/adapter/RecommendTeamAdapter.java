package com.xiumi.qirenbao.team.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.team.bean.RecommendTeamBean;
import com.xiumi.qirenbao.team.expert.MasterApplyJoinTeamActivity;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 作者 ：Created by DXR on 2017/3/13.
 * 达人推荐团队 adapter
 */

public class RecommendTeamAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<RecommendTeamBean> recommendlist = new ArrayList<RecommendTeamBean>();

    public RecommendTeamAdapter(Context context, ArrayList<RecommendTeamBean> recommendlist) {
        this.context = context;
        this.recommendlist = recommendlist;
    }

    @Override
    public int getCount() {
        return recommendlist.size();
    }

    @Override
    public Object getItem(int position) {
        return recommendlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final RecommendTeamBean item = recommendlist.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recommend_item_list_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.team_name = (TextView) convertView.findViewById(R.id.team_name);
            viewHolder.lead_img = (ImageView) convertView.findViewById(R.id.lead_img);
            viewHolder.lead_name = (TextView) convertView.findViewById(R.id.lead_name);
            viewHolder.join_apply = (TextView) convertView.findViewById(R.id.join_apply);
            viewHolder.sex = (ImageView) convertView.findViewById(R.id.sex);
            viewHolder.work_duty = (TextView) convertView.findViewById(R.id.work_duty);
            viewHolder.growth_value = (TextView) convertView.findViewById(R.id.growth_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(item.name)) {
            viewHolder.team_name.setText(item.name);
        }
        if (!TextUtils.isEmpty(item.user.name)) {
            viewHolder.lead_name.setText(item.user.name);
        }
        // 达人申请加入团队
        viewHolder.join_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MasterApplyJoinTeamActivity.class);
                intent.putExtra("team_id",item.id);
                context.startActivity(intent);
            }
        });
        if (!TextUtils.isEmpty(item.user.user_info.avatar)) {
            ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + item.user.user_info.avatar, viewHolder.lead_img);
        } else {
            viewHolder.lead_img.setImageDrawable(context.getResources().getDrawable(R.drawable.peoppe));
        }
        if (!TextUtils.isEmpty(item.user.user_info.sex)) {
            if (item.user.user_info.sex.equals("2")) {
                viewHolder.sex.setImageDrawable(context.getResources().getDrawable(R.drawable.women));
            } else {
                viewHolder.sex.setImageDrawable(context.getResources().getDrawable(R.drawable.men));
            }
        }
        if (!TextUtils.isEmpty(item.user.user_info.work_title)) {
            viewHolder.work_duty.setText(item.user.user_info.work_title);
        }
        if (!TextUtils.isEmpty(item.user.user_info.growth_value)) {
            viewHolder.growth_value.setText(item.user.user_info.growth_value);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView team_name;
        ImageView lead_img;
        TextView lead_name;
        TextView join_apply;
        ImageView sex;
        TextView work_duty;
        TextView growth_value;
    }
}
