package com.xiumi.qirenbao.team.expert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.login.LoginActivity;
import com.xiumi.qirenbao.mine.company.PeopleDetailActivity;
import com.xiumi.qirenbao.team.bean.TeamMemberBean;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.utils.UserInfo;
import com.xiumi.qirenbao.widget.CircleImageView;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 ：Created by DXR on 2017/4/1.
 * 达人-成员详情
 */

public class MasterMemberDetailActivityNew extends Activity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.team_layout)
    LinearLayout team_layout;
    @Bind(R.id.head)
    CircleImageView head;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.occupation)
    TextView occupation;
    @Bind(R.id.quit_team)
    TextView quit_team; // 退出团队

    public static String user_id;
    public static String team_id;
    private MainActivity mactivity;
    private UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_member_detail_layout_new);
        ButterKnife.bind(this);
        userInfo = new UserInfo(this);
        setStatusBar();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            team_id = bundle.getString("team_id");
            user_id = bundle.getString("user_id");
            Log.v("team_user_id", team_id + user_id);
            String user_head = bundle.getString("user_head");
            String user_name = bundle.getString("user_name");
            String user_job = bundle.getString("user_job");
            ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + user_head, head);
            name.setText(user_name);
            occupation.setText(user_job);
        }
        // 退出团队
        if (user_id.equals(MainActivity.id)) {
            quit_team.setVisibility(View.VISIBLE);
            quit_team.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DropOut();
                }
            });
        }
        getFragmentManager().beginTransaction().replace(R.id.team_layout, new MemberDetailFragment()).commit();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // 达人退出团队
    private void DropOut() {
        String url = "https://qrb.shoomee.cn/api/dropOut";
        OkHttpUtils
                .post()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("team_id", team_id)
                .addParams("user_id", user_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MasterMemberDetailActivityNew.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("达人退出团队", obj.toString());
                            String result = obj.getString("result");
                            if (result.equals("success")) {
                                Toast.makeText(MasterMemberDetailActivityNew.this, "退出团队成功", Toast.LENGTH_SHORT).show();
                                // 退出团队后重新登录
                                userInfo.clear();
                                Intent intent=new Intent(MasterMemberDetailActivityNew.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(MasterMemberDetailActivityNew.this, obj.optString("data"), Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MasterMemberDetailActivityNew.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                        }
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

}
