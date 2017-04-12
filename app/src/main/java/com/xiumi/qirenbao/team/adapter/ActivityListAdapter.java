package com.xiumi.qirenbao.team.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
import com.xiumi.qirenbao.team.bean.ActivityListBean;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.widget.CustomerListView;
import com.xiumi.qirenbao.widget.FullGridView;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 作者 ：Created by DXR on 2017/3/6.
 * 达人 - 活动列表adapter
 */

public class ActivityListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ActivityListBean> activityList = new ArrayList<ActivityListBean>();
    PullToRefreshLayout mPullRefreshScrollView;
    // 参与 人 头像展示 adapter
    private TeamActivityUsersAdapter teamActivityUsersAdapter = null;

    public ActivityListAdapter(Context context, ArrayList<ActivityListBean> activityList, PullToRefreshLayout mPullRefreshScrollView) {
        this.context = context;
        this.activityList = activityList;
        this.mPullRefreshScrollView = mPullRefreshScrollView;
    }

    @Override
    public int getCount() {
        return activityList.size();
    }

    @Override
    public Object getItem(int position) {
        return activityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ActivityListBean item = activityList.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_list_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.activity_title = (TextView) convertView.findViewById(R.id.activity_title);
            viewHolder.activity_image = (ImageView) convertView.findViewById(R.id.activity_image);
            viewHolder.start_at = (TextView) convertView.findViewById(R.id.start_at);
            viewHolder.activity_address = (TextView) convertView.findViewById(R.id.activity_address);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.sign_up = (TextView) convertView.findViewById(R.id.sign_up);
            viewHolder.users_list = (FullGridView) convertView.findViewById(R.id.users_list);
            viewHolder.state_change = (TextView) convertView.findViewById(R.id.state_change);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(item.title)) {
            viewHolder.activity_title.setText("《活动主题》" + item.title);
        }
        if (item.img_url!=null) {
            // 需完善活动图片展示
            ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + item.img_url, viewHolder.activity_image);
        } else {
            viewHolder.activity_image.setImageDrawable(context.getResources().getDrawable(R.drawable.add_pic));
        }

        if (!TextUtils.isEmpty(item.start_at)) {
            viewHolder.start_at.setText(item.start_at);
        }
        if (!TextUtils.isEmpty(item.address)) {
            viewHolder.activity_address.setText(item.address);
        }
        if (!TextUtils.isEmpty(item.description)) {
            viewHolder.description.setText(item.description);
        }
        // 0 进行中 3 已取消 1 已结束
        if (!TextUtils.isEmpty(item.status)) {
            switch (Integer.parseInt(item.status)) {
                case 0:
                    if (item.team_activity_users != null && item.team_activity_users.size() > 0) {
                        for (int i = 0; i < item.team_activity_users.size(); i++) {
                            if (item.team_activity_users.get(i).user_id.equals(MainActivity.id)) {
                                viewHolder.sign_up.setText("已报名");
                            } else {
                                viewHolder.sign_up.setText("报名");
                                viewHolder.state_change.setText("进行中");
                                viewHolder.state_change.setBackgroundResource(R.drawable.master_home_master_border);
                                viewHolder.sign_up.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            long start = stringToLong(item.start_at, "yyyy-MM-dd HH:mm");
                                            long curDate = new Date().getTime();//获取当前时间
                                            if (curDate < start) {
                                                new android.app.AlertDialog.Builder(context).setMessage("确认报名活动？")
                                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // 报名活动
                                                                JoinActivity(item.id);
                                                            }
                                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                }).create().show();
                                            } else {
                                                Toast.makeText(context, "活动报名已结束", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }
                        }
                    } else {
                        viewHolder.sign_up.setText("报名");
                        viewHolder.state_change.setText("进行中");
                        viewHolder.state_change.setBackgroundResource(R.drawable.master_home_master_border);
                        viewHolder.sign_up.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    long start = stringToLong(item.start_at, "yyyy-MM-dd HH:mm");
                                    long curDate = new Date().getTime();//获取当前时间
                                    if (curDate < start) {
                                        new android.app.AlertDialog.Builder(context).setMessage("确认报名活动？")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // 报名活动
                                                        JoinActivity(item.id);
                                                    }
                                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).create().show();
                                    } else {
                                        Toast.makeText(context, "活动报名已结束", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    break;
                case 1:
                    viewHolder.sign_up.setText("活动已结束");
                    viewHolder.state_change.setText("已结束");
                    viewHolder.state_change.setBackgroundResource(R.drawable.master_home_platinum_border);
                    break;
                case 3:
                    viewHolder.sign_up.setText("活动已取消");
                    viewHolder.state_change.setText("已取消");
                    viewHolder.state_change.setBackgroundResource(R.drawable.master_home_platinum_border);
                    break;
                default:
                    break;
            }
        }
        if (item.team_activity_users != null) {
            teamActivityUsersAdapter = new TeamActivityUsersAdapter(context, item.team_activity_users);
            viewHolder.users_list.setAdapter(teamActivityUsersAdapter);
            teamActivityUsersAdapter.notifyDataSetChanged();
        }
        return convertView;
    }

    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public static long dateToLong(Date date) {
        return date.getTime();
    }

    private class ViewHolder {
        TextView activity_title;
        ImageView activity_image;
        TextView start_at;
        TextView activity_address;
        TextView description;
        TextView sign_up; // 报名
        FullGridView users_list; // 报名参与人集合
        TextView state_change;
    }

    /**
     * 达人报名活动
     */
    private void JoinActivity(final String team_activity_id) {
        String url = "https://qrb.shoomee.cn/api/joinActivity";
        OkHttpUtils
                .post()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("team_activity_id", team_activity_id)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.v("达人报名活动", object.toString());
                            if (object.getString("result").equals("success")) {
                                Toast.makeText(context, "报名成功", Toast.LENGTH_SHORT).show();
                                mPullRefreshScrollView.autoRefresh();
                            } else {
                                Toast.makeText(context, "你已经报名了哦", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }
}
