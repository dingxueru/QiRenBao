package com.xiumi.qirenbao.order.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.order.bean.CompanyOrderBean;
import com.xiumi.qirenbao.order.company.CompanyCommentActivity;
import com.xiumi.qirenbao.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by qianbailu on 2017/3/15.
 */

public class CompanyOrderAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CompanyOrderBean> companylist = new ArrayList<CompanyOrderBean>();

    public CompanyOrderAdapter(Context context, ArrayList<CompanyOrderBean> companylist) {
        this.context = context;
        this.companylist = companylist;
    }

    @Override
    public int getCount() {
        return companylist.size();
    }

    @Override
    public Object getItem(int position) {
        return companylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CompanyOrderBean item = companylist.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.company_order_item_layout, null);
            viewHolder = new ViewHolder();
             viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.expert_name = (TextView) convertView.findViewById(R.id.expert_name);
            viewHolder.tell_people = (TextView) convertView.findViewById(R.id.tell_people);
            viewHolder.call= (TextView) convertView.findViewById(R.id.call);
            viewHolder.has_close= (TextView) convertView.findViewById(R.id.has_close);
            viewHolder.finish= (TextView) convertView.findViewById(R.id.finish);
            viewHolder.comment= (TextView) convertView.findViewById(R.id.comment);
            viewHolder.colonel= (TextView) convertView.findViewById(R.id.colonel);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(item.name!=null) {
            viewHolder.title.setText(item.name);
        }
        viewHolder.time.setText(item.updated_at);
        viewHolder.colonel.setText(item.partner_user.name);//团长
        if(item.service_user!=null){
            viewHolder.expert_name.setText(item.service_user.name);
        }else{
            viewHolder.expert_name.setText("");
        }
        if(item.tel_service_user!=null){
            viewHolder.tell_people.setText(item.tel_service_user.name);
        }else{
            viewHolder.tell_people.setText("");
        }
        if(item.status.equals("0")||item.status.equals("1")||item.status.equals("2")){
           //拨号按钮
            viewHolder.call.setVisibility(View.VISIBLE);
            viewHolder.has_close.setVisibility(View.GONE);
            viewHolder.finish.setVisibility(View.GONE);
            viewHolder.comment.setVisibility(View.GONE);
            viewHolder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //拨号
                    if(item.service_user!=null){
                        final String phone =item.service_user.mobile;
                        new AlertDialog.Builder(context).setMessage(phone)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
                    }else{
                        final String phone =item.partner_user.mobile;
                        new AlertDialog.Builder(context).setMessage(phone)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
                    }


                }
            });
        }else if(item.status.equals("5")||item.status.equals("6")){
            //已关闭
            viewHolder.has_close.setVisibility(View.VISIBLE);
            viewHolder.call.setVisibility(View.GONE);
            viewHolder.finish.setVisibility(View.GONE);
            viewHolder.comment.setVisibility(View.GONE);
        }else if(item.status.equals("4")){
            viewHolder.has_close.setVisibility(View.GONE);
            viewHolder.call.setVisibility(View.GONE);
           if(item.tel_service_id!=null){
               if(item.order_comment!=null&&StringUtils.isNotEmpty(item.order_comment.c2p_labels)&&StringUtils.isNotEmpty(item.order_comment.c2t_labels)){
                   viewHolder.finish.setVisibility(View.VISIBLE);
                   viewHolder.comment.setVisibility(View.GONE);
               }else{
                   viewHolder.finish.setVisibility(View.GONE);
                   viewHolder.comment.setVisibility(View.VISIBLE);
               }
           }else{
               if(item.order_comment!=null&&StringUtils.isNotEmpty(item.order_comment.c2p_labels)&&StringUtils.isNotEmpty(item.order_comment.p2c_labels)){
                   viewHolder.finish.setVisibility(View.VISIBLE);
                   viewHolder.comment.setVisibility(View.GONE);
               }else{
                   viewHolder.finish.setVisibility(View.GONE);
                   viewHolder.comment.setVisibility(View.VISIBLE);
               }
           }
        }
        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去评价
                Intent intent=new Intent(context,CompanyCommentActivity.class);
                if(item.tel_service_user!=null){
                    intent.putExtra("waiter",item.tel_service_user.name);
                    intent.putExtra("tel_service_id",item.tel_service_user.id);
                }
                intent.putExtra("partner_user_id",item.partner_user_id);
                intent.putExtra("name",item.service_user.name);
                intent.putExtra("order_id",item.id);
                intent.putExtra("service_user_id",item.service_user.id);
                intent.putExtra("avatar",item.service_user.user_info.avatar);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    private class ViewHolder {
        TextView title;
        TextView time;
        TextView expert_name;
        TextView tell_people;
        TextView call;
        TextView has_close;
        TextView finish;
        TextView comment;
        TextView colonel;
    }
}
