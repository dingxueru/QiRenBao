package com.xiumi.qirenbao.myWallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.myWallet.adapter.IncomeAdapter;
import com.xiumi.qirenbao.myWallet.adapter.WithdrawAdapter;
import com.xiumi.qirenbao.myWallet.bean.ChangeBean;
import com.xiumi.qirenbao.myWallet.bean.IncomeBean;
import com.xiumi.qirenbao.myWallet.bean.WithdrawBean;
import com.xiumi.qirenbao.mygift.bean.PieDataEntity;
import com.xiumi.qirenbao.mygift.widget.PieChart;
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
import butterknife.OnClick;

/**
 * Created by qianbailu on 2016/01/21.
 * 我的钱包
 */
public class MyWalletActivity extends AppCompatActivity {
    @Bind(R.id.balance)
    TextView balance;//钱包余额显示
    @Bind(R.id.withdraw)
    TextView withdraw;
    @Bind(R.id.chart)
    PieChart chart;
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.first)
    TextView first;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.second)
    TextView second;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView ;
    @Bind(R.id.base_list)
    ListViewForScrollView orderList;
    private  int index = 1;
    private int page=1;
    private IncomeAdapter incomeAdapter = null;
    private ArrayList<IncomeBean> orderBeen = new ArrayList<IncomeBean>();
    private WithdrawAdapter withdrawAdapter=null;
    private ArrayList<WithdrawBean> withdrawBeen = new ArrayList<WithdrawBean>();
    public  static int isGetBack=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
        setStatusBar();
        getbanlane();//获取钱包余额
        incomePercent();//获取钱包收支比例
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
        incomeAdapter = new IncomeAdapter(this,orderBeen);
        withdrawAdapter = new WithdrawAdapter(this,withdrawBeen);
        orderList.setAdapter(incomeAdapter);
        if(orderBeen.size()<=0){
            getRecord(1);
        }
        if(orderBeen.size()<=0){
            getWithdraw(1);
        }
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
                // 千万别忘了告诉控件刷新完毕了哦！
                index = 1;
                page=1;
                orderBeen.clear();
                withdrawBeen.clear();
                getRecord(index);
                getWithdraw(page);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                index++;
                page++;
                getWithdraw(page);
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

        tintManager.setTintColor(getResources().getColor(R.color.title_bg));
    }
    @OnClick(R.id.first)
    public void clickLayout1(View view){
        //点击了收入
        orderBeen.clear();
        isGetBack=0;
        getRecord(1);
        orderList.setAdapter(incomeAdapter);
        incomeAdapter.notifyDataSetChanged();
        first.setTextColor(getResources().getColor(R.color.title_bg));
        second.setTextColor(getResources().getColor(R.color.black));
        line1.setVisibility(View.VISIBLE);
        line2.setVisibility(View.INVISIBLE);
    }
    @OnClick(R.id.second)
    public void clickSecond(View view){
        //点击了提现
        withdrawBeen.clear();
        getWithdraw(1);
        isGetBack=1;
        orderList.setAdapter(withdrawAdapter);
        withdrawAdapter.notifyDataSetChanged();
        first.setTextColor(getResources().getColor(R.color.black));
        second.setTextColor(getResources().getColor(R.color.title_bg));
        line1.setVisibility(View.INVISIBLE);
        line2.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.withdraw)
    public void setWithdraw(View view){

        float cash= Float.parseFloat(balance.getText().toString().substring(1,balance.getText().toString().length()));
        if(cash>0.0){
            Intent intent=new Intent(MyWalletActivity.this,WithdrawActivity.class);
            startActivity(intent);
        }else{
            new AlertDialog.Builder(MyWalletActivity.this).setMessage("没有可以提现的金额")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }
    /**
     * 获取钱包的余额
     */
    private void getbanlane(){

        String url= "https://qrb.shoomee.cn/api/walletMoney";
        OkHttpUtils
                .get()
                .addHeader("Accept","application/json")
                .addHeader("Authorization",MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MyWalletActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String result=obj.optString("result");
                            Log.e("钱包的余额",obj.toString());
                            if(result.equals("success")){
                                final List<ChangeBean> temp = JSON.parseArray(obj.getJSONArray("data").toString(), ChangeBean.class);
                                if(temp.size()>0) {
                                    if( temp.get(0).money.equals("0")){
                                        balance.setText("¥"+temp.get(0).money+".00");
                                    }else{
                                        balance.setText("¥"+temp.get(0).money);
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MyWalletActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    /**
     * 获取钱包的收支记录
     */
    private void getRecord(final int page){
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
                                     }
                        } catch (JSONException e) {
                            e.printStackTrace();
                               mPullRefreshScrollView.loadmoreFinish(PullToRefreshLayout.FAIL);
                        }
                    }
                });
    }
    /**
     * 获取钱包的提现记录
     */
    private void getWithdraw(int page){
        String url= "https://qrb.shoomee.cn/api/withdrawLogs?page="+page;
        OkHttpUtils
                .get()
                .addHeader("Accept","application/json")
                .addHeader("Authorization",MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MyWalletActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
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
                                List<WithdrawBean> temp  = JSON.parseObject(data.optJSONArray("data").toString(), new TypeReference<ArrayList<WithdrawBean>>() {});
                                if(temp.size()>0){
                                    withdrawBeen.addAll(temp);
                                    withdrawAdapter.notifyDataSetChanged();
                                    try {
                                        mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }else{
                                try {
                                    mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(MyWalletActivity.this,"没有更多数据啦~",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                              mPullRefreshScrollView.loadmoreFinish(PullToRefreshLayout.FAIL);
                        }
                    }
                });
    }
    /**
     * 获取钱包收入比例
     */
    /**
     * 获取钱包的比例
     */
    private void incomePercent(){

        String url= "https://qrb.shoomee.cn/api/proportionOfMoney";
        OkHttpUtils
                .get()
                .addHeader("Accept","application/json")
                .addHeader("Authorization",MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MyWalletActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String result=obj.optString("result");
                            if(result.equals("success")){
                                JSONObject data = obj.optJSONObject("data");
                                float cityCommission=0,commission=0,atmGifts=0;
                                cityCommission=data.optInt("CityCommission");//城市佣金
                                commission=data.optInt("Commission");//成员佣金
                                atmGifts=data.optInt("AtmGifts");//礼物折现
                               float[] percent = {cityCommission, commission, atmGifts};
                                int[] mColors = {0xFFFE6A4F, 0xFFF7B52A, 0xFF0084F0};
                                List<PieDataEntity> dataEntities = new ArrayList<>();
                                Log.e("obj",cityCommission+commission+atmGifts+"");
                                if(cityCommission+commission+atmGifts==0.0){
                                    PieDataEntity entity = new PieDataEntity("name",1,0xFFCCCCCC);
                                    dataEntities.add(entity);
                                }else{
                                    for(int i = 0;i<3;i++){
                                        PieDataEntity entity = new PieDataEntity("name"+i,percent[i],mColors[i]);
                                        dataEntities.add(entity);
                                    }
                                }

                                chart.setDataList(dataEntities);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MyWalletActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
