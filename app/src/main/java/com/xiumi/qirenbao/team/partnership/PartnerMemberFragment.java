package com.xiumi.qirenbao.team.partnership;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.team.adapter.MasterTeamMemberAdapter;
import com.xiumi.qirenbao.team.bean.TeamMemberBean;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.widget.CustomerGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qianbailu on 2017/3/9.
 * 合伙人-我的团队成员
 */

public class PartnerMemberFragment extends Fragment {
    @Bind(R.id.team_address)
    TextView team_address;
    @Bind(R.id.team_title)
    TextView team_title;
    @Bind(R.id.lead_avatar)
    ImageView lead_avatar;
    @Bind(R.id.lead_name)
    TextView lead_name;
    @Bind(R.id.lead_job)
    TextView lead_job;
    @Bind(R.id.team_list)
    CustomerGridView member_list;
    // 团队list
    private ArrayList<TeamMemberBean> memberlist = new ArrayList<TeamMemberBean>();
    private MasterTeamMemberAdapter masterTeamMemberAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.partner_team_member_layout, container, false);
        ButterKnife.bind(this, mView);
        // adapter 初始化
        masterTeamMemberAdapter = new MasterTeamMemberAdapter(getActivity(), memberlist);
        member_list.setAdapter(masterTeamMemberAdapter);
        // 跳转达人详情
        member_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                new AlertView("选择操作", null, null, null, new String[]{"查看详情", "踢出团队", "拨号", "取消"},
                        getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        switch (position) {
                            case 0:
                                //跳转到成员详情页面
                                Intent intent = new Intent(getActivity(), ParnerMemberDetailActivity.class);
                                intent.putExtra("team_id", memberlist.get(i).team_id + "");
                                intent.putExtra("user_id", memberlist.get(i).user.user_info.user_id + "");
                                intent.putExtra("user_head", memberlist.get(i).user.user_info.avatar);
                                intent.putExtra("user_name", memberlist.get(i).user.name);
                                intent.putExtra("user_job", memberlist.get(i).user.user_info.work_duty);
                                intent.putExtra("login_type", memberlist.get(i).user.login_type);
                                intent.putExtra("sex", memberlist.get(i).user.user_info.sex);
                                intent.putExtra("growth_value", memberlist.get(i).user.user_info.growth_value);
                                startActivity(intent);
                                break;
                            case 1:
                                // 剔出团队
                                new android.app.AlertDialog.Builder(getActivity()).setMessage("确认剔出团队？")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                kickOut(memberlist.get(i).user.user_info.user_id + "");
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                                break;
                            case 2:
                                // 拨号
                                new android.app.AlertDialog.Builder(getActivity()).setMessage(memberlist.get(i).user.mobile)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + memberlist.get(i).user.mobile));
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                getActivity().startActivity(intent);
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                                break;
                            default:
                                break;
                        }
                    }
                }).show();

            }
        });
        return mView;
    }

    /**
     * 合伙人将达人踢出团队
     */
    private void kickOut(final String user_id) {
        String url = "https://qrb.shoomee.cn//api/kickOut";
        OkHttpUtils
                .post()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("team_id", MainActivity.team_id)
                .addParams("user_id", user_id)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(), "网络不给力,请重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("合伙人将达人踢出团队", obj.toString());
                            String result = obj.getString("result");
                            if (result.equals("success")) {

                                new AlertDialog.Builder(getActivity()).setMessage("踢出团队成功")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // 刷新队员
                                                memberlist.clear();
                                                // 获取团队成员列表
                                                GetTeamUsers();
//                                                masterTeamMemberAdapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "数据解析错误", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onResume() {
        // 获取团队成员列表
        GetTeamUsers();
        super.onResume();
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
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.v("达人-团队成员列表", object.toString());
                            if (object.getString("result").equals("success")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                final ArrayList<TeamMemberBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<TeamMemberBean>>() {
                                });
                                team_title.setText(temp.get(0).user.name + "的团队");
                                for (int i = 0; i < temp.size(); i++) {
                                    if (temp.get(i).type == 1) {
                                        if (!TextUtils.isEmpty(temp.get(i).user.name)) {
                                            lead_name.setText(temp.get(i).user.name);
                                            final int team_id = temp.get(i).team_id;
                                            final int user_id = temp.get(i).user.user_info.user_id;
                                            final String avatar = temp.get(i).user.user_info.avatar;
                                            final String user_name = temp.get(i).user.name;
                                            final String work_title = temp.get(i).user.user_info.work_duty;
                                            final int login_type = temp.get(i).user.login_type;
                                            final int sex = temp.get(i).user.user_info.sex;
                                            final int growth_value = temp.get(i).user.user_info.growth_value;
                                            if (temp.get(i).user.user_info != null) {
                                                ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + temp.get(i).user.user_info.avatar, lead_avatar);
                                                lead_job.setText(temp.get(i).user.user_info.work_duty);
                                                lead_avatar.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(getActivity(), ParnerMemberDetailActivity.class);
                                                        intent.putExtra("team_id", team_id + "");
                                                        intent.putExtra("user_id", user_id + "");
                                                        intent.putExtra("user_head", avatar);
                                                        intent.putExtra("user_name", user_name);
                                                        intent.putExtra("user_job", work_title);
                                                        intent.putExtra("login_type", login_type);
                                                        intent.putExtra("sex", sex);
                                                        intent.putExtra("growth_value", growth_value);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        }
                                        // 移除队长
                                        temp.remove(i);
                                    }
                                    // 再无下拉刷新之前，防止每次请求重复
                                    memberlist.clear();
                                    Log.v("队员size", temp.size() + "");
                                    if (temp.size() > 0) {
                                        memberlist.addAll(temp);
                                        masterTeamMemberAdapter.notifyDataSetChanged();
                                    } else {
                                        masterTeamMemberAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
