package com.xiumi.qirenbao.team.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.team.bean.ActivityListBean;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.widget.CircleImageView;

import java.util.List;

/**
 * Created by qianbailu on 2017/3/27.
 */

public class AddPeopleAdapter extends BaseAdapter {

    private List<ActivityListBean.Team_Activity_Users> beans ;
    private Context mContext ;


    public AddPeopleAdapter(List<ActivityListBean.Team_Activity_Users> beans, Context mContext) {
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
        final ActivityListBean.Team_Activity_Users item = beans.get(position);
        if (convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.add_people_layout, parent, false);
            myViewHolder =new ViewHolder();

            myViewHolder.people_head = (CircleImageView) convertView.findViewById(R.id.people_head);

            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (ViewHolder) convertView.getTag();
        }
        ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + item.user.user_info.avatar, myViewHolder.people_head);

        return convertView;
    }

    private class ViewHolder {

        CircleImageView people_head;

    }
}
