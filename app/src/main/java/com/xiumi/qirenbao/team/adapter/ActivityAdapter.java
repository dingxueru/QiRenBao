package com.xiumi.qirenbao.team.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.team.bean.ActivityListBean;
import com.xiumi.qirenbao.team.partnership.AddPicActivity;
import com.xiumi.qirenbao.team.partnership.PeopeleActivity;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.widget.FullGridView;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by qianbailu on 2017/3/11.
 */

public class ActivityAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ActivityListBean> activityList = new ArrayList<ActivityListBean>();
    private PullToRefreshLayout mPullRefreshScrollView;
    public ActivityAdapter(Context context, ArrayList<ActivityListBean> activityList, PullToRefreshLayout mPullRefreshScrollView) {
        this.context = context;
        this.activityList = activityList;
        this.mPullRefreshScrollView=mPullRefreshScrollView;
    }

    @Override
    public int getCount() {
        return activityList.size();
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
        for(int i=0;i<activityList.size();i++){
            Log.e("activity",activityList.get(i).id);
        }

        final ActivityListBean item = activityList.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_partnerlayout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.activity_title = (TextView) convertView.findViewById(R.id.activity_title);
            viewHolder.activity_image = (ImageView) convertView.findViewById(R.id.activity_image);
            viewHolder.start_at = (TextView) convertView.findViewById(R.id.start_at);
            viewHolder.activity_address = (TextView) convertView.findViewById(R.id.activity_address);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            viewHolder.cancel= (TextView) convertView.findViewById(R.id.cancel);
            viewHolder.add_show= (TextView) convertView.findViewById(R.id.add_show);
            viewHolder.has_finish= (TextView) convertView.findViewById(R.id.has_finish);
            viewHolder.finish= (TextView) convertView.findViewById(R.id.finish);
            viewHolder.click_finish= (TextView) convertView.findViewById(R.id.click_finish);
            viewHolder.has_delete= (TextView) convertView.findViewById(R.id.has_delete);
            viewHolder.history_girde= (FullGridView) convertView.findViewById(R.id.history_girde);
            viewHolder.has=(LinearLayout)convertView.findViewById(R.id.has);
            viewHolder.add_finish=(TextView) convertView.findViewById(R.id.add_finish);
            viewHolder.has_gone=(TextView) convertView.findViewById(R.id.has_gone);
            viewHolder.top= (TextView) convertView.findViewById(R.id.top);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(position==0){
            viewHolder.top.setVisibility(View.GONE);
        }
        viewHolder.click_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    long start=   stringToLong(item.start_at,"yyyy-MM-dd HH:mm");
                     long curDate =  new Date().getTime();//获取当前时间
                    curDate +=30*60*1000;
                    Log.e("start",start+"");
                    Log.e("curDate",curDate+"");
                    if(curDate<start){
                        new AlertDialog.Builder(context).setMessage("一定要在活动完成后才能点完成哦")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setCancelable(false).show();
                    }else{
                        Intent intent=new Intent(context, AddPicActivity.class);
                        intent.putExtra("id",item.id);
                        context.startActivity(intent);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        if (!TextUtils.isEmpty(item.title)) {
            viewHolder.activity_title.setText("活动主题《"+item.title+"》");
        }
            if(item.team_activity_users!=null){
            final ArrayList<String> head=new ArrayList<>();
            final ArrayList<String> name=new ArrayList<>();
            final ArrayList<String> sex=new ArrayList<>();
            final ArrayList<String> work_duty=new ArrayList<>();
            final ArrayList<String> growth_value=new ArrayList<>();
            for(int i=0;i<item.team_activity_users.size();i++){
                head.add(item.team_activity_users.get(i).user.user_info.avatar);
                name.add(item.team_activity_users.get(i).user.name);
                sex.add(item.team_activity_users.get(i).user.user_info.sex);
                work_duty.add(item.team_activity_users.get(i).user.user_info.work_duty);
                growth_value.add(item.team_activity_users.get(i).user.user_info.growth_value);
            }
            AddPeopleAdapter addPeopleAdapter=new AddPeopleAdapter(item.team_activity_users,context);
            viewHolder.history_girde.setAdapter(addPeopleAdapter);
            addPeopleAdapter.notifyDataSetChanged();
            viewHolder.has.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, PeopeleActivity.class);
                    intent.putStringArrayListExtra("head", head);
                    intent.putStringArrayListExtra("name", name);
                    intent.putStringArrayListExtra("sex", sex);
                    intent.putStringArrayListExtra("work_duty", work_duty);
                    intent.putStringArrayListExtra("growth_value", growth_value);
                    context.startActivity(intent);
                }
            });

         }
        if (!TextUtils.isEmpty(item.start_at)) {
            viewHolder.start_at.setText(item.start_at);
        }
        if (!TextUtils.isEmpty(item.address)) {
            viewHolder.activity_address.setText(item.address);
        }
        if (!TextUtils.isEmpty(item.description)) {
            viewHolder.description.setText(item.description);
        }else{
            viewHolder.description.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(item.status)) {
            // status 状态(0发布，3删除)
            if(item.status.equals("0")){
                viewHolder.activity_image.setImageDrawable(context.getResources().getDrawable(R.drawable.add_pic));
                viewHolder.add_show.setVisibility(View.VISIBLE);
                viewHolder.has_finish.setVisibility(View.GONE);
                viewHolder.cancel.setVisibility(View.VISIBLE);
                viewHolder.finish.setVisibility(View.GONE);
                viewHolder.click_finish.setVisibility(View.VISIBLE);
                viewHolder.has_delete.setVisibility(View.GONE);
                viewHolder.add_finish.setVisibility(View.GONE);
                viewHolder.has_gone.setVisibility(View.GONE);
            }else if(item.status.equals("1")){
                ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + item.img_url, viewHolder.activity_image);
                viewHolder.add_show.setVisibility(View.GONE);
                viewHolder.has_finish.setVisibility(View.VISIBLE);
                viewHolder.cancel.setVisibility(View.GONE);
                viewHolder.finish.setVisibility(View.VISIBLE);
                viewHolder.click_finish.setVisibility(View.GONE);
                viewHolder.has_delete.setVisibility(View.GONE);
                viewHolder.add_finish.setVisibility(View.GONE);
                viewHolder.has_gone.setVisibility(View.GONE);
            }else{
                viewHolder.activity_image.setImageDrawable(context.getResources().getDrawable(R.drawable.add_pic));
                viewHolder.add_show.setVisibility(View.GONE);
                viewHolder.has_finish.setVisibility(View.GONE);
                viewHolder.cancel.setVisibility(View.GONE);
                viewHolder.finish.setVisibility(View.GONE);
                viewHolder.click_finish.setVisibility(View.GONE);
                viewHolder.has_delete.setVisibility(View.GONE);
                viewHolder.add_finish.setVisibility(View.VISIBLE);
                viewHolder.has_gone.setVisibility(View.VISIBLE);

            }
        }

        viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除活动
                DelActivity(item.id);
            }
        });
        return convertView;
    }
    /**
     * 删除活动
     */
    private void DelActivity(String id) {
        String url = "https://qrb.shoomee.cn/api/deleteActivity";
        OkHttpUtils
                .post()
                .addHeader("Accept","application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("activity_id",id)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(context,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.v("获取团队活动列表",object.toString());
                            if (object.getString("result").equals("success")) {
                                new AlertDialog.Builder(context).setMessage("你确定要取消这个活动吗？")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mPullRefreshScrollView.autoRefresh();
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }

                                        }).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }
    private class ViewHolder {
        TextView activity_title;
        ImageView activity_image;
        TextView start_at;
        TextView activity_address;
        TextView description;
        TextView status;
        TextView cancel;
        TextView add_show;
        TextView has_finish;
        FullGridView history_girde;
        TextView finish;
        LinearLayout has;
        TextView click_finish;
        TextView has_delete;
        TextView add_finish;
        TextView has_gone;
        TextView top;
    }
}
