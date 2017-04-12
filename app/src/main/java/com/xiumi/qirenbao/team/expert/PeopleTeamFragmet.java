package com.xiumi.qirenbao.team.expert;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.team.adapter.RecommendTeamAdapter;
import com.xiumi.qirenbao.team.bean.RecommendTeamBean;
import com.xiumi.qirenbao.widget.CustomerListView;
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
 * Created by qianbailu on 2017/2/5.
 * [达人]我的团队-未加入团队
 */
public class PeopleTeamFragmet extends Fragment {

    @Bind(R.id.join_log)
    LinearLayout join_log;
    // 推荐团队列表
    @Bind(R.id.recommend_list)
    CustomerListView recommend_list;
    // 换一批
    @Bind(R.id.change_team)
    TextView change_team;
    @Bind(R.id.pull_to_scrollview)
    ScrollView pull_to_scrollview;

    private RecommendTeamAdapter recommendTeamAdapter = null;
    private ArrayList<RecommendTeamBean> recommendlist = new ArrayList<RecommendTeamBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.no_team_layout, container, false);
        ButterKnife.bind(this, mView);
        setStatusBar();
        // 解决scrollview嵌套listview位置上移问题
//        recommend_list.setFocusable(false);
        // 团队推荐列表
        GetTeams();
        // adapter 初始化
        recommendTeamAdapter = new RecommendTeamAdapter(getActivity(),recommendlist);
        recommend_list.setAdapter(recommendTeamAdapter);
        // 达人申请列表
        join_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MasterApplyTeamActivity.class);
                startActivity(intent);
            }
        });
        // 换一批推荐团队
        change_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新数据
                GetTeams();
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



    /**
     * 随机获取3个团队列表
     */
    private void GetTeams ()  {
        String url = "https://qrb.shoomee.cn/api/getTeams";
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
                            Log.v("团队推荐列表",object.toString());
                            if (object.getString("result").equals("success")) {
                                JSONArray jsonArray = object.getJSONArray("data");
                                ArrayList<RecommendTeamBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<RecommendTeamBean>>() {
                                });
                                // 再无下拉刷新之前，防止每次请求重复
                                recommendlist.clear();
                                if (temp.size() > 0) {
                                    recommendlist.addAll(temp);
                                    recommendTeamAdapter.notifyDataSetChanged();
                                } else if (temp.size() == 0) {
                                    Toast.makeText(getActivity(), "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                }
                                // 解决scrollview嵌套listview位置上移问题
//                                pull_to_scrollview.smoothScrollTo(0,0);
                            } else {
                                Toast.makeText(getActivity(), "暂无团队推荐列表", Toast.LENGTH_SHORT).show();
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
