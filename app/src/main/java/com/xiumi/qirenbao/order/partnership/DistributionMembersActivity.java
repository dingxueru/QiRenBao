package com.xiumi.qirenbao.order.partnership;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.BaseBundle;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.order.adapter.DistributionMemberAdapter;
import com.xiumi.qirenbao.team.adapter.MasterTeamMemberAdapter;
import com.xiumi.qirenbao.team.bean.TeamMemberBean;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.widget.CustomerGridView;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 ：Created by DXR on 2017/3/18.
 * 分配团员
 */

public class DistributionMembersActivity extends Activity {

    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.team_address)
    TextView team_address;
    @Bind(R.id.lead_avatar)
    ImageView lead_avatar;
    @Bind(R.id.lead_name)
    TextView lead_name;
    @Bind(R.id.lead_job)
    TextView lead_job;
    @Bind(R.id.member_list)
    CustomerGridView member_list;
    @Bind(R.id.team_name)
    TextView team_name;
    @Bind(R.id.submit)
    TextView submit;
    // 队长是否被点击
    @Bind(R.id.clicked)
    TextView clicked;
    private int userID;
    private String avtar, useName, useJob;

    private int order_id; // 订单id
    // 团队成员list
    private ArrayList<TeamMemberBean> memberlist = new ArrayList<TeamMemberBean>();
    private DistributionMemberAdapter distributionMemberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distribution_member_layout);
        ButterKnife.bind(this);
        setStatusBar();
        // 获取订单id
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            order_id = bundle.getInt("order_id");
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // adapter 初始化
        distributionMemberAdapter = new DistributionMemberAdapter(DistributionMembersActivity.this, memberlist, order_id);
        member_list.setAdapter(distributionMemberAdapter);
        // 获取团队成员列表
        GetTeamUsers();

        // 设置是否选中状态
        member_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < memberlist.size(); i++) {
                    if (i == position) {
                        memberlist.get(i).ischeck = true;
                        clicked.setBackgroundDrawable(getResources().getDrawable(R.drawable.distribution_no_select));
                    } else {
                        memberlist.get(i).ischeck = false;
                    }
                }
                distributionMemberAdapter.notifyDataSetChanged();
                userID = memberlist.get(position).user.user_info.user_id;
            }
        });
        // 判断团长是否被点击
        clicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked.setBackgroundDrawable(getResources().getDrawable(R.drawable.distribution_is_select));
                for (int i = 0; i < memberlist.size(); i++) {
                    memberlist.get(i).ischeck = false;
                }
                distributionMemberAdapter.notifyDataSetChanged();
            }
        });
        // 确认分配
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DistributionMembersActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确认分配？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (String.valueOf(userID).equals(MainActivity.id)) {
                            // 自己接单
                            ReceiveOrder(order_id);
                        } else {
                            // 分配团员
                            AllotMember(userID);
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
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

    /**
     * 合伙人接单
     */

    private void ReceiveOrder(int order_id) {
        String url = "https://qrb.shoomee.cn/api/receiveOrder";
        OkHttpUtils
                .post()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("order_id", order_id + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(DistributionMembersActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        Log.v("Exception", e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.v("合伙人接单", object.toString());
                            // 待数据完善，添加页面逻辑交互
                            if (object.optString("result").equals("success")) {
                                Toast.makeText(DistributionMembersActivity.this, "接单成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(DistributionMembersActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(DistributionMembersActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }

    /**
     * 合伙人分配订单给团员
     */
    private void AllotMember(int service_user_id) {
        String url = "https://qrb.shoomee.cn/api/allotOrder";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("order_id", order_id + "")
                .addParams("service_user_id", service_user_id + "")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(DistributionMembersActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.v("合伙人分配订单给团员", object.toString());
                            if (object.getString("result").equals("success")) {
                                Toast.makeText(DistributionMembersActivity.this, "分配成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(DistributionMembersActivity.this, "分配失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(DistributionMembersActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                            Log.e("Exception", e.toString());
                        }
                    }
                });

    }

    /**
     * 获取团队成员列表
     */
    private void GetTeamUsers() {
        String url = "https://qrb.shoomee.cn/api/teamUsers?team_id=" + MainActivity.team_id;
        OkHttpUtils
                .get()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(DistributionMembersActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.e("合伙人-团队成员列表", object.toString());
                            if (object.getString("result").equals("success")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                ArrayList<TeamMemberBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<TeamMemberBean>>() {
                                });
                                for (int i = 0; i < temp.size(); i++) {
                                    if (temp.get(i).type == 1) {
                                        if (!TextUtils.isEmpty(temp.get(i).user.name)) {
                                            lead_name.setText(temp.get(i).user.name);
                                            team_name.setText(temp.get(i).user.name + "的团队");
                                            userID = temp.get(i).user.user_info.user_id;
                                            avtar = temp.get(i).user.user_info.avatar;
                                            useName = temp.get(i).user.name;
                                            useJob = temp.get(i).user.user_info.work_title;
                                            Log.e("userID", temp.get(i).user.user_info.user_id + "");
                                            if (temp.get(i).user.user_info != null) {
                                                ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + temp.get(i).user.user_info.avatar, lead_avatar);
                                                lead_job.setText(temp.get(i).user.user_info.work_title);
                                            }
                                        }
                                        // 移除队长
                                        temp.remove(i);
                                    }
                                    // 再无下拉刷新之前，防止每次请求重复
                                    memberlist.clear();
                                    Log.e("队员size", temp.size() + "");
                                    if (temp.size() > 0) {
                                        memberlist.addAll(temp);
                                        distributionMemberAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                Toast.makeText(DistributionMembersActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(DistributionMembersActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                            Log.e("Exception", e.toString());
                        }
                    }
                });
    }

}
