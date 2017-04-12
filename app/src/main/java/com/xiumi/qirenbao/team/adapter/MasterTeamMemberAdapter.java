package com.xiumi.qirenbao.team.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.team.bean.TeamMemberBean;
import com.xiumi.qirenbao.utils.ImageUtil;

import java.util.ArrayList;

/**
 * 作者 ：Created by DXR on 2017/3/7.
 * 达人- 我的团队成员
 */

public class MasterTeamMemberAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TeamMemberBean> memberlist = new ArrayList<TeamMemberBean>();

    public MasterTeamMemberAdapter(Context context, ArrayList<TeamMemberBean> memberlist) {
        this.context = context;
        this.memberlist = memberlist;
    }

    @Override
    public int getCount() {
        return memberlist.size();
    }

    @Override
    public Object getItem(int position) {
        return memberlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TeamMemberBean item = memberlist.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.master_team_member_list_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.member_avatar = (ImageView) convertView.findViewById(R.id.member_avatar);
            viewHolder.member_name = (TextView) convertView.findViewById(R.id.member_name);
            viewHolder.member_job = (TextView) convertView.findViewById(R.id.member_job);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (item.user.user_info != null) {
            if (!TextUtils.isEmpty(item.user.user_info.avatar)) {
                ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + item.user.user_info.avatar, viewHolder.member_avatar);
            }  else {
                viewHolder.member_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.peoppe));
            }
            if (!TextUtils.isEmpty(item.user.user_info.work_duty)) {
                viewHolder.member_job.setText(item.user.user_info.work_duty);
            }
        }
        if (!TextUtils.isEmpty(item.user.name)) {
            viewHolder.member_name.setText(item.user.name);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView member_avatar;
        TextView member_name;
        TextView member_job;
    }
}
