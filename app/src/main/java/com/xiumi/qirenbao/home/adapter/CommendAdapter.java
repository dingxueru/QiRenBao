package com.xiumi.qirenbao.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.home.bean.ParterDetailBean;
import com.xiumi.qirenbao.order.OrderCommentActivity;
import com.xiumi.qirenbao.order.bean.CommentBean;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.widget.CircleImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianbailu on 2017/3/9.
 */

public class CommendAdapter extends BaseAdapter {

    private List<ParterDetailBean.TeamBean.OrderBean> beans ;
    List<String> commentId=new ArrayList<>();
    List<String> commentName=new ArrayList<>();
    private Context mContext ;
    public CommendAdapter(List<ParterDetailBean.TeamBean.OrderBean> beans, List<String> commentId, List<String> commentName ,Context mContext) {
        this.beans = beans;
        this.commentId=commentId;
        this.commentName=commentName;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public Object getItem(int i) {
        return beans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      final ViewHolder myViewHolder ;

        final ParterDetailBean.TeamBean.OrderBean item = beans.get(position);
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.commend_layout, parent, false);
            myViewHolder =new ViewHolder();
            myViewHolder.head= (CircleImageView) convertView.findViewById(R.id.head);
            myViewHolder.name= (TextView) convertView.findViewById(R.id.name);
            myViewHolder.time= (TextView) convertView.findViewById(R.id.time);
            myViewHolder.commend= (TextView) convertView.findViewById(R.id.commend);
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (ViewHolder) convertView.getTag();
        }
        ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + item.company_user.user_info.avatar,myViewHolder.head);
        myViewHolder.name.setText(item.company_user.name);
        myViewHolder.time.setText(item.created_at);
        if(item.order_comment!=null && StringUtils.isNotEmpty(item.order_comment.c2p_labels)) {
            String labelName="";
            if(item.order_comment.c2p_labels.contains(",")){
                final String[] labels = item.order_comment.c2p_labels.split(",");
                Log.e("获取评价标签", item.order_comment.c2p_labels);
                Log.e("labels[0]",labels.length+"");
                Log.e("commentId",commentId.size()+"");
                  for(int j=0;j<labels.length;j++){
                    for(int i=0;i<commentId.size();i++){
                        if(labels[j].equals(commentId.get(i))){
                            labelName= labelName+"  "+commentName.get(i)+",";
                        }
                    }
                }
                myViewHolder.commend.setText(labelName.substring(1,labelName.length()-1));
            }else{
                myViewHolder.commend.setText("暂无评价");
            }
        }
        else
            myViewHolder.commend.setVisibility(View.GONE);
        return convertView;
    }

    private class ViewHolder {
        TextView commend;
        TextView time;
        TextView name;
        CircleImageView head;
    }

}