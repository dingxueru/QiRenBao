package com.xiumi.qirenbao.team.expert;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 达人 团队成员
 */
public class MyTeamMemberFragment extends Fragment {

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
    // 队长查看详情
    @Bind(R.id.lead_detail)
    LinearLayout lead_detail;

    private String team_id;
    private String team_name;
    private String team_job;
    private String team_head;
    private String team_userid;
    private String team_mobile;
    private AlertView mAlertView;
    // 团队list
    private ArrayList<TeamMemberBean> memberlist = new ArrayList<TeamMemberBean>();
    private MasterTeamMemberAdapter masterTeamMemberAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.partner_team_member_layout, container, false);
        ButterKnife.bind(this, mView);
        // 获取团队成员列表
//        GetTeamUsers();
        // adapter 初始化
        masterTeamMemberAdapter = new MasterTeamMemberAdapter(getActivity(), memberlist);
        member_list.setAdapter(masterTeamMemberAdapter);
        // 跳转达人详情
        member_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                new AlertView("选择操作", null, null, null, new String[]{"查看详情", "拨号","取消"}, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        switch (position) {
                            case 0:
                                //跳转到成员详情页面
                                Intent intent = new Intent(getActivity(), MasterMemberDetailActivityNew.class);
                                intent.putExtra("team_id", memberlist.get(i).team_id + "");
                                intent.putExtra("user_id", memberlist.get(i).user.user_info.user_id + "");
                                intent.putExtra("user_head", memberlist.get(i).user.user_info.avatar);
                                intent.putExtra("user_name", memberlist.get(i).user.name);
                                intent.putExtra("user_job", memberlist.get(i).user.user_info.work_duty);
                                startActivity(intent);
                                break;
                            case 1:
                                // 拨号
                                new AlertDialog.Builder(getActivity()).setMessage(memberlist.get(i).user.mobile)
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
        // 查看队长详情
        lead_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("选择操作", null, null, null, new String[]{"查看详情", "拨号","取消"}, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        switch (position) {
                            case 0:
                                //跳转到成员详情页面
                                Intent intent = new Intent(getActivity(), MasterMemberDetailActivityNew.class);
                                intent.putExtra("team_id", team_id);
                                intent.putExtra("user_id", team_userid);
                                intent.putExtra("user_head", team_head);
                                intent.putExtra("user_name", team_name);
                                intent.putExtra("user_job", team_job);
                                startActivity(intent);
                                break;
                            case 1:
                                // 拨号
                                new AlertDialog.Builder(getActivity()).setMessage(team_mobile)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + team_mobile));
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
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.v("达人-团队成员列表", object.toString());
                            if (object.getString("result").equals("success")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                ArrayList<TeamMemberBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<TeamMemberBean>>() {
                                });
                                team_title.setText(temp.get(0).user.name + "的团队");
                                for (int i = 0; i < temp.size(); i++) {
                                    if (temp.get(i).type == 1) {
                                        if (!TextUtils.isEmpty(temp.get(i).user.name)) {
                                            lead_name.setText(temp.get(i).user.name);
                                            team_id = temp.get(i).team_id + "";
                                            team_userid = temp.get(i).user.id + "";
                                            team_name = temp.get(i).user.name;
                                            team_head = temp.get(i).user.user_info.avatar;
                                            team_job = temp.get(i).user.user_info.work_duty;
                                            team_mobile = temp.get(i).user.mobile;
                                            if (temp.get(i).user.user_info != null) {
                                                ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + temp.get(i).user.user_info.avatar, lead_avatar);
                                                lead_job.setText(temp.get(i).user.user_info.work_duty);
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
                                    } else if (temp.size() == 0) {
                                        Toast.makeText(getActivity(), "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }
}
