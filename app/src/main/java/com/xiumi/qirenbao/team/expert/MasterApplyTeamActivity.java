package com.xiumi.qirenbao.team.expert;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.myWallet.expert.MasterWalletActivity;
import com.xiumi.qirenbao.team.adapter.MasterApplyLogAdapter;
import com.xiumi.qirenbao.team.bean.MasterApplyLogBean;
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
 * 作者 ：Created by DXR on 2017/3/11.
 * 达人申请列表
 */

public class MasterApplyTeamActivity extends Activity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.join_list)
    CustomerListView join_list;
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView;
    @Bind(R.id.pull_to_scrollview)
    PullableScrollView pullableScrollView;
    private MasterApplyLogAdapter applyLogAdapter = null;
    private ArrayList<MasterApplyLogBean> applylist = new ArrayList<MasterApplyLogBean>();
    private int index = 1;
    @Bind(R.id.empty_img)
    LinearLayout empty_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_join_team_layout);
        ButterKnife.bind(this);
        setStatusBar();

        // 获取申请记录
        GetServiceApplies(1);
        // adapter 初始化
        applyLogAdapter = new MasterApplyLogAdapter(this, applylist);
        join_list.setAdapter(applyLogAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 刷新
         */
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                index = 1;
                applylist.clear();
                GetServiceApplies(index);
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
                GetServiceApplies(index);
                // 告诉控件刷新完毕
                try {
                    mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(getResources().getColor(R.color.orangenormal));
    }

    /**
     * 达人发出的申请
     */

    private void GetServiceApplies(final int page) {
        String url = "https://qrb.shoomee.cn/api/serviceApplies?page=" + page;
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MasterApplyTeamActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        applylist.clear();
                        GetServiceApplies(1);
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("达人申请列表", obj.toString());
                            if (obj.optString("result").equals("success")) {
                                if (obj.optJSONObject("data") != null) {
                                    JSONObject json = obj.optJSONObject("data");
                                    JSONArray jsonArray = json.optJSONArray("data");
                                    ArrayList<MasterApplyLogBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<MasterApplyLogBean>>() {
                                    });
                                    if (temp.size() > 0) {
                                        applylist.addAll(temp);
                                        applyLogAdapter.notifyDataSetChanged();
                                    } else if (temp.size() == 0 && index > 1) {
                                        Toast.makeText(MasterApplyTeamActivity.this, "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                    }
                                }  else if (index == 1){
                                    empty_img.setVisibility(View.VISIBLE);
                                    mPullRefreshScrollView.setVisibility(View.GONE);
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
                            Toast.makeText(MasterApplyTeamActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }
}
