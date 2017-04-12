package com.xiumi.qirenbao.team.expert;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.team.bean.TeamMemberBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 ：Created by DXR on 2017/3/20.
 * 达人成员详细信息
 */

public class MemberDetailFragment extends Fragment {

    @Bind(R.id.work_years)
    TextView workYears;
    @Bind(R.id.user_sex)
    TextView userSex;
    @Bind(R.id.work_title)
    TextView workTitle;
    @Bind(R.id.work_duty)
    TextView workDuty;
    @Bind(R.id.user_address)
    TextView userAddress;
    @Bind(R.id.description)
    TextView description;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.member_detail_layout, container, false);
        ButterKnife.bind(this, view);
        /**
         * 团队成员-成员详情
         */
        TeamUserInfo();
        return view;
    }

    /**
     * 团队成员-成员信息详情
     */

    private void TeamUserInfo() {
        String url = "https://qrb.shoomee.cn/api/teamUserInfo?team_id=" + MasterMemberDetailActivityNew.team_id + "&user_id=" + MasterMemberDetailActivityNew.user_id;
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
                            JSONObject obj = new JSONObject(response);
                            Log.e("团队成员-成员详情", obj.toString());
                            String result = obj.getString("result");
                            if (result.equals("success")) {
                                Log.e("data", obj.optJSONArray("data").toString());
                                List<TeamMemberBean> temp = JSON.parseObject(obj.optJSONArray("data").toString(), new TypeReference<ArrayList<TeamMemberBean>>() {
                                });
                                TeamMemberBean teamMemberBean = temp.get(0);
                                if (!TextUtils.isEmpty(teamMemberBean.user.user_info.work_years)) {
                                    workYears.setText(teamMemberBean.user.user_info.work_years + "年");
                                }
                                if (teamMemberBean.user.user_info.sex == 1) {
                                    userSex.setText("男");
                                } else if (teamMemberBean.user.user_info.sex == 2) {
                                    userSex.setText("女");
                                }
                                if (!TextUtils.isEmpty(teamMemberBean.user.user_info.work_title)) {
                                    workTitle.setText(teamMemberBean.user.user_info.work_title);
                                }
                                if (!TextUtils.isEmpty(teamMemberBean.user.user_info.work_duty)) {
                                    workDuty.setText(teamMemberBean.user.user_info.work_duty);
                                }
                                if (!TextUtils.isEmpty(teamMemberBean.user.user_info.company)) {
                                    userAddress.setText(teamMemberBean.user.user_info.company);
                                }
                                if (!TextUtils.isEmpty(teamMemberBean.user.user_info.description)) {
                                    description.setText(teamMemberBean.user.user_info.description);
                                }

                            } else {

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "数据解析错误", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
