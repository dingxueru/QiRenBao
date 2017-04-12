package com.xiumi.qirenbao.team.partnership;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.team.adapter.ApplayAdapter;
import com.xiumi.qirenbao.team.bean.ApplyBean;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qianbailu on 2017/2/5.
 * [合伙]我的团队-申请
 */
public class TeamApplyFragment extends Fragment {
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView ;
    @Bind(R.id.base_list)
    ListView apply;
    @Bind(R.id.empty_img)
    LinearLayout empty_img;
    private ApplayAdapter applayAdapter=null;
    private ArrayList<ApplyBean> applyList = new ArrayList<ApplyBean>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.team_apply_layout, container, false);
        ButterKnife.bind(this, mView);
        applayAdapter = new ApplayAdapter(getActivity(),applyList,mPullRefreshScrollView);
        apply.setAdapter(applayAdapter);
        applyList.clear();
        getApplyList ();
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
                // 千万别忘了告诉控件刷新完毕了哦！

                applyList.clear();
                getApplyList ();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                Toast.makeText(getActivity(),"没有更多数据了",Toast.LENGTH_SHORT).show();
                mPullRefreshScrollView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                // 千万别忘了告诉控件加载完毕了哦！
            }
        });

        return mView;
    }
    /**
     * 获取申请团队的人
     */
    private void getApplyList () {
        String url= "https://qrb.shoomee.cn/api/appliesToTeam?team_id="+MainActivity.team_id;
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
                        mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("result").equals("success")){
                                JSONObject data = obj.optJSONObject("data");
                                List<ApplyBean> temp= JSON.parseObject(data.getJSONArray("data").toString(), new TypeReference<ArrayList<ApplyBean>>() {});
                                applyList.addAll(temp);
                                applayAdapter.notifyDataSetChanged();
                                mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);
                            }else{
                                empty_img.setVisibility(View.VISIBLE);
                                mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);
                            }
                          } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"网络异常",Toast.LENGTH_SHORT).show();
                            mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }
                    }
                });
    }
}
