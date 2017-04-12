package com.xiumi.qirenbao.myWallet.expert;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.myWallet.adapter.IncomeAdapter;
import com.xiumi.qirenbao.myWallet.bean.IncomeBean;
import com.xiumi.qirenbao.widget.ListViewForScrollView;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qianbailu on 2016/01/21.
 *  我的钱包
 */

public class MasterWalletActivity  extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView ;
    @Bind(R.id.base_list)
    ListViewForScrollView orderList;
    private  int index = 1;
    private IncomeAdapter incomeAdapter = null;
    private ArrayList<IncomeBean> orderBeen = new ArrayList<IncomeBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_wallet_layout);
        ButterKnife.bind(this);
        setStatusBar();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        incomeAdapter = new IncomeAdapter(this,orderBeen);
        orderList.setAdapter(incomeAdapter);
        if(orderBeen.size()<=0){
            getRecord(1);
        }

        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
                // 千万别忘了告诉控件刷新完毕了哦！
                index = 1;
                orderBeen.clear();
                getRecord(index);

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                index++;

                getRecord(index);
                // 千万别忘了告诉控件加载完毕了哦！
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
     * 获取钱包的收支记录
     */
    private void getRecord(int page){
        String url= "https://qrb.shoomee.cn/api/walletLogs?page="+page;
        OkHttpUtils
                .get()
                .addHeader("Accept","application/json")
                .addHeader("Authorization",MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MasterWalletActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        mPullRefreshScrollView.loadmoreFinish(PullToRefreshLayout.FAIL);
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("钱包的收支记录",obj.toString());
                            JSONObject data=obj.getJSONObject("data");
                            String result=obj.getString("result");
                            if(result.equals("success")){
                                Log.e("data",data.optJSONArray("data").toString());
                                List<IncomeBean> temp  = JSON.parseObject(data.optJSONArray("data").toString(), new TypeReference<ArrayList<IncomeBean>>() {});
                                if(temp.size()>0){
                                    orderBeen.addAll(temp);
                                    incomeAdapter.notifyDataSetChanged();
                                    try {
                                        mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }else  if(result.equals("fail")){
                                try {
                                    mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(MasterWalletActivity.this,"没有更多数据啦~",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mPullRefreshScrollView.loadmoreFinish(PullToRefreshLayout.FAIL);
                        }
                    }
                });
    }

}
