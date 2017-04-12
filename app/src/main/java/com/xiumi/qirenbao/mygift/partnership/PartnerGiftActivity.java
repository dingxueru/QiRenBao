package com.xiumi.qirenbao.mygift.partnership;

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
import com.xiumi.qirenbao.mygift.adapter.PartnerAllGiftAdapter;
import com.xiumi.qirenbao.mygift.adapter.PartnerReceiveGiftAdapter;
import com.xiumi.qirenbao.mygift.adapter.PartnerSendGiftAdapter;
import com.xiumi.qirenbao.mygift.adapter.SellGiftLogAdapter;
import com.xiumi.qirenbao.mygift.bean.MasterGiftBean;
import com.xiumi.qirenbao.mygift.bean.ReceiveGiftsCountBean;
import com.xiumi.qirenbao.mygift.bean.SellGiftLogBean;
import com.xiumi.qirenbao.reward.bean.GiftBean;
import com.xiumi.qirenbao.widget.CustomerGridView;
import com.xiumi.qirenbao.widget.CustomerListView;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qianbailu on 2016/01/21.
 * 我的礼物
 */
public class PartnerGiftActivity extends Activity implements View.OnClickListener {
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
    @Bind(R.id.third)
    TextView third;
    @Bind(R.id.line3)
    View line3;
    @Bind(R.id.layout1)
    LinearLayout layout1;
    @Bind(R.id.layout2)
    LinearLayout layout2;
    @Bind(R.id.layout3)
    LinearLayout layout3;
    @Bind(R.id.reward_list)
    CustomerListView reward_list;
    @Bind(R.id.pull_to_refresh_index)
    PullToRefreshLayout mPullRefreshScrollView;
    // 打赏的礼物类别
    @Bind(R.id.gift_list)
    CustomerGridView gift_list;
    // 礼物所有类别
    private PartnerAllGiftAdapter partnerAllGiftAdapter = null;
    private ArrayList<GiftBean> giftlist = new ArrayList<GiftBean>();
    // 收获list
    private PartnerReceiveGiftAdapter partnerGiftAdapter = null;
    private ArrayList<MasterGiftBean> receivelist = new ArrayList<MasterGiftBean>();
    // 打赏list
    private PartnerSendGiftAdapter partnerSendGiftAdapter = null;
    private ArrayList<MasterGiftBean> sendlist = new ArrayList<MasterGiftBean>();
    // 变现礼物记录
    private SellGiftLogAdapter sellGiftLogAdapter = null;
    private ArrayList<SellGiftLogBean> selllist = new ArrayList<SellGiftLogBean>();
    // 礼物变现
    @Bind(R.id.sellALLGift)
    TextView sellALLGift;
    @Bind(R.id.gift_total)
    TextView gift_total;
    // 收到的礼物统计 list
    private ArrayList<ReceiveGiftsCountBean> countlist = new ArrayList<ReceiveGiftsCountBean>();
    private ArrayList<String> numlist = new ArrayList<String>();
    // 现金礼物总值
    private double total_price = 0.00;
    private int flag = 0;
    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gift);
        ButterKnife.bind(this);
        setStatusBar();
        for (int i = 0; i < 12; i++) {
            numlist.add("0");
        }
        // 计算我收到的礼物统计
        ReceiveGiftsCount();
        // adapter初始化 ，默认我收到的礼物
        partnerGiftAdapter = new PartnerReceiveGiftAdapter(PartnerGiftActivity.this, receivelist);
        reward_list.setAdapter(partnerGiftAdapter);
        // 获取我收到的礼物
        ReceiveGifts(1);
        // 所有礼物类型初始化
        partnerAllGiftAdapter = new PartnerAllGiftAdapter(PartnerGiftActivity.this, giftlist, numlist);
        gift_list.setAdapter(partnerAllGiftAdapter);
        // 获取打赏的礼物类别
//        GetAllGifts();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                if (flag == 0) {
                    receivelist.clear();
                    ReceiveGifts(index);
                } else if (flag == 1) {
                    sendlist.clear();
                    SendGifts(index);
                } else if (flag == 2) {
                    selllist.clear();
                    SellGiftLogs(index);
                }
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
                if (flag == 0) {
                    ReceiveGifts(index);
                } else if (flag == 1) {
                    SendGifts(index);
                } else if (flag == 2) {
                    SellGiftLogs(index);
                }
                // 告诉控件刷新完毕
                try {
                    mPullRefreshScrollView.refreshFinish(PullToRefreshLayout.SUCCEED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        sellALLGift.setOnClickListener(this);

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 我收到的礼物统计
     */
    private void ReceiveGiftsCount() {
        String url = "https://qrb.shoomee.cn/api/receiveGiftsCount";
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(PartnerGiftActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.v("我收到的礼物统计", obj.toString());
                            if (obj.optString("result").equals("success")) {
                                // 待数据，完善逻辑
                                JSONArray jsonArray = obj.optJSONArray("data");
                                ArrayList<ReceiveGiftsCountBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<ReceiveGiftsCountBean>>() {
                                });
                                if (temp.size() > 0) {
                                    countlist.addAll(temp);
                                }
                            }
                            // 获取打赏的礼物类别
                            GetAllGifts();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(PartnerGiftActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 获取打赏的礼物类别
     */
    private void GetAllGifts() {
        String url = "https://qrb.shoomee.cn/qrb_api/getAllGifts";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(PartnerGiftActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.v("获取打赏的礼物类别", obj.toString());
                            if (obj.optString("result").equals("success")) {
                                JSONArray jsonArray = obj.optJSONArray("data");
                                ArrayList<GiftBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<GiftBean>>() {
                                });
                                if (temp.size() > 0) {
                                    Log.v("countlist_size", countlist.size() + "");
                                    if (countlist.size() > 0) {
                                        for (int i = 0; i < countlist.size(); i++) {
                                            for (int j = 0; j < temp.size(); j++) {
                                                if (Integer.parseInt(temp.get(j).id) == countlist.get(i).gift_id) {
                                                    numlist.set(j,countlist.get(i).count);
                                                    Log.v("countlist.get(i).count",countlist.get(i).count);
                                                    Log.v("numlist.get(j)",numlist.get(j));
                                                    double price = Float.parseFloat(countlist.get(i).count) * Float.parseFloat(temp.get(j).price);
                                                    total_price = price + total_price;
                                                    gift_total.setText("￥ " + total_price);
                                                    Log.v("count", countlist.get(i).count);
                                                    Log.v("price", temp.get(j).price);
                                                    Log.v("total_price", total_price + "");
                                                }
                                            }
                                        }
                                        Log.v("numlist_size", numlist.size() + "");
                                    }
                                    giftlist.addAll(temp);
                                    partnerAllGiftAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(PartnerGiftActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 我收到的礼物
     */
    private void ReceiveGifts(final int page) {
        String url = "https://qrb.shoomee.cn/api/receiveGifts?page=" + page;
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(PartnerGiftActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.e("我收到的礼物", object.toString());
                            if (object.getString("result").equals("success")) {
                                JSONObject json = object.optJSONObject("data");
                                JSONArray jsonArray = json.optJSONArray("data");
                                ArrayList<MasterGiftBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<MasterGiftBean>>() {
                                });
                                if (temp.size() > 0) {
                                    receivelist.addAll(temp);
                                    partnerGiftAdapter.notifyDataSetChanged();
                                } else if (temp.size() == 0) {
                                    Toast.makeText(PartnerGiftActivity.this, "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                }
                            } else {
//                                Toast.makeText(PartnerGiftActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(PartnerGiftActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }

    /**
     * 我送出的礼物
     */
    private void SendGifts(final int page) {
        String url = "https://qrb.shoomee.cn/api/sendGifts?page=" + page;
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(PartnerGiftActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
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
                                    sendlist.addAll(temp);
                                    partnerSendGiftAdapter.notifyDataSetChanged();
                                } else if (temp.size() == 0) {
                                    Toast.makeText(PartnerGiftActivity.this, "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                }
                            } else {
//                                Toast.makeText(PartnerGiftActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(PartnerGiftActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }

    /**
     * 变现礼物记录
     */
    private void SellGiftLogs(final int page) {
        String url = "https://qrb.shoomee.cn/api/sellGiftLogs?page=" + page;
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(PartnerGiftActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        Log.v("Exception", e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.e("变现礼物记录", object.toString());
                            if (object.getString("result").equals("success")) {
                                JSONObject json = object.optJSONObject("data");
                                JSONArray jsonArray = json.optJSONArray("data");
                                ArrayList<SellGiftLogBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<SellGiftLogBean>>() {
                                });
                                if (temp.size() > 0) {
                                    selllist.addAll(temp);
                                    sellGiftLogAdapter.notifyDataSetChanged();
                                } else if (temp.size() == 0) {
                                    Toast.makeText(PartnerGiftActivity.this, "没有更多数据啦", Toast.LENGTH_SHORT).show();
                                }
                            } else {
//                                Toast.makeText(PartnerGiftActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(PartnerGiftActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout1:
                //点击了收获
                first.setTextColor(getResources().getColor(R.color.title_bg));
                second.setTextColor(getResources().getColor(R.color.black));
                third.setTextColor(getResources().getColor(R.color.black));
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.INVISIBLE);
                // 收的打赏
                flag = 0;
                index = 1;
                receivelist.clear();
                ReceiveGifts(index);
                partnerGiftAdapter = new PartnerReceiveGiftAdapter(PartnerGiftActivity.this, receivelist);
                reward_list.setAdapter(partnerGiftAdapter);
                break;
            case R.id.layout2:
                //点击了打赏
                first.setTextColor(getResources().getColor(R.color.black));
                second.setTextColor(getResources().getColor(R.color.title_bg));
                third.setTextColor(getResources().getColor(R.color.black));
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);
                line3.setVisibility(View.INVISIBLE);
                // 送出的打赏
                flag = 1;
                index = 1;
                sendlist.clear();
                SendGifts(index);
                partnerSendGiftAdapter = new PartnerSendGiftAdapter(PartnerGiftActivity.this, sendlist);
                reward_list.setAdapter(partnerSendGiftAdapter);
                break;
            case R.id.layout3: //点击了变现
                first.setTextColor(getResources().getColor(R.color.black));
                second.setTextColor(getResources().getColor(R.color.black));
                third.setTextColor(getResources().getColor(R.color.title_bg));
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.VISIBLE);
                // 变现礼物记录
                flag = 2;
                index = 1;
                selllist.clear();
                SellGiftLogs(index);
                sellGiftLogAdapter = new SellGiftLogAdapter(PartnerGiftActivity.this, selllist);
                reward_list.setAdapter(sellGiftLogAdapter);
                break;
            case R.id.sellALLGift:
                // 礼物变现
                SellALLGift();
                break;
            default:
                break;
        }
    }

    /**
     * 礼物变现
     */
    private void SellALLGift() {
        String url = "https://qrb.shoomee.cn/api/sellALLGift";
        OkHttpUtils
                .post()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("", "")
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(PartnerGiftActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        Log.v("Exception", e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.e("礼物变现", object.toString());
                            if (object.optString("result").equals("success")) {
                                // 变现成功，现有礼物为0
                                gift_total.setText("￥ 0.00");
                            } else {
                                Toast.makeText(PartnerGiftActivity.this, "暂时还没有礼物哦", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(PartnerGiftActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }
}
