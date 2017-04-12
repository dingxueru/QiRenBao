package com.xiumi.qirenbao.mygift.expert;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.mygift.adapter.MasterGiftAdapter;
import com.xiumi.qirenbao.mygift.bean.MasterGiftBean;
import com.xiumi.qirenbao.widget.CustomerListView;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 ：Created by DXR on 2017/3/20.
 * 达人-我的礼物
 */

public class MasterGiftActivity extends Activity {

    private MasterGiftAdapter masterGiftAdapter = null;
    private ArrayList<MasterGiftBean> giftlist = new ArrayList<MasterGiftBean>();

    @Bind(R.id.reward_list)
    CustomerListView reward_list;
    @Bind(R.id.back)
    TextView back;
    // 送出的礼物总值
    @Bind(R.id.total_money)
    TextView total_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_gift_layout);
        ButterKnife.bind(this);
        setStatusBar();
        // 请求数据
        SendGifts();
        // 我送出的礼物总值
        SendGiftMoney();
        // adapter 初始化
        masterGiftAdapter = new MasterGiftAdapter(this, giftlist);
        reward_list.setAdapter(masterGiftAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
     * 我送出的礼物总值
     */
    private void SendGiftMoney() {
        String url = "https://qrb.shoomee.cn/api/sendGiftMoney";
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MasterGiftActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.e("我送出的礼物总值", object.toString());
                            if (object.getString("result").equals("success")) {
                                total_money.setText("￥ " + object.optInt("data"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MasterGiftActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }

    /**
     * 我送出的礼物记录
     */
    private void SendGifts() {
        String url = "https://qrb.shoomee.cn/api/sendGifts";
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MasterGiftActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.e("我送出的礼物", object.toString());
                            if (object.getString("result").equals("success")) {
                                JSONObject json = object.optJSONObject("data");
                                JSONArray jsonArray = json.optJSONArray("data");
                                ArrayList<MasterGiftBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<MasterGiftBean>>() {
                                });
                                if (temp.size() > 0) {

                                    giftlist.addAll(temp);
                                    masterGiftAdapter.notifyDataSetChanged();

                                } else if (temp.size() == 0) {
                                    Toast.makeText(MasterGiftActivity.this, "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MasterGiftActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }

}
