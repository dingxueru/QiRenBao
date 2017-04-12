package com.xiumi.qirenbao.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.widget.CircleImageView;
import com.xiumi.qirenbao.widget.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterFirstActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.next)
    TextView next;
    @Bind(R.id.company_layout)
    CircleImageView companyLayout;
    @Bind(R.id.people_layout)
    CircleImageView peopleLayout;
    private int  chose=1;//2是达人1是企业主
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_first);
        ButterKnife.bind(this);
        setStatusBar();
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

        tintManager.setTintColor(getResources().getColor(R.color.title_bg));
    }
    @OnClick(R.id.next)
    public void nextPage(View view){
        Intent intent = new Intent(RegisterFirstActivity.this,RegisterActivity.class);
        intent.putExtra("chose",chose);
        startActivity(intent);
    }
    @OnClick(R.id.people_layout)
    public void setPeopleChose(View view){
        chose=2;
        companyLayout.setImageDrawable(getResources().getDrawable(R.drawable.unchose_company));
        peopleLayout.setImageDrawable(getResources().getDrawable(R.drawable.chose_export));
    }
    @OnClick(R.id.company_layout)
    public void setCompanyChose(View view){
        chose=1;
        companyLayout.setImageDrawable(getResources().getDrawable(R.drawable.chose_company));
        peopleLayout.setImageDrawable(getResources().getDrawable(R.drawable.unchose_export));
    }
}
