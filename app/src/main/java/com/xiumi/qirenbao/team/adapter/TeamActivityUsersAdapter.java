package com.xiumi.qirenbao.team.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.team.bean.ActivityListBean;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.widget.CircleImageView;

import java.util.ArrayList;

/**
 * 作者 ：Created by DXR on 2017/3/22.
 * 达人-团队活动参与人头像展示 adapter
 */

public class TeamActivityUsersAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<ActivityListBean.Team_Activity_Users> userslist = new ArrayList<ActivityListBean.Team_Activity_Users>();

    public TeamActivityUsersAdapter(Context context, ArrayList<ActivityListBean.Team_Activity_Users> userslist) {
        this.context = context;
        this.userslist = userslist;
    }

    @Override
    public int getCount() {
        return userslist.size();
    }

    @Override
    public Object getItem(int position) {
        return userslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ActivityListBean.Team_Activity_Users item = userslist.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.team_activity_users_list_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.join_img = (CircleImageView) convertView.findViewById(R.id.join_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(item.user.user_info.avatar)) {
            ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + item.user.user_info.avatar, viewHolder.join_img);
        } else {
            viewHolder.join_img.setImageDrawable(context.getResources().getDrawable(R.drawable.peoppe));
        }
        return convertView;
    }
    private class ViewHolder {
        CircleImageView join_img;
    }
}
