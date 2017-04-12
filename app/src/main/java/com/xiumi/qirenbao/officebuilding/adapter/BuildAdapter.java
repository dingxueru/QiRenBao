package com.xiumi.qirenbao.officebuilding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.officebuilding.bean.BuildBean;
import java.util.ArrayList;

/**
 * Created by qianbailu on 2017/2/28.
 */
public class BuildAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BuildBean.DataBean> buildlist = new ArrayList<BuildBean.DataBean>();

    public BuildAdapter(Context context, ArrayList<BuildBean.DataBean> buildlist) {
        this.context = context;
        this.buildlist = buildlist;
    }

    @Override
    public int getCount() {
        return buildlist.size();
    }

    @Override
    public Object getItem(int position) {
        return buildlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BuildBean.DataBean item = buildlist.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.build_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.name= (TextView) convertView.findViewById(R.id.name);
            viewHolder.address= (TextView) convertView.findViewById(R.id.address);
            viewHolder.cost= (TextView) convertView.findViewById(R.id.cost);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load("https://qrb.shoomee.cn/upload/" +item.pic).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESULT).into(viewHolder.imageView);
        viewHolder.name.setText(item.name);
        viewHolder.address.setText(item.address);
        viewHolder.cost.setText("¥ "+item.price);
        return convertView;
    }
    private class ViewHolder {
        ImageView imageView;
        TextView name;
        TextView address;
        TextView cost;
        TextView skillType;
        TextView collectcount;
        TextView distance;
    }
}
