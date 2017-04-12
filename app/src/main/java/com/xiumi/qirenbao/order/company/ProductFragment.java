package com.xiumi.qirenbao.order.company;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.order.adapter.CompanyOrderAdapter;
import com.xiumi.qirenbao.order.bean.CompanyOrderBean;
import com.xiumi.qirenbao.widget.CustomerListView;
import com.xiumi.qirenbao.widget.ListViewForScrollView;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qianbailu on 2016/12/26.
 * 企业订单
 */
public class ProductFragment extends Fragment {
    @Bind(R.id.first)
    TextView first;
    @Bind(R.id.second)
    TextView second;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.empty_img)
    LinearLayout empty_img;
    // 刷新控件
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView;
    @Bind(R.id.base_list)
    CustomerListView listSkill;
    private int index = 1;
    private int isclose = 0;
    private CompanyOrderAdapter companyOrderAdapter = null;
    private ArrayList<CompanyOrderBean> companyOrderBeanList = new ArrayList<CompanyOrderBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.product_layout, container, false);
        ButterKnife.bind(this, mView);
        companyOrderAdapter = new CompanyOrderAdapter(getActivity(), companyOrderBeanList);
        listSkill.setAdapter(companyOrderAdapter);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isclose = 0;
                index = 1;
                companyOrderBeanList.clear();
                getOrder(index);
                companyOrderAdapter = new CompanyOrderAdapter(getActivity(), companyOrderBeanList);
                listSkill.setAdapter(companyOrderAdapter);
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                first.setTextColor(getResources().getColor(R.color.title_bg));
                second.setTextColor(getResources().getColor(R.color.black));
//                empty_img.setVisibility(View.GONE);
//                mPullRefreshScrollView.setVisibility(View.VISIBLE);
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isclose = 1;
                index = 1;
                companyOrderBeanList.clear();
//                empty_img.setVisibility(View.GONE);
//                mPullRefreshScrollView.setVisibility(View.VISIBLE);
                getOrder(index);
                companyOrderAdapter = new CompanyOrderAdapter(getActivity(), companyOrderBeanList);
                listSkill.setAdapter(companyOrderAdapter);
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);
                second.setTextColor(getResources().getColor(R.color.title_bg));
                first.setTextColor(getResources().getColor(R.color.black));
            }
        });
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
                // 千万别忘了告诉控件刷新完毕了哦！
                index = 1;
                companyOrderBeanList.clear();
                getOrder(index);
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
                getOrder(index);
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
     * 获取企业主的订单
     */
    @Override
    public void onResume() {
        super.onResume();
        companyOrderBeanList.clear();
        getOrder(1);
    }

    private void getOrder(final int index) {
        String url = "https://qrb.shoomee.cn/api/getCompanyUserOrders?page=" + index + "&is_close=" + isclose;
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
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
                            JSONObject obj = new JSONObject(response);
                            JSONObject data = obj.getJSONObject("data");
                            Log.e("获取企业主的订单", obj.toString());
                            String result = obj.getString("result");
                            if (result.equals("success")) {
                                Log.e("data", data.optJSONArray("data").toString());
                                List<CompanyOrderBean> temp = JSON.parseObject(data.optJSONArray("data").toString(), new TypeReference<ArrayList<CompanyOrderBean>>() {
                                });
                                if (temp.size() > 0) {
                                    empty_img.setVisibility(View.GONE);
                                    companyOrderBeanList.addAll(temp);
                                    companyOrderAdapter.notifyDataSetChanged();

                                } else if (temp.size() == 0 && index > 1) {
                                    Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                                } else {
                                    empty_img.setVisibility(View.VISIBLE);
//                                    mPullRefreshScrollView.setVisibility(View.GONE);
                                }
                            } else if (index == 1) {
                                empty_img.setVisibility(View.VISIBLE);
                                mPullRefreshScrollView.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
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
