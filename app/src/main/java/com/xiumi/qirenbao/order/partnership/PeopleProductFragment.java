package com.xiumi.qirenbao.order.partnership;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.order.adapter.OverOrderListAdapter;
import com.xiumi.qirenbao.order.adapter.PartnerOrderListAdapter;
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
 * Created by qianbailu on 2017/1/23.
 * [合伙订单]
 */
public class PeopleProductFragment extends Fragment implements View.OnClickListener {
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
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView;
    @Bind(R.id.pull_to_scrollview)
    PullableScrollView pullableScrollView;
    @Bind(R.id.empty_img)
    LinearLayout empty_img;
    private int index = 1;
    private int isclose = 0;
    // 进行中订单数据
    private ArrayList<PartnerOrderListBean> orderlist = new ArrayList<PartnerOrderListBean>();
    private PartnerOrderListAdapter partnerOrderListAdapter = null;
    // 已完结订单数据
    private ArrayList<PartnerOrderListBean> overlist = new ArrayList<PartnerOrderListBean>();
    private OverOrderListAdapter overOrderListAdapter = null;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.people_product_layout, container, false);
        ButterKnife.bind(this, mView);
        setStatusBar();
        // 合伙人的订单列表
//        GetPartnerOrderList(1);
        // adapter 初始化
        partnerOrderListAdapter = new PartnerOrderListAdapter(getActivity(), orderlist, mPullRefreshScrollView);
        order_list.setAdapter(partnerOrderListAdapter);

        first.setOnClickListener(this);
        second.setOnClickListener(this);
        /**
         * 刷新
         */
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                index = 1;
                if (isclose == 0) {
                    orderlist.clear();
                    GetPartnerOrderList(index);
                } else if (isclose == 1) {
                    overlist.clear();
                    GetPartnerOrderList(index);
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
                GetPartnerOrderList(index);
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
            GetPartnerOrderList(1);
        } else if (isclose == 1) {
            overlist.clear();
            GetPartnerOrderList(1);
        }
    }

    protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(getResources().getColor(R.color.title_bg));
    }

    /**
     * 合伙人的订单列表（包含评价）
     */
    private void GetPartnerOrderList(final int page) {
        String url = "https://qrb.shoomee.cn/api/partnerOrderList?is_close=" + isclose + "&page=" + page;
        OkHttpUtils
                .post()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("", "")
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        mPullRefreshScrollView.setVisibility(View.GONE);
                        empty_img.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.v("合伙人的订单列表", object.toString());
                            if (object.getString("result").equals("success")) {
                                JSONObject json = object.optJSONObject("data");
                                JSONArray jsonArray = json.optJSONArray("data");
                                ArrayList<PartnerOrderListBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<PartnerOrderListBean>>() {
                                });
                                if (temp.size() > 0) {
                                    empty_img.setVisibility(View.GONE);
                                    if (isclose == 0) {
                                        orderlist.addAll(temp);
                                        partnerOrderListAdapter.notifyDataSetChanged();
                                    } else if (isclose == 1) {
                                        overlist.addAll(temp);
                                        overOrderListAdapter.notifyDataSetChanged();
                                    }
                                } else if (temp.size() == 0 && index > 1) {
                                    empty_img.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                } else {
                                    empty_img.setVisibility(View.VISIBLE);
                                }
                            } else if (index == 1){
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first:
                mPullRefreshScrollView.setVisibility(View.VISIBLE);
                empty_img.setVisibility(View.GONE);
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                first.setTextColor(getResources().getColor(R.color.title_bg));
                second.setTextColor(getResources().getColor(R.color.black));
                isclose = 0;
                index = 1;
                orderlist.clear();
                GetPartnerOrderList(index);
                partnerOrderListAdapter = new PartnerOrderListAdapter(getActivity(), orderlist, mPullRefreshScrollView);
                order_list.setAdapter(partnerOrderListAdapter);
                break;
            case R.id.second:
                mPullRefreshScrollView.setVisibility(View.VISIBLE);
                empty_img.setVisibility(View.GONE);
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);
                second.setTextColor(getResources().getColor(R.color.title_bg));
                first.setTextColor(getResources().getColor(R.color.black));
                isclose = 1;
                index = 1;
                overlist.clear();
                GetPartnerOrderList(index);
                overOrderListAdapter = new OverOrderListAdapter(getActivity(), overlist);
                order_list.setAdapter(overOrderListAdapter);
                break;
            default:
                break;
        }
    }
}
