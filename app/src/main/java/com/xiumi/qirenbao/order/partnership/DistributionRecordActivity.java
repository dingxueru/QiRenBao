package com.xiumi.qirenbao.order.partnership;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.order.adapter.DistributionRecordAdapter;
import com.xiumi.qirenbao.order.bean.PartnerOrderListBean;
import com.xiumi.qirenbao.widget.CustomerListView;
import com.xiumi.qirenbao.widget.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 ：Created by DXR on 2017/3/30.
 * 分配记录
 */

public class DistributionRecordActivity extends Activity {

    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.record_list)
    CustomerListView record_list;

    private DistributionRecordAdapter distributionRecordAdapter = null;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    public static String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partner_distribution_record);
        ButterKnife.bind(this);
        setStatusBar();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 接收记录
        name = getIntent().getStringArrayListExtra("name");
        time = getIntent().getStringArrayListExtra("time");
        title = getIntent().getStringExtra("title");
        Log.v("receive_name_time",name.toString() + time.toString() + title);
        // adapter 初始化
        distributionRecordAdapter = new DistributionRecordAdapter(DistributionRecordActivity.this, name,time,title);
        record_list.setAdapter(distributionRecordAdapter);
        distributionRecordAdapter.notifyDataSetChanged();
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
