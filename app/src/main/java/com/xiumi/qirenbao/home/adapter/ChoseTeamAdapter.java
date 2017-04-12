package com.xiumi.qirenbao.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.home.bean.ChoseTeamBean;
import com.xiumi.qirenbao.home.company.PlaceOrderActivity;
import com.xiumi.qirenbao.officebuilding.BuildingDetailActivity;
import com.xiumi.qirenbao.officebuilding.PicStyleActivity;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.widget.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by qianbailu on 2017/3/7.
 */

public class ChoseTeamAdapter extends BaseAdapter {

    private List<ChoseTeamBean> beans ;
    private Context mContext ;
    public ChoseTeamAdapter(List<ChoseTeamBean> beans, Context mContext) {
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

        final ChoseTeamBean item = beans.get(position);
        if (convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.serch_chose_layout, parent, false);
            myViewHolder =new ViewHolder();
            myViewHolder.captainName = (TextView) convertView.findViewById(R.id.captain_name);
            myViewHolder.captainOccupation= (TextView) convertView.findViewById(R.id.captain_occupation);
            myViewHolder.userLv= (TextView) convertView.findViewById(R.id.user_lv);
            myViewHolder.workYears= (TextView) convertView.findViewById(R.id.work_years);
            myViewHolder.workDuty= (TextView) convertView.findViewById(R.id.work_duty);
            myViewHolder.company= (TextView) convertView.findViewById(R.id.company);
            myViewHolder.skillGirde= (GridView) convertView.findViewById(R.id.skill_girde);
            myViewHolder.head= (CircleImageView) convertView.findViewById(R.id.head);
            myViewHolder.sex= (ImageView) convertView.findViewById(R.id.sex);
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (ViewHolder) convertView.getTag();
        }
        myViewHolder.captainName.setText(item.user.name);
        myViewHolder.captainOccupation.setText(item.user.user_info.work_duty);
        myViewHolder.userLv.setText(item.user.user_info.growth_value+"");
        myViewHolder.workYears.setText(item.user.user_info.work_years);
        myViewHolder.workDuty.setText(item.user.user_info.work_title);
        myViewHolder.company.setText(item.user.user_info.company);
        if(item.user.user_info.sex.equals("0")){
            myViewHolder.sex.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.men));
        }else{
            myViewHolder.sex.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.women));
        }
        ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + item.user.user_info.avatar,myViewHolder.head);
        if(StringUtils.isNotEmpty(item.vr_words)){
            List<String> skillName=new ArrayList<>();
            String sp = "[]";
            StringTokenizer pic = new StringTokenizer(item.vr_words,sp);

            while (pic.hasMoreTokens())
                skillName.add(pic.nextToken());
            for(int i=0;i<skillName.size();i++)
               Log.e("skillName",skillName.get(i).toString());
            SkillNameAdapter  skillNameAdapter = new SkillNameAdapter(skillName,mContext);
            myViewHolder.skillGirde.setAdapter(skillNameAdapter);
            skillNameAdapter.notifyDataSetChanged();
        }
        myViewHolder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //放大头像
                final ArrayList<String> pic=new ArrayList<String>();
                pic.add(item.user.user_info.avatar);
                Intent intent=new Intent(mContext, PicStyleActivity.class);
                intent.putExtra(PicStyleActivity.HEAD,true);
                intent.putStringArrayListExtra(PicStyleActivity.PICVIEW_KEY,pic);
                mContext.startActivity(intent);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到下单页面
                Intent intent=new Intent(mContext, PlaceOrderActivity.class);
                intent.putExtra("user_id",item.user_id);
                intent.putExtra("user_name",item.name);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {

        TextView captainName;
        TextView captainOccupation;
        TextView userLv;
        TextView workYears;
        TextView workDuty;
        TextView company;
        GridView skillGirde;
        CircleImageView head;
        ImageView sex;
    }
}
