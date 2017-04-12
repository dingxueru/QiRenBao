package com.xiumi.qirenbao.team.partnership;

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
import com.xiumi.qirenbao.order.bean.PartnerOrderListBean;
import com.xiumi.qirenbao.team.adapter.TeamOrderAdapter;
import com.xiumi.qirenbao.widget.ListViewForScrollView;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qianbailu on 2017/1/25.
 * 团队订单
 */
public class TeamOrderFrament  extends Fragment {
    // 刷新控件
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView ;
    @Bind(R.id.base_list)
    ListViewForScrollView orderList;
    @Bind(R.id.first)
    TextView first;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.second)
    TextView second;
    @Bind(R.id.empty_img)
    LinearLayout empty_img;
    private  int index = 1;
    private int is_close=0;
    private TeamOrderAdapter orderAdapter = null;//未完成订单
    private ArrayList<PartnerOrderListBean> orderBeen = new ArrayList<PartnerOrderListBean>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.parter_item_layout, container, false);
        ButterKnife.bind(this, mView);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
                // 千万别忘了告诉控件刷新完毕了哦！
                index = 1;
                orderBeen.clear();
                GetPartnerOrderList(index);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                index++;
                GetPartnerOrderList(index);
                // 千万别忘了告诉控件加载完毕了哦！
            }
        });

        return mView;
    }
    @Override
    public void onResume(){
        super.onResume();
        is_close=0;
        orderAdapter = new TeamOrderAdapter(getActivity(),orderBeen,is_close);
        orderList.setAdapter(orderAdapter);
        orderBeen.clear();
        if(orderBeen.size()<=0){
            GetPartnerOrderList(1);
        }
    }

    @OnClick(R.id.first)
    public void setFirst(View view){
        is_close=0;
        Log.e("is_close",is_close+"");
        orderAdapter = new TeamOrderAdapter(getActivity(),orderBeen,is_close);
        orderList.setAdapter(orderAdapter);
        orderBeen.clear();
        GetPartnerOrderList(1);
        line1.setVisibility(View.VISIBLE);
        line2.setVisibility(View.INVISIBLE);
        first.setTextColor(getResources().getColor(R.color.title_bg));
        second.setTextColor(getResources().getColor(R.color.black));
    }
    @OnClick(R.id.second)
    public void setSecond(View view){
        is_close=1;
        Log.e("is_close",is_close+"");
        orderAdapter = new TeamOrderAdapter(getActivity(),orderBeen,is_close);
        orderList.setAdapter(orderAdapter);
        orderBeen.clear();
        GetPartnerOrderList(1);
        line1.setVisibility(View.INVISIBLE);
        line2.setVisibility(View.VISIBLE);
        second.setTextColor(getResources().getColor(R.color.title_bg));
        first.setTextColor(getResources().getColor(R.color.black));
    }
    /**
     * 合伙人的订单列表（包含评价）
     */
    private void GetPartnerOrderList(final int page) {
        Log.e("user_id",ParnerMemberDetailActivity.user_id+"");
        String url = "https://qrb.shoomee.cn/api/teamUsersOrderList?service_user_id=" +ParnerMemberDetailActivity.user_id+ "&is_close=" + is_close+"&team_id="+MainActivity.team_id+ "&page=" + page;
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("", "")
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        mPullRefreshScrollView.loadmoreFinish(PullToRefreshLayout.FAIL);
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
                                        orderBeen.addAll(temp);
                                        orderAdapter.notifyDataSetChanged();
                                    try {
                                        mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (temp.size() == 0&&page>1) {
                                    empty_img.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                    try {
                                        mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    empty_img.setVisibility(View.VISIBLE);
                                    mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);
                                }
                            } else {
                                try {
                                    empty_img.setVisibility(View.VISIBLE);
                                    mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mPullRefreshScrollView.loadmoreFinish(PullToRefreshLayout.FAIL);
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }
}
