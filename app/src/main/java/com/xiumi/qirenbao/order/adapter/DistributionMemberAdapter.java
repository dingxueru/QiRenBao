package com.xiumi.qirenbao.order.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.order.partnership.DistributionMembersActivity;
import com.xiumi.qirenbao.team.bean.TeamMemberBean;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 作者 ：Created by DXR on 2017/3/18.
 * 分配团员adapter
 */

public class DistributionMemberAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TeamMemberBean> memberlist = new ArrayList<TeamMemberBean>();
    private int order_id; // 订单id

    public DistributionMemberAdapter(Context context, ArrayList<TeamMemberBean> memberlist, int order_id) {
        this.context = context;
        this.memberlist = memberlist;
        this.order_id = order_id;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.distribution_member_list_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.member_avatar = (ImageView) convertView.findViewById(R.id.member_avatar);
            viewHolder.member_name = (TextView) convertView.findViewById(R.id.member_name);
            viewHolder.member_job = (TextView) convertView.findViewById(R.id.member_job);
            viewHolder.clicked = (TextView) convertView.findViewById(R.id.clicked);
            viewHolder.view = (LinearLayout) convertView.findViewById(R.id.view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (item.type == 1) {
            viewHolder.member_avatar.setVisibility(View.GONE);
            viewHolder.member_job.setVisibility(View.GONE);
            viewHolder.clicked.setVisibility(View.GONE);
            viewHolder.member_name.setVisibility(View.GONE);
        } else {
            if (item.user.user_info != null) {
                if (!TextUtils.isEmpty(item.user.user_info.avatar)) {
                    ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + item.user.user_info.avatar, viewHolder.member_avatar);
                }
                if (!TextUtils.isEmpty(item.user.user_info.work_title)) {
                    viewHolder.member_job.setText(item.user.user_info.work_title);
                }
            }
            if (!TextUtils.isEmpty(item.user.name)) {
                viewHolder.member_name.setText(item.user.name);
            }
            // 根据item是否被点击改变状态
            if (item.ischeck == true) {
                viewHolder.clicked.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.distribution_is_select));
            } else {
                viewHolder.clicked.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.distribution_no_select));
            }
        }
        return convertView;
    }


    private class ViewHolder {
        ImageView member_avatar;
        TextView member_name;
        TextView member_job;
        TextView clicked;
        LinearLayout view;
    }
}
