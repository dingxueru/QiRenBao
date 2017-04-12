package com.xiumi.qirenbao.team.expert;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.mine.company.PeopleDetailActivity;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 ：Created by DXR on 2017/3/28.
 * 达人申请加入团队
 */

public class MasterApplyJoinTeamActivity extends AppCompatActivity {

    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.apply_message)
    EditText apply_message;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.submit)
    TextView submit;
    // 团队id
    private int team_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_apply_join_team_layout);
        ButterKnife.bind(this);
        setStatusBar();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            team_id = bundle.getInt("team_id");
        }

        // back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // cancel
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // submit
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("MainActivity.class_id",MainActivity.class_id);
                if (!MainActivity.class_id.equals("null")) {
                    ApplyJoin(team_id);
                } else {
                    // 跳转完善信息
                    new AlertDialog.Builder(MasterApplyJoinTeamActivity.this).setMessage("加入团队必须填写自己的服务分类名称！").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(MasterApplyJoinTeamActivity.this, PeopleDetailActivity.class);
                            startActivity(intent);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
                }
            }
        });
    }

    /**
     * 达人申请加入团队
     */
    private void ApplyJoin(int team_id) {
        String url = "https://qrb.shoomee.cn/api/applyJoin";
        OkHttpUtils
                .post()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("team_id", team_id + "")
                .addParams("user_id", MainActivity.id)
                .addParams("comment", apply_message.getText().toString().trim())
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MasterApplyJoinTeamActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.v("达人申请加入团队", object.toString());
                            if (object.getString("result").equals("success")) {
                                Toast.makeText(MasterApplyJoinTeamActivity.this, "申请成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                finish();
                                Toast.makeText(MasterApplyJoinTeamActivity.this, "已申请，请等待队长同意", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MasterApplyJoinTeamActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
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
