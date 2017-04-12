package com.xiumi.qirenbao.team.partnership;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiumi.qirenbao.R;

/**
 * Created by qianbailu on 2017/2/5.
 * [合伙]我的团队-统计
 */
public class TeamStatisticsFragment  extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.statistics_layout, container, false);
        return mView;
    }
}
