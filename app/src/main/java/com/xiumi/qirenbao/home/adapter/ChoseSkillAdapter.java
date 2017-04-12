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
 * Created by qianbailu on 2017/3/23.
 */

public class ChoseSkillAdapter   extends BaseAdapter {

    private List<String> beans ;
    private Context mContext ;
    public ChoseSkillAdapter(List<String> beans, Context mContext) {
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

        final String  item = beans.get(position);
        if (convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.pop_chose_layout, parent, false);
            myViewHolder =new ViewHolder();
            myViewHolder.skill_name = (TextView) convertView.findViewById(R.id.skill_name);
                  convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (ViewHolder) convertView.getTag();
        }
        myViewHolder.skill_name.setText(item);
        return convertView;
    }

    private class ViewHolder {

        TextView skill_name;

    }
}
