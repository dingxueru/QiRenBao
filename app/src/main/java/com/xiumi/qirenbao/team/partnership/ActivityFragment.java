package com.xiumi.qirenbao.team.partnership;

import android.app.Fragment;
import android.content.Intent;
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
import com.xiumi.qirenbao.team.adapter.ActivityAdapter;
import com.xiumi.qirenbao.team.bean.ActivityListBean;
import com.xiumi.qirenbao.widget.CustomerListView;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.xiumi.qirenbao.widget.pullableView.PullableScrollView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qianbailu on 2017/1/25.
 * [合伙]我的团队---活动
 */
public class ActivityFragment extends Fragment {

    @Bind(R.id.add_activity)
    ImageView addActivity;
    @Bind(R.id.activity_list)
    CustomerListView activity_list;
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView;
    @Bind(R.id.empty_img)
    LinearLayout empty_img;
    // 活动列表
    private ArrayList<ActivityListBean> activityList = new ArrayList<ActivityListBean>();
    private ActivityAdapter activityListAdapter = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.activity_layout, container, false);
        ButterKnife.bind(this, mView);
        // adapter 初始化
        activityListAdapter = new ActivityAdapter(getActivity(),activityList,mPullRefreshScrollView);
        activity_list.setAdapter(activityListAdapter);

        /**
         * 刷新
         */
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 获取活动列表
                activityList.clear();
                GetActivityList();
                // 告诉控件刷新完毕
                try {
                    mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
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
    @Override
    public void onResume(){
        super.onResume();
        // 获取活动列表
        activityList.clear();
        GetActivityList();
    }
    /**
     * 获取团队活动列表
     */
    private void GetActivityList() {
        String url = "https://qrb.shoomee.cn/api/activityList?team_id=" + MainActivity.team_id;
        OkHttpUtils
                .get()
                .addHeader("Accept","application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(),"网络异常",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.e("获取团队活动列表",object.toString());
                            if (object.getString("result").equals("success")) {
                                empty_img.setVisibility(View.GONE);
                                JSONArray jsonArray = object.getJSONArray("data");
                                ArrayList<ActivityListBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<ActivityListBean>>() {
                                });
                                // 再无下拉刷新之前，防止每次请求重复
                                if (temp.size() > 0) {
                                    activityList.addAll(temp);
                                    activityListAdapter.notifyDataSetChanged();
                                }else {
                                    empty_img.setVisibility(View.VISIBLE);
                                }
                            } else {
                                empty_img.setVisibility(View.VISIBLE);
                              }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }
    @OnClick(R.id.add_activity)
    public void setAddActivity(View view){
        //添加活动
        Intent intent=new Intent(getActivity(),AddActivity.class);
        startActivity(intent);
    }
}
