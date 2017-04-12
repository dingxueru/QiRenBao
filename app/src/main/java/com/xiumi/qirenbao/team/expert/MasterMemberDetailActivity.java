package com.xiumi.qirenbao.team.expert;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.widget.CircleImageView;
import com.xiumi.qirenbao.widget.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者 ：Created by DXR on 2017/3/20.
 * 达人-成员详情
 */

public class MasterMemberDetailActivity extends Activity {

    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.first)
    TextView first;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.second)
    TextView second;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.third)
    TextView third;
    @Bind(R.id.line3)
    View line3;
    @Bind(R.id.team_layout)
    LinearLayout team_layout;
    @Bind(R.id.head)
    CircleImageView head;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.occupation)
    TextView occupation;
    private Fragment[] fragments;
    public static String user_id;
    public static String team_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_member_detail_layout);
        ButterKnife.bind(this);
        setStatusBar();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            team_id = bundle.getString("team_id");
            user_id = bundle.getString("user_id");
            String user_head = bundle.getString("user_head");
            String user_name = bundle.getString("user_name");
            String user_job = bundle.getString("user_job");
            ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + user_head, head);
            name.setText(user_name);
            occupation.setText(user_job);
        }
        fragments = new Fragment[]{new MemberDetailFragment(), new MemberOrderFragment(), new MasterContributionFragment()};
        getFragmentManager().beginTransaction().replace(R.id.team_layout, fragments[0]).commit();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(getResources().getColor(R.color.orangenormal));
    }
    @OnClick(R.id.first)
    public void setFirst(View view){
        first.setTextColor(getResources().getColor(R.color.orangenormal));
        line1.setVisibility(View.VISIBLE);
        second.setTextColor(getResources().getColor(R.color.black));
        line2.setVisibility(View.GONE);
        third.setTextColor(getResources().getColor(R.color.black));
        line3.setVisibility(View.GONE);
        getFragmentManager().beginTransaction().replace(R.id.team_layout, fragments[0]).commit();
    }

    @OnClick(R.id.second)
    public void setSecond(View view){
        first.setTextColor(getResources().getColor(R.color.black));
        line1.setVisibility(View.GONE);
        second.setTextColor(getResources().getColor(R.color.orangenormal));
        line2.setVisibility(View.VISIBLE);
        third.setTextColor(getResources().getColor(R.color.black));
        line3.setVisibility(View.GONE);
        getFragmentManager().beginTransaction().replace(R.id.team_layout, fragments[1]).commit();
    }
    @OnClick(R.id.third)
    public void setThird(View view){
        first.setTextColor(getResources().getColor(R.color.black));
        line1.setVisibility(View.GONE);
        second.setTextColor(getResources().getColor(R.color.black));
        line2.setVisibility(View.GONE);
        third.setTextColor(getResources().getColor(R.color.orangenormal));
        line3.setVisibility(View.VISIBLE);
        getFragmentManager().beginTransaction().replace(R.id.team_layout, fragments[2]).commit();
    }
}
