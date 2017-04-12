package com.xiumi.qirenbao.location.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.location.bean.AreasBean;
import java.util.List;

/**
 * Created by qianbailu on 2017/3/2.
 */
public class LocationAdapter extends BaseAdapter {

    private List<AreasBean> beans ;
    private Context mContext ;


    public LocationAdapter(List<AreasBean> beans, Context mContext) {
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
        final AreasBean item = beans.get(position);
        if (convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.location_item_layout, parent, false);
            myViewHolder =new ViewHolder();

            myViewHolder.typeName = (TextView) convertView.findViewById(R.id.tv_location);

            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (ViewHolder) convertView.getTag();
        }
        myViewHolder.typeName.setText(item.name);
        return convertView;
    }

    private class ViewHolder {

        TextView typeName;


    }
}
