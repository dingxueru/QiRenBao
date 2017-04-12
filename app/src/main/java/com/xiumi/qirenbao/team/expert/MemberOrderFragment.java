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
import com.xiumi.qirenbao.order.bean.PartnerOrderListBean;
import com.xiumi.qirenbao.team.adapter.MasterDetailOrderAdapter;
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
 * 作者 ：Created by DXR on 2017/3/20.
 * 达人-团员详情-订单
 */

public class MemberOrderFragment extends Fragment {
    private int index = 1;
    @Bind(R.id.base_list)
    CustomerListView base_list;
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView;
    @Bind(R.id.pull_to_scrollview)
    PullableScrollView pullableScrollView;

    private MasterDetailOrderAdapter masterDetailOrderAdapter = null;
    private ArrayList<PartnerOrderListBean> orderlist = new ArrayList<PartnerOrderListBean>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_list_layout, container, false);
        ButterKnife.bind(this, view);

        // 团队达人订单

        GetTeamUsersOrderList();

        // adapter 初始化
        masterDetailOrderAdapter = new MasterDetailOrderAdapter(getActivity(), orderlist);
        base_list.setAdapter(masterDetailOrderAdapter);
        /**
         * 刷新
         */
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

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

        return view;
    }


    /**
     * 团队达人订单
     */
    private void GetTeamUsersOrderList() {
        String url = "https://qrb.shoomee.cn/api/teamUsersOrderList?service_user_id=" + MainActivity.id;
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
                            Log.v("团队达人订单列表", object.toString());
                            // 待数据完整，完善页面交互
                            if (object.getString("result").equals("success")) {
                                JSONObject json = object.optJSONObject("data");
                                JSONArray jsonArray = json.optJSONArray("data");
                                ArrayList<PartnerOrderListBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<PartnerOrderListBean>>() {
                                });
                                orderlist.clear();
                                if (temp.size() > 0) {
                                    orderlist.addAll(temp);
                                    masterDetailOrderAdapter.notifyDataSetChanged();
                                } else if (temp.size() == 0) {
                                    Toast.makeText(getActivity(), "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                }
                            } else {

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
