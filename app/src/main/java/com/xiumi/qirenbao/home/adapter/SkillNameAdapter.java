package com.xiumi.qirenbao.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiumi.qirenbao.R;

import java.util.List;

/**
 * Created by qianbailu on 2017/3/8.
 */

public class SkillNameAdapter  extends BaseAdapter {

    private List<String> beans ;
    private Context mContext ;


    public SkillNameAdapter(List<String> beans, Context mContext) {
        this.beans = beans;
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
      ViewHolder myViewHolder ;
       String item = beans.get(position);
        if (convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.skill_name_layout, parent, false);
            myViewHolder =new ViewHolder();
            myViewHolder.skillName = (TextView) convertView.findViewById(R.id.skill_name_tv);
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (ViewHolder) convertView.getTag();
        }

        if(item.length()>4){
            item=item.substring(0,4);
        }
        myViewHolder.skillName.setText(item);
         if(position==0){
             myViewHolder.skillName .setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.textview_serch_border));
             myViewHolder.skillName.setTextColor(mContext.getResources().getColor(R.color.white));
         }
        return convertView;
    }

    private class ViewHolder {

        TextView skillName;

    }
}
