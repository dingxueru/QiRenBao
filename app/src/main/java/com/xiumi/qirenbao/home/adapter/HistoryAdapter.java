package com.xiumi.qirenbao.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.home.bean.HistoryBean;

import java.util.List;

/**
 * Created by qianbailu on 2017/3/2.
 */
public class HistoryAdapter  extends BaseAdapter {

    private List<HistoryBean> beans ;
    private Context mContext ;


    public HistoryAdapter(List<HistoryBean> beans, Context mContext) {
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
        final HistoryBean item = beans.get(position);
        if (convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.history_layout, parent, false);
            myViewHolder =new ViewHolder();

            myViewHolder.historyName = (TextView) convertView.findViewById(R.id.search_history_tv);

            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (ViewHolder) convertView.getTag();
        }
        if(item.content.length()>4){
            myViewHolder.historyName.setText(item.content.substring(0,4));
    }else{
            myViewHolder.historyName.setText(item.content);
        }

        return convertView;
    }

    private class ViewHolder {

        TextView historyName;

    }
}
