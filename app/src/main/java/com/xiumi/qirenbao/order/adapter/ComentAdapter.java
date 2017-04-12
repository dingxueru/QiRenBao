package com.xiumi.qirenbao.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.order.bean.CommentBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianbailu on 2017/3/16.
 * 评价
 */

public class ComentAdapter extends BaseAdapter {

    private Context context;
    private List<CommentBean> commentlist = new ArrayList<CommentBean>();
    public static  String SeleckStr="";
    public ComentAdapter(Context context, List<CommentBean> commentlist) {
        this.context = context;
        this.commentlist = commentlist;
    }

    @Override
    public int getCount() {
        return commentlist.size();
    }

    @Override
    public Object getItem(int position) {
        return commentlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommentBean item = commentlist.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.content_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.skillName = (TextView) convertView.findViewById(R.id.skill_name_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.skillName.setText(item.content);
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.skillName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalViewHolder.skillName.isSelected() == false) {
                    SeleckStr=SeleckStr+item.id+",";
                    finalViewHolder.skillName.setSelected(true);
                    finalViewHolder.skillName.setTextColor(context.getResources().getColor(R.color.white));
                    //2是达人1是企业主3是合伙人
                    if(MainActivity.chose==2){
                        finalViewHolder.skillName.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.textbord_orange_select));
                    }else{
                        finalViewHolder.skillName.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.textbord_select));
                    }
                }else{
                    SeleckStr=SeleckStr.replace(item.id+",","");
                    finalViewHolder.skillName.setSelected(false);
                    finalViewHolder.skillName.setTextColor(context.getResources().getColor(R.color.unselcet));
                    finalViewHolder.skillName.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.textbord_unselect));
                }
            }
        });
        return convertView;
    }
    private class ViewHolder {
        TextView skillName;
    }
}
