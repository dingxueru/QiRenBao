package com.xiumi.qirenbao.team.expert;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.team.adapter.MasterContributionAdapter;
import com.xiumi.qirenbao.team.bean.ContributeBean;
import com.xiumi.qirenbao.widget.CustomerListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 ：Created by DXR on 2017/3/20.
 * 达人-贡献
 */

public class MasterContributionFragment extends Fragment {

    private MasterContributionAdapter masterContributionAdapter = null;
    private ArrayList<ContributeBean> contributelist = new ArrayList<ContributeBean>();
    @Bind(R.id.base_list)
    CustomerListView base_list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_list_layout, container, false);
        ButterKnife.bind(this, view);
        // adapter 初始化
        masterContributionAdapter = new MasterContributionAdapter(getActivity(), contributelist);
        base_list.setAdapter(masterContributionAdapter);
        // 获取贡献数据
        TeamUserContribute();

        return view;
    }

    /**
     * 团员详情-贡献
     */
    private void TeamUserContribute() {

        String url = "https://qrb.shoomee.cn/api/teamUserContribute?service_user_id=" + MasterMemberDetailActivity.user_id;
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
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
                            Log.e("团员详情-贡献", object.toString());
                            if (object.getString("result").equals("success")) {
                                JSONObject json = object.optJSONObject("data");
                                JSONArray jsonArray = json.optJSONArray("data");
                                ArrayList<ContributeBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<ContributeBean>>() {
                                });
                                if (temp.size() > 0) {
                                    contributelist.addAll(temp);
                                    masterContributionAdapter.notifyDataSetChanged();
                                } else if (temp.size() == 0) {
                                    Toast.makeText(getActivity(), "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "数据解析错误", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
