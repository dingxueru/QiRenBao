package com.xiumi.qirenbao.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.home.bean.HistoryBean;
import com.xiumi.qirenbao.home.bean.RecommendTypeBean;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.widget.CircleImageView;

import java.util.List;

/**
 * Created by qianbailu on 2017/3/2.
 */
public class TypeAdapter  extends BaseAdapter {

    private List<RecommendTypeBean> beans ;
    private Context mContext ;


    public TypeAdapter(List<RecommendTypeBean> beans, Context mContext) {
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

            convertView = LayoutInflater.from(mContext).inflate(R.layout.hot_type_layout, parent, false);
            myViewHolder =new ViewHolder();
            myViewHolder.typeName = (TextView) convertView.findViewById(R.id.tv_type);
            myViewHolder.img= (ImageView) convertView.findViewById(R.id.iv_img);
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (ViewHolder) convertView.getTag();
        }
        myViewHolder.typeName.setText(item.name);
        ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + item.icon,myViewHolder.img);

        return convertView;
    }

    private class ViewHolder {

        TextView typeName;
        ImageView img;

    }
}
