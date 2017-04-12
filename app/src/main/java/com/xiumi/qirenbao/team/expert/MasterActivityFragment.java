package com.xiumi.qirenbao.team.expert;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.team.adapter.ActivityListAdapter;
import com.xiumi.qirenbao.team.bean.ActivityListBean;
import com.xiumi.qirenbao.widget.CustomerListView;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.xiumi.qirenbao.widget.pullableView.PullableScrollView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 ：Created by DXR on 2017/3/6.
 * 达人 我的团队活动
 */

public class MasterActivityFragment extends Fragment {
    @Bind(R.id.activity_list)
    CustomerListView activity_list;
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView;
    @Bind(R.id.pull_to_scrollview)
    PullableScrollView pullableScrollView;
    @Bind(R.id.empty_img)
    LinearLayout empty_img;
    // 活动列表
    private ArrayList<ActivityListBean> activityList = new ArrayList<ActivityListBean>();
    private ActivityListAdapter activityListAdapter = null;
    // 分页
    private int index = 1;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.master_activity_layout, container, false);
        ButterKnife.bind(this, view);
        // 获取活动列表
        GetActivityList(1);
        // adapter 初始化
        activityListAdapter = new ActivityListAdapter(getActivity(), activityList,mPullRefreshScrollView);
        activity_list.setAdapter(activityListAdapter);

        /**
         * 刷新
         */
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                index = 1;
                activityList.clear();
                GetActivityList(index);
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
                GetActivityList(index);
                // 告诉控件刷新完毕
                try {
                    mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    /**
     * 获取团队活动列表
     */
    private void GetActivityList(final int page) {
        String url = "https://qrb.shoomee.cn/api/activityList?team_id=" + MainActivity.team_id + "&page=" + page;
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
                            Log.v("获取团队活动列表", object.toString());
                            Log.v("activity_index",index + "");
                            if (object.getString("result").equals("success")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                ArrayList<ActivityListBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<ActivityListBean>>() {
                                });
                                // 再无下拉刷新之前，防止每次请求重复
                                activityList.clear();
                                if (temp.size() > 0) {
                                    empty_img.setVisibility(View.GONE);
                                    activityList.addAll(temp);
                                    activityListAdapter.notifyDataSetChanged();
                                } else if (temp.size() == 0 && index > 1) {
                                    empty_img.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                }else {
                                    empty_img.setVisibility(View.VISIBLE);
                                }
                            } else if (index == 1){
//                                Toast.makeText(getActivity(), "暂无团队活动", Toast.LENGTH_SHORT).show();
                                empty_img.setVisibility(View.VISIBLE);
                                mPullRefreshScrollView.setVisibility(View.GONE);
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
