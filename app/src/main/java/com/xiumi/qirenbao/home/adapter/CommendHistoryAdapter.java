package com.xiumi.qirenbao.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.home.bean.RecommendSerchBean;

import java.util.List;

/**
 * Created by qianbailu on 2017/3/2.
 */
public class CommendHistoryAdapter extends BaseAdapter {

    private List<RecommendSerchBean> beans ;
    private Context mContext ;


    public CommendHistoryAdapter(List<RecommendSerchBean> beans, Context mContext) {
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
        final RecommendSerchBean item = beans.get(position);
        if (convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.recommend_item_layout, parent, false);
            myViewHolder =new ViewHolder();

            myViewHolder.recommendName = (TextView) convertView.findViewById(R.id.commend_item);

            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (ViewHolder) convertView.getTag();
        }
        myViewHolder.recommendName.setText(item.name);
        return convertView;
    }

    private class ViewHolder {

        TextView recommendName;

    }
}
