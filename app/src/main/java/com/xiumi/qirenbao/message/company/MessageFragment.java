package com.xiumi.qirenbao.message.company;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.message.adapter.CompanyMessageAdapter;
import com.xiumi.qirenbao.message.bean.MessageBean;
import com.xiumi.qirenbao.widget.CustomerListView;
import com.xiumi.qirenbao.widget.ListViewForScrollView;
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
 * Created by qianbailu on 2017/1/20.
 * 企业消息
 */
public class MessageFragment extends Fragment {
    private CompanyMessageAdapter masterMessageAdapter = null;
    private ArrayList<MessageBean> messagelist = new ArrayList<MessageBean>();
    @Bind(R.id.message_list)
    CustomerListView message_list;
    @Bind(R.id.empty_img)
    LinearLayout empty_img;
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView;
    @Bind(R.id.pull_to_scrollview)
    PullableScrollView pullableScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.introuduce_layout, container, false);
        ButterKnife.bind(this, mView);
        // adapter 初始化
        masterMessageAdapter = new CompanyMessageAdapter(getActivity(),messagelist);
        message_list.setAdapter(masterMessageAdapter);
        message_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //跳转到消息详情
                Intent intent=new Intent(getActivity(),CompanyDetailActivity.class);
                intent.putExtra("id",messagelist.get(i).id);
                startActivity(intent);
            }
        });

        /**
         * 刷新
         */
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                getMessaage();
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
        messagelist.clear();
        getMessaage();
    }

    /**
     * 获取用户的所有信息
     */
    private void getMessaage(){
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
                        Toast.makeText(getActivity(),"网络异常",Toast.LENGTH_SHORT).show();
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
                             }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "数据解析错误", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }
}
