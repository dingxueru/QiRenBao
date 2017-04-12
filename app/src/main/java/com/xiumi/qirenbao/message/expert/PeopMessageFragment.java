package com.xiumi.qirenbao.message.expert;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.message.partnership.PeoMessDetailActivity;
import com.xiumi.qirenbao.message.adapter.MasterMessageAdapter;
import com.xiumi.qirenbao.message.bean.MessageBean;
import com.xiumi.qirenbao.widget.CustomerListView;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
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
 * Created by qianbailu on 2017/2/24.
 * 达人消息
 */
public class PeopMessageFragment extends Fragment {

    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView;
    @Bind(R.id.pull_to_scrollview)
    PullableScrollView pullableScrollView;
    @Bind(R.id.message_list)
    CustomerListView message_list;
    @Bind(R.id.empty_img)
    LinearLayout empty_img;
    private MasterMessageAdapter masterMessageAdapter = null;
    private ArrayList<MessageBean> messagelist = new ArrayList<MessageBean>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.people_message_layout, container, false);
        ButterKnife.bind(this, mView);
        setStatusBar();
        // adapter 初始化
        masterMessageAdapter = new MasterMessageAdapter(getActivity(),messagelist);
        message_list.setAdapter(masterMessageAdapter);
        /**
         * 刷新
         */
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                GetUserMessage();
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

        // 点击查看详情
        message_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
                intent.putExtra("id",messagelist.get(position).id);
                startActivity(intent);
            }
        });
        return mView;
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
    @Override
    public void onResume(){
        // 获取用户信息
        messagelist.clear();
        GetUserMessage();
        super.onResume();
    }
    /**
     * 获取达人的所有信息
     */
    private void GetUserMessage() {
        String url = "https://qrb.shoomee.cn//api/getUserMessages";
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
                            Log.e("达人用户信息", obj.toString());
                            if (obj.optString("result").equals("success")) {
                                JSONArray jsonArray = obj.getJSONArray("data");
                                ArrayList<MessageBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<MessageBean>>() {
                                });
                                // 再无下拉刷新之前，防止每次请求重复
                                messagelist.clear();
                                if (temp.size() > 0) {
                                    messagelist.addAll(temp);
                                    masterMessageAdapter.notifyDataSetChanged();
                                }
                            }else {
                                empty_img.setVisibility(View.VISIBLE);
                                mPullRefreshScrollView.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            empty_img.setVisibility(View.VISIBLE);
                            mPullRefreshScrollView.setVisibility(View.GONE);
                    }
                    }
                });
    }
}
