package com.xiumi.qirenbao.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.home.bean.HistoryBean;
import com.xiumi.qirenbao.home.bean.RecommendTypeBean;

import java.util.List;

/**
 * Created by qianbailu on 2017/3/28.
 */

public class SkillItemAdapter extends BaseAdapter {

    private List<RecommendTypeBean> beans ;
    private Context mContext ;


    public SkillItemAdapter(List<RecommendTypeBean> beans, Context mContext) {
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
        final RecommendTypeBean item = beans.get(position);
        if (convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.skill_item_layout, parent, false);
            myViewHolder =new ViewHolder();

            myViewHolder.historyName = (TextView) convertView.findViewById(R.id.search_history_tv);

            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (ViewHolder) convertView.getTag();
        }
        if(item.isCheck==true){
            myViewHolder.historyName.setTextColor(mContext.getResources().getColor(R.color.white));
            myViewHolder.historyName.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.add_join_backround));

        }else{
            myViewHolder.historyName.setTextColor(mContext.getResources().getColor(R.color.black));
            myViewHolder.historyName.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.text_skill_border));
        }
        myViewHolder.historyName.setText(item.name);
        return convertView;
    }

    private class ViewHolder {

        TextView historyName;

    }
}
