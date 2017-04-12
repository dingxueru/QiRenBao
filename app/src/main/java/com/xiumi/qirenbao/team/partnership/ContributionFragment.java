package com.xiumi.qirenbao.team.partnership;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.mygift.partnership.PartnerGiftActivity;
import com.xiumi.qirenbao.reward.bean.GiftBean;
import com.xiumi.qirenbao.team.adapter.MasterContributionAdapter;
import com.xiumi.qirenbao.team.bean.ContributeBean;
import com.xiumi.qirenbao.team.partnership.ParnerMemberDetailActivity;
import com.xiumi.qirenbao.widget.CustomerListView;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.xiumi.qirenbao.widget.pullableView.PullableScrollView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qianbailu on 2017/1/25.
 * 合伙人 团队贡献
 */
public class ContributionFragment extends Fragment {
    MasterContributionAdapter masterContributionAdapter = null;
    private ArrayList<ContributeBean> contributelist = new ArrayList<ContributeBean>();
    @Bind(R.id.base_list)
    CustomerListView base_list;
    @Bind(R.id.empty_img)
    LinearLayout empty_img;
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView;
    @Bind(R.id.pull_to_scrollview)
    PullableScrollView pullableScrollView;
    private ArrayList<String> name = new ArrayList<>();
    private int index = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.contribution_layout, container, false);
        ButterKnife.bind(this, mView);
        // 获取打赏的礼物类别
        GetAllGifts();
        // adapter 初始化
        masterContributionAdapter = new MasterContributionAdapter(getActivity(), contributelist, name);
        base_list.setAdapter(masterContributionAdapter);
        // 获取贡献数据
        teamUserContribute(1);
        /**
         * 刷新
         */
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                index = 1;
                contributelist.clear();
                teamUserContribute(1);
                // 告诉控件刷新完毕
                try {
                    mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                index++;
                teamUserContribute(index);
                // 告诉控件刷新完毕
                try {
                    mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return mView;
    }

    /**
     * 获取打赏的礼物类别
     */
    private void GetAllGifts() {
        String url = "https://qrb.shoomee.cn/qrb_api/getAllGifts";
        OkHttpUtils
                .get()
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
                            JSONObject obj = new JSONObject(response);
                            Log.v("获取打赏的礼物类别", obj.toString());
                            if (obj.optString("result").equals("success")) {
                                JSONArray jsonArray = obj.optJSONArray("data");
                                ArrayList<GiftBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<GiftBean>>() {
                                });
                                if (temp.size() > 0) {

                                    for (int i = 0; i < temp.size(); i++) {
                                        name.add(temp.get(i).name);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "数据解析错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 团员详情-贡献
     */
    private void teamUserContribute(final int page) {
        String url = "https://qrb.shoomee.cn/api/teamUserContribute?service_user_id=" + ParnerMemberDetailActivity.user_id + "&team_id=" + MainActivity.team_id + "&page=" + page;
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
                                contributelist.clear();
                                if (temp.size() > 0) {
                                    empty_img.setVisibility(View.GONE);
                                    contributelist.addAll(temp);
                                    masterContributionAdapter.notifyDataSetChanged();
                                } else if (temp.size() == 0 && index >1) {
                                    Toast.makeText(getActivity(), "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                }else {
                                    empty_img.setVisibility(View.VISIBLE);
                                }
                            } else if (index == 1) {
                                empty_img.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (index == 1) {
                                empty_img.setVisibility(View.VISIBLE);
                                mPullRefreshScrollView.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }
}
