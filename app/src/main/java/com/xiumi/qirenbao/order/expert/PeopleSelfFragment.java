package com.xiumi.qirenbao.order.expert;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.order.adapter.MasterOrderListAdapter;
import com.xiumi.qirenbao.order.adapter.OverOrderListAdapter;
import com.xiumi.qirenbao.order.bean.PartnerOrderListBean;
import com.xiumi.qirenbao.widget.CustomerListView;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.xiumi.qirenbao.widget.pullableView.PullableScrollView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qianbailu on 2017/2/24.
 * 达人订单
 */
public class PeopleSelfFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.first)
    TextView first;
    @Bind(R.id.second)
    TextView second;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.order_list)
    CustomerListView order_list;
    // 进行中订单
    private ArrayList<PartnerOrderListBean> orderlist = new ArrayList<PartnerOrderListBean>();
    private MasterOrderListAdapter masterOrderListAdapter = null;
    // 已完结订单数据
    private ArrayList<PartnerOrderListBean> overlist = new ArrayList<PartnerOrderListBean>();
    private OverOrderListAdapter overOrderListAdapter = null;
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView;
    @Bind(R.id.pull_to_scrollview)
    PullableScrollView pullableScrollView;
    @Bind(R.id.empty_img)
    LinearLayout empty_img;
    private int index = 1;
    private int isclose = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.intelligent_layout, container, false);
        ButterKnife.bind(this, mView);
        setStatusBar();
        // 订单状态点击
        first.setOnClickListener(this);
        second.setOnClickListener(this);
        /**
         * 获取团队达人订单列表
         */
//        GetTeamUsersOrderList(1);
        // adapter 初始化
        masterOrderListAdapter = new MasterOrderListAdapter(getActivity(), orderlist, mPullRefreshScrollView);
        order_list.setAdapter(masterOrderListAdapter);

        /**
         * 刷新
         */
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                index = 1;
                if (isclose == 0) {
                    orderlist.clear();
                    GetTeamUsersOrderList(index);
                } else if (isclose == 1) {
                    overlist.clear();
                    GetTeamUsersOrderList(index);
                }
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
                GetTeamUsersOrderList(index);
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
    public void onResume() {
        super.onResume();
        if (isclose == 0) {
            orderlist.clear();
            GetTeamUsersOrderList(1);
        } else if (isclose == 1) {
            overlist.clear();
            GetTeamUsersOrderList(1);
        }
    }

    protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(getResources().getColor(R.color.orangenormal));
    }

    /**
     * 团队达人订单
     */
    private void GetTeamUsersOrderList(final int page) {
        String url = "https://qrb.shoomee.cn/api/serviceOrderList?is_close=" + isclose + "&page=" + page;
        OkHttpUtils
                .get()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        empty_img.setVisibility(View.VISIBLE);
                        mPullRefreshScrollView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.v("团队达人订单列表", object.toString());
                            // 待数据完整，完善页面交互
                            JSONObject json = object.optJSONObject("data");
                            if (object.getString("result").equals("success")) {
                                JSONArray jsonArray = json.optJSONArray("data");
                                ArrayList<PartnerOrderListBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<PartnerOrderListBean>>() {
                                });
                                if (temp.size() > 0) {
                                    empty_img.setVisibility(View.GONE);
                                    if (isclose == 0) {
                                        orderlist.addAll(temp);
                                        masterOrderListAdapter.notifyDataSetChanged();
                                    } else if (isclose == 1) {
                                        overlist.addAll(temp);
                                        overOrderListAdapter.notifyDataSetChanged();
                                    }
                                } else if (temp.size() == 0 && index > 1) {
                                    Toast.makeText(getActivity(), "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                } else {
                                    empty_img.setVisibility(View.VISIBLE);
                                }
                            } else if (index == 1) {
                                empty_img.setVisibility(View.VISIBLE);
                                mPullRefreshScrollView.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            if (index == 1) {
                                empty_img.setVisibility(View.VISIBLE);
                                mPullRefreshScrollView.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first:
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                first.setTextColor(getResources().getColor(R.color.orangenormal));
                second.setTextColor(getResources().getColor(R.color.black));
                isclose = 0;
                index = 1;
                orderlist.clear();
                GetTeamUsersOrderList(index);
                masterOrderListAdapter = new MasterOrderListAdapter(getActivity(), orderlist, mPullRefreshScrollView);
                order_list.setAdapter(masterOrderListAdapter);
                break;
            case R.id.second:
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);
                second.setTextColor(getResources().getColor(R.color.orangenormal));
                first.setTextColor(getResources().getColor(R.color.black));
                isclose = 1;
                index = 1;
                overlist.clear();
                GetTeamUsersOrderList(index);
                overOrderListAdapter = new OverOrderListAdapter(getActivity(), overlist);
                order_list.setAdapter(overOrderListAdapter);
                break;
            default:
                break;
        }
    }
}