package com.xiumi.qirenbao.team.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.widget.CircleImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianbailu on 2017/3/30.
 */

public class TeamAdapter extends BaseAdapter {

    private Context context;
    private List<String> name=new ArrayList<>(),head=new ArrayList<>(),sex=new ArrayList<>(),work_duty=new ArrayList<>(),growth_value=new ArrayList<>();


    public TeamAdapter(Context context,List<String> name,List<String> head,List<String> sex,List<String> work_duty,List<String> growth_value) {
        this.context = context;
        this.name=name;
        this.head=head;
        this.sex=sex;
        this.work_duty=work_duty;
        this.growth_value=growth_value;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String  name1 = name.get(position);
        String  head1=head.get(position);
        String  sex1 = sex.get(position);
        String  work_duty1=work_duty.get(position);
        String  growth_value1 = growth_value.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.people_detail_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.partner_img= (CircleImageView) convertView.findViewById(R.id.partner_img);
            viewHolder.partner_name= (TextView) convertView.findViewById(R.id.partner_name);
            viewHolder.sex= (ImageView) convertView.findViewById(R.id.sex);
            viewHolder.work_duty= (TextView) convertView.findViewById(R.id.work_duty);
            viewHolder.growth_value= (TextView) convertView.findViewById(R.id.growth_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + head1,viewHolder.partner_img);
        viewHolder.partner_name.setText(name1);
        if(sex1.equals("1")){
            viewHolder.sex.setImageDrawable(context.getResources().getDrawable(R.drawable.men));
        }else{
            viewHolder.sex.setImageDrawable(context.getResources().getDrawable(R.drawable.women));
        }
        viewHolder.work_duty.setText(work_duty1);
        if(!work_duty1.equals("null"))

        viewHolder.growth_value.setText(growth_value1);
        return convertView;
    }
    private class ViewHolder {
       CircleImageView partner_img;
       TextView partner_name;
       ImageView sex;
       TextView work_duty;
       TextView growth_value;
    }
}
