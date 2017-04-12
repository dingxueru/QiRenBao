package com.xiumi.qirenbao.team.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import com.xiumi.qirenbao.team.bean.ApplyBean;

/**
 * Created by qianbailu on 2017/3/10.
 */

public class ApplayAdapter extends BaseAdapter {
    ViewHolder viewHolder;
    private Context context;
    private ArrayList<ApplyBean> applyBeanList = new ArrayList<ApplyBean>();
    private PullToRefreshLayout mPullRefreshScrollView ;
    public ApplayAdapter(Context context, ArrayList<ApplyBean> applyBeanList,PullToRefreshLayout mPullRefreshScrollView) {
        this.context = context;
        this.applyBeanList = applyBeanList;
        this.mPullRefreshScrollView=mPullRefreshScrollView;
    }

    @Override
    public int getCount() {
        return applyBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return applyBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ApplyBean item = applyBeanList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.apply_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.people_head = (ImageView) convertView.findViewById(R.id.people_head);
            viewHolder.apply_name = (TextView) convertView.findViewById(R.id.apply_name);
            viewHolder.member_job = (TextView) convertView.findViewById(R.id.member_job);
            viewHolder.apply_occupation= (TextView) convertView.findViewById(R.id.apply_occupation);
            viewHolder.apply_description= (TextView) convertView.findViewById(R.id.apply_description);
            viewHolder.agress_add= (TextView) convertView.findViewById(R.id.agress_add);
            viewHolder.disagree_add= (TextView) convertView.findViewById(R.id.disagree_add);
            viewHolder.has_add= (TextView) convertView.findViewById(R.id.has_add);
            viewHolder.has_refress= (TextView) convertView.findViewById(R.id.has_refress);
            viewHolder.sex_img=(ImageView)convertView.findViewById(R.id.sex_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(item.apply_result.equals("0")){
            //未接受省钱
            viewHolder.agress_add.setVisibility(View.VISIBLE);
            viewHolder.disagree_add.setVisibility(View.VISIBLE);
            viewHolder.has_add.setVisibility(View.GONE);
            viewHolder.has_refress.setVisibility(View.GONE);
        }else if(item.apply_result.equals("1")){
            //已同意
            viewHolder.agress_add.setVisibility(View.GONE);
            viewHolder.disagree_add.setVisibility(View.GONE);
            viewHolder.has_add.setVisibility(View.VISIBLE);
            viewHolder.has_refress.setVisibility(View.GONE);
        }else{
            //已拒绝
            viewHolder.agress_add.setVisibility(View.GONE);
            viewHolder.disagree_add.setVisibility(View.GONE);
            viewHolder.has_add.setVisibility(View.GONE);
            viewHolder.has_refress.setVisibility(View.VISIBLE);
        }
        ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + item.user.user_info.avatar, viewHolder.people_head);
       if(item.user.user_info.sex=="1"){
           viewHolder.sex_img.setImageDrawable(context.getResources().getDrawable(R.drawable.men));
       }else{
           viewHolder.sex_img.setImageDrawable(context.getResources().getDrawable(R.drawable.women));
       }
        viewHolder.apply_name.setText(item.user.name);
        viewHolder.apply_occupation.setText(item.user.user_info.work_title);
        if(StringUtils.isEmpty(item.comment)){
            viewHolder.apply_description.setVisibility(View.GONE);
        }else{
            viewHolder.apply_description.setVisibility(View.VISIBLE);
            viewHolder.apply_description.setText(item.comment);
        }
        viewHolder.agress_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //合伙人接受达人申请
                agreeApply(item.user.id);
            }
        });
        viewHolder.disagree_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //合伙人拒绝达人的申请
                disagreeApply(item.user.id);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView people_head;
        TextView apply_name;
        TextView member_job;
        TextView apply_occupation;
        TextView apply_description;
        TextView agress_add;
        TextView disagree_add;
        TextView has_add;
        TextView has_refress;
        ImageView sex_img;
    }
    /**
     * 合伙人接受达人的申请
     */
    private void agreeApply(final String user_id){
        String url= "https://qrb.shoomee.cn//api/acceptJoin";
        OkHttpUtils
                .post()
                .addHeader("Accept","application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("team_id",MainActivity.team_id)
                .addParams("user_id",user_id)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        agreeApply(user_id);
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("合伙人接受达人的申请",obj.toString());
                            if(obj.getString("result").equals("success")){
                                mPullRefreshScrollView.autoRefresh();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                           }
                    }
                });
    }
    /**
     * 合伙人拒绝达人的申请
     */
    private void disagreeApply(final String user_id){
        String url= "https://qrb.shoomee.cn//api/refuseJoin";
        OkHttpUtils
                .post()
                .addHeader("Accept","application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("team_id",MainActivity.team_id)
                .addParams("user_id",user_id)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        disagreeApply(user_id);
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("合伙人拒绝达人的申请",obj.toString());
                            if(obj.getString("result").equals("success")){
                                mPullRefreshScrollView.autoRefresh();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
