package com.xiumi.qirenbao.team.partnership;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.team.adapter.TeamAdapter;
import com.xiumi.qirenbao.widget.ListViewForScrollView;
import com.xiumi.qirenbao.widget.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qianbailu on 2017/3/30.
 */
public class PeopeleActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.base_list)
    ListViewForScrollView base_list;
    private TeamAdapter teamAdapter = null;
    private List<String> name = new ArrayList<>(), head = new ArrayList<>(), sex = new ArrayList<>(), work_duty = new ArrayList<>(), growth_value = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peopele);
        ButterKnife.bind(this);
        setStatusBar();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        name = getIntent().getStringArrayListExtra("name");
        head = getIntent().getStringArrayListExtra("head");
        sex = getIntent().getStringArrayListExtra("sex");
        work_duty = getIntent().getStringArrayListExtra("work_duty");
        growth_value = getIntent().getStringArrayListExtra("growth_value");
        // adapter 初始化
        teamAdapter = new TeamAdapter(this, name, head, sex, work_duty, growth_value);
        base_list.setAdapter(teamAdapter);
        teamAdapter.notifyDataSetChanged();
    }

    protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(getResources().getColor(R.color.title_bg));
    }
}
