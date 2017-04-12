package com.xiumi.qirenbao.officebuilding;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.officebuilding.adapter.BuildAdapter;
import com.xiumi.qirenbao.officebuilding.bean.BuildBean;
import com.xiumi.qirenbao.widget.ListViewForScrollView;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by qianbailu on 2017/1/20.
 * 写字楼列表页
 */

public class OfficeBuildingActivity extends AppCompatActivity {
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView ;
    @Bind(R.id.base_list)
    ListViewForScrollView listSkill;
    @Bind(R.id.back)
    TextView back;
    private ArrayList<BuildBean.DataBean> buildList = new ArrayList<BuildBean.DataBean>();
    private BuildAdapter buildAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_building);
        ButterKnife.bind(this);
        setStatusBar();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buildAdapter = new BuildAdapter(this,buildList);
        listSkill.setAdapter(buildAdapter);
        if(buildList.size()<=0)
            GetAllBuild ();
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
                // 千万别忘了告诉控件刷新完毕了哦！

                buildList.clear();
                GetAllBuild ();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                GetAllBuild ();
                // 千万别忘了告诉控件加载完毕了哦！
            }
        });
// 点击事件
        listSkill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OfficeBuildingActivity.this, BuildingDetailActivity.class);
                intent.putExtra("id",buildList.get(position).id);
                OfficeBuildingActivity.this.startActivity(intent);
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

        tintManager.setTintColor(getResources().getColor(R.color.title_bg));
    }

    /**
     * 获取所有的写字楼信息
     */
    private void GetAllBuild () {
        String url = "https://qrb.shoomee.cn//qrb_api/getBuildings?city_id=1";
        OkHttpUtils
                .get()
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
                            JSONObject data = object.optJSONObject("data");
                            JSONObject buildings = data.optJSONObject("buildings");
                            Log.e("build",buildings.getJSONArray("data").toString());
                            List< BuildBean.DataBean> temp = JSON.parseArray(buildings.getJSONArray("data").toString(),  BuildBean.DataBean.class);

                            buildList.addAll(temp);
                            buildAdapter.notifyDataSetChanged();
                            try {
                                mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mPullRefreshScrollView.loadmoreFinish(PullToRefreshLayout.FAIL);
                        }
                    }

                });
    }
}
