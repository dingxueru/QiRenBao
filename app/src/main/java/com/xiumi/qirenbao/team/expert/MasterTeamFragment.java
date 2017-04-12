package com.xiumi.qirenbao.team.expert;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;

import com.xiumi.qirenbao.team.bean.TeamMemberBean;
import com.xiumi.qirenbao.team.partnership.TeamStatisticsFragment;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者 ：Created by DXR on 2017/3/6.
 * 达人-我的团队
 */

public class MasterTeamFragment extends Fragment {

    @Bind(R.id.master_layout)
    LinearLayout master_layout;
    @Bind(R.id.chose_member)
    TextView choseMember;
    @Bind(R.id.chose_activity)
    TextView choseActivity;
    @Bind(R.id.chose_count)
    TextView choseCount;
    private Fragment[] fragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.master_team_layout, container, false);
        ButterKnife.bind(this, view);
        setStatusBar();
        fragments = new Fragment[]{new MyTeamMemberFragment(), new MasterActivityFragment(), new TeamStatisticsFragment()};
        getActivity().getFragmentManager().beginTransaction().replace(R.id.master_layout, fragments[0]).commit();
        return view;
    }
    @OnClick(R.id.chose_member)
    public void choseMember(View view) {
        //成员
        choseMember.setBackgroundResource(R.drawable.master_team_select_border);
        choseMember.setPadding(dip2px(getActivity(), 25), dip2px(getActivity(), 10), dip2px(getActivity(), 25), dip2px(getActivity(), 10));
        choseMember.setTextColor(getResources().getColor(R.color.white));
        Drawable member = getActivity().getResources().getDrawable(R.drawable.master_member_white);
        member.setBounds(0,0,member.getMinimumWidth(),member.getMinimumHeight());
        choseMember.setCompoundDrawables(null,member,null,null);
        choseActivity.setBackgroundResource(R.drawable.master_team_white_border);
        choseActivity.setPadding(dip2px(getActivity(), 25), dip2px(getActivity(), 10), dip2px(getActivity(), 25), dip2px(getActivity(), 10));
        choseActivity.setTextColor(getResources().getColor(R.color.orangenormal));
        Drawable activity = getActivity().getResources().getDrawable(R.drawable.master_activity_orange);
        activity.setBounds(0,0,activity.getMinimumWidth(),activity.getMinimumHeight());
        choseActivity.setCompoundDrawables(null,activity,null,null);
        choseCount.setBackgroundResource(R.drawable.master_team_white_border);
        choseCount.setPadding(dip2px(getActivity(), 25), dip2px(getActivity(), 10), dip2px(getActivity(), 25), dip2px(getActivity(), 10));
        choseCount.setTextColor(getResources().getColor(R.color.orangenormal));
        Drawable count = getActivity().getResources().getDrawable(R.drawable.master_count_orange);
        count.setBounds(0,0,count.getMinimumWidth(),count.getMinimumHeight());
        choseCount.setCompoundDrawables(null,count,null,null);
        getActivity().getFragmentManager().beginTransaction().replace(R.id.master_layout, fragments[0]).commit();
    }

    @OnClick(R.id.chose_activity)
    public void choseActivity(View view) {
        //活动
        choseMember.setBackgroundResource(R.drawable.master_team_white_border);
        choseMember.setPadding(dip2px(getActivity(), 25), dip2px(getActivity(), 10), dip2px(getActivity(), 25), dip2px(getActivity(), 10));
        choseMember.setTextColor(getResources().getColor(R.color.orangenormal));
        Drawable member = getActivity().getResources().getDrawable(R.drawable.master_member_orange);
        member.setBounds(0,0,member.getMinimumWidth(),member.getMinimumHeight());
        choseMember.setCompoundDrawables(null,member,null,null);
        choseActivity.setBackgroundResource(R.drawable.master_team_select_border);
        choseActivity.setPadding(dip2px(getActivity(), 25), dip2px(getActivity(), 10), dip2px(getActivity(), 25), dip2px(getActivity(), 10));
        choseActivity.setTextColor(getResources().getColor(R.color.white));
        Drawable activity = getActivity().getResources().getDrawable(R.drawable.master_activity_white);
        activity.setBounds(0,0,activity.getMinimumWidth(),activity.getMinimumHeight());
        choseActivity.setCompoundDrawables(null,activity,null,null);
        choseCount.setBackgroundResource(R.drawable.master_team_white_border);
        choseCount.setPadding(dip2px(getActivity(), 25), dip2px(getActivity(), 10), dip2px(getActivity(), 25), dip2px(getActivity(), 10));
        choseCount.setTextColor(getResources().getColor(R.color.orangenormal));
        Drawable count = getActivity().getResources().getDrawable(R.drawable.master_count_orange);
        count.setBounds(0,0,count.getMinimumWidth(),count.getMinimumHeight());
        choseCount.setCompoundDrawables(null,count,null,null);
        getActivity().getFragmentManager().beginTransaction().replace(R.id.master_layout, fragments[1]).commit();
    }

    @OnClick(R.id.chose_count)
    public void choseCount(View view) {
        //统计
        choseMember.setBackgroundResource(R.drawable.master_team_white_border);
        choseMember.setPadding(dip2px(getActivity(), 25), dip2px(getActivity(), 10), dip2px(getActivity(), 25), dip2px(getActivity(), 10));
        choseMember.setTextColor(getResources().getColor(R.color.orangenormal));
        Drawable member = getActivity().getResources().getDrawable(R.drawable.master_member_orange);
        member.setBounds(0,0,member.getMinimumWidth(),member.getMinimumHeight());
        choseMember.setCompoundDrawables(null,member,null,null);
        choseActivity.setBackgroundResource(R.drawable.master_team_white_border);
        choseActivity.setPadding(dip2px(getActivity(), 25), dip2px(getActivity(), 10), dip2px(getActivity(), 25), dip2px(getActivity(), 10));
        choseActivity.setTextColor(getResources().getColor(R.color.orangenormal));
        Drawable activity = getActivity().getResources().getDrawable(R.drawable.master_activity_orange);
        activity.setBounds(0,0,activity.getMinimumWidth(),activity.getMinimumHeight());
        choseActivity.setCompoundDrawables(null,activity,null,null);
        choseCount.setBackgroundResource(R.drawable.master_team_select_border);
        choseCount.setPadding(dip2px(getActivity(), 25), dip2px(getActivity(), 10), dip2px(getActivity(), 25), dip2px(getActivity(), 10));
        choseCount.setTextColor(getResources().getColor(R.color.white));
        Drawable count = getActivity().getResources().getDrawable(R.drawable.master_count_white);
        count.setBounds(0,0,count.getMinimumWidth(),count.getMinimumHeight());
        choseCount.setCompoundDrawables(null,count,null,null);
        getActivity().getFragmentManager().beginTransaction().replace(R.id.master_layout, fragments[2]).commit();
    }

    /**
     *  根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *    
     */
    public static int dip2px(Activity activity, float dpValue) {
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);

        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(getResources().getColor(R.color.orangenormal));
    }
}
