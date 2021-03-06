package com.xiumi.qirenbao.reward.expert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.widget.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者 ：Created by qianbailu on 2017/3/20.
 * 达人打赏选择金额
 */
public class MasterRewardChoseActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.next)
    TextView next;
    @Bind(R.id.layout1)
    LinearLayout layout1;
    @Bind(R.id.layout2)
    LinearLayout layout2;
    @Bind(R.id.layout3)
    LinearLayout layout3;
    @Bind(R.id.layout4)
    LinearLayout layout4;
    @Bind(R.id.layout5)
    LinearLayout layout5;
    @Bind(R.id.layout6)
    LinearLayout layout6;
    @Bind(R.id.layout7)
    LinearLayout layout7;
    @Bind(R.id.layout8)
    LinearLayout layout8;
    @Bind(R.id.layout9)
    LinearLayout layout9;
    private String cash="5";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_reward_chose);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        layout1.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_select));
        setStatusBar();
    }
    @OnClick(R.id.layout1)
    public void setLayout1(View view){
        setBackgroundNo();
        cash="5";
        layout1.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_select));
    }
    @OnClick(R.id.layout2)
    public void setLayout2(View view){
        setBackgroundNo();
        cash="8";
        layout2.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_select));
    }
    @OnClick(R.id.layout3)
    public void setLayout3(View view){
        setBackgroundNo();
        cash="18";
        layout3.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_select));
    }
    @OnClick(R.id.layout4)
    public void setLayout4(View view){
        setBackgroundNo();
        cash="28";
        layout4.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_select));
    }
    @OnClick(R.id.layout5)
    public void setLayout5(View view){
        setBackgroundNo();
        cash="58";
        layout5.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_select));
    }
    @OnClick(R.id.layout6)
    public void setLayout6(View view){
        setBackgroundNo();
        cash="68";
        layout6.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_select));
    }
    @OnClick(R.id.layout7)
    public void setLayout7(View view){
        setBackgroundNo();
        cash="88";
        layout7.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_select));
    }
    @OnClick(R.id.layout8)
    public void setLayout8(View view){
        setBackgroundNo();
        cash="188";
        layout8.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_select));
    }
    @OnClick(R.id.layout9)
    public void setLayout9(View view){
        setBackgroundNo();
        cash="288";
        layout9.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_select));
    }
    public void setBackgroundNo(){
        layout1.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        layout2.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        layout3.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        layout4.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        layout5.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        layout6.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        layout7.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        layout8.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        layout9.setBackgroundDrawable(getResources().getDrawable(R.color.white));
    }
    @OnClick(R.id.next)
    public void setNext(View view){
        //下一步
        Intent intent=new Intent();
        intent.putExtra("cash", cash);
        setResult(RESULT_OK, intent);
        finish();
    }
    private void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(getResources().getColor(R.color.orangenormal));
    }
}
