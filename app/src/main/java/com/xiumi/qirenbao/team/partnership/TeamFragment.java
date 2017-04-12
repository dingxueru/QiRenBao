package com.xiumi.qirenbao.team.partnership;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.widget.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qianbailu on 2017/1/20.
 * [合伙]我的团队[成员]
 */
public class TeamFragment  extends Fragment {
    @Bind(R.id.layout_team)
    LinearLayout layoutTeam;
    @Bind(R.id.chose_member)
    RadioButton choseMember;
    @Bind(R.id.chose_apply)
    RadioButton choseApply;
    @Bind(R.id.chose_activity)
    RadioButton choseActivity;
    @Bind(R.id.chose_count)
    RadioButton choseCount;
    @Bind(R.id.layout_nav)
    RadioGroup layoutNav;
    private Fragment[] fragments;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.setting_layout, container, false);
        ButterKnife.bind(this, mView);
        setStatusBar();
        fragments = new Fragment[]{new PartnerMemberFragment(),new TeamApplyFragment(),new ActivityFragment(),new TeamStatisticsFragment()};
        getActivity().getFragmentManager().beginTransaction().replace(R.id.layout_team, fragments[0]).commit();
        layoutNav.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.chose_member:
                        //成员
                        getActivity().getFragmentManager().beginTransaction().replace(R.id.layout_team, fragments[0]).commit();
                        break;
                    case R.id.chose_apply:
                        //申请
                        getActivity().getFragmentManager().beginTransaction().replace(R.id.layout_team, fragments[1]).commit();
                        break;
                    case R.id.chose_activity:
                        //活动
                        getActivity().getFragmentManager().beginTransaction().replace(R.id.layout_team, fragments[2]).commit();
                        break;
                    case R.id.chose_count:
                        //统计
                        getActivity().getFragmentManager().beginTransaction().replace(R.id.layout_team, fragments[3]).commit();
                        break;
                }
            }
        });
        return mView;
    }
      protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(getResources().getColor(R.color.title_bg));
    }
}
