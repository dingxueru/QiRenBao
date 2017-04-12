package com.xiumi.qirenbao.reward.expert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.reward.bean.GiftBean;
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

import static com.xiumi.qirenbao.reward.expert.MasterRewardActivity.GIFT;
import static com.xiumi.qirenbao.reward.expert.MasterRewardActivity.GIFT1;
import static com.xiumi.qirenbao.reward.expert.MasterRewardActivity.isToHeader;

/**
 * 作者 ：Created by qianbailu on 2017/3/20.
 * 达人打赏选择页面
 */

public class MasterRewardSelectActivity extends Activity{
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.gift_name)
    TextView giftName;
    @Bind(R.id.gift_price)
    TextView giftPrice;
    @Bind(R.id.no_reduce)
    ImageView noReduce;
    @Bind(R.id.next_add)
    ImageView nextAdd;
    @Bind(R.id.buy_reduce)
    ImageView buyReduce;
    @Bind(R.id.total_order)
    TextView total_order;
    @Bind(R.id.buy_add)
    ImageView buy_add;
    @Bind(R.id.next)
    TextView next;
    @Bind(R.id.gift_pic)
    ImageView gift_pic;
    private int i=0,price=0;
    private String id,img;
    private List<GiftBean> giftBeanList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_select);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getGiftDetail();
        setStatusBar();
    }
    @OnClick(R.id.buy_reduce)
    public void setBuyReduce(View view){
        String totalFree=total_order.getText().toString().trim();
        if(totalFree.equals("1")){
            Toast.makeText(MasterRewardSelectActivity.this,"礼物数量不能小于1哦",Toast.LENGTH_SHORT).show();
        }else{
            int free= Integer.parseInt(totalFree);
            free=free-1;
            total_order.setText(free+"");
        }
    }
    @OnClick(R.id.next)
    public void setNext(View view){
        String totalFree=total_order.getText().toString().trim();
        int free= Integer.parseInt(totalFree);
        int totalChange=free*price;
        if(isToHeader==true){
            Intent intent=new Intent();
            intent.putExtra("HeaderName",giftName.getText().toString());
            intent.putExtra("gift_for_executor_total",total_order.getText().toString());
            intent.putExtra("toHeaderChange",totalChange);
            intent.putExtra("gift_for_executor_id",id);
            intent.putExtra("img",img);
            setResult(GIFT, intent);
            Log.e("toHeaderChange",giftName.getText().toString());
            finish();
        }else {
            Intent intent = new Intent();
            intent.putExtra("name", giftName.getText().toString());
            intent.putExtra("free", total_order.getText().toString());
            intent.putExtra("toWaiterChange", totalChange);
            intent.putExtra("id", id);
            intent.putExtra("img",img);
            setResult(GIFT1, intent);
            Log.e("free",total_order.getText().toString());
            finish();
        }
    }
    @OnClick(R.id.buy_add)
    public void  setBuy_add(View view){
        String totalFree=total_order.getText().toString().trim();
        int free= Integer.parseInt(totalFree);
        free=free+1;
        total_order.setText(free+"");
    }
    @OnClick(R.id.no_reduce)
    public void setNoReduce(View view) {
        if (i == 0) {
            Toast.makeText(MasterRewardSelectActivity.this, "前面没有数据了", Toast.LENGTH_SHORT).show();
        } else {
            //上一页
            total_order.setText("1");
            --i;
            GiftBean giftBean = giftBeanList.get(i);
            img = "https://qrb.shoomee.cn/upload/" + giftBean.icon;
            Glide.with(MasterRewardSelectActivity.this).load(img).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESULT).into(gift_pic);
            giftName.setText(giftBean.name);
            giftPrice.setText("¥ " + giftBean.price);
            price = Integer.parseInt(giftBean.price);
            id = giftBean.id;
        }
    }
    @OnClick(R.id.next_add)
    public void setNextAdd(View view){

                if(i==giftBeanList.size()-1){
                    Toast.makeText(MasterRewardSelectActivity.this,"后面没有数据了",Toast.LENGTH_SHORT).show();
                }else{
                    total_order.setText("1");
                    //下一页
                    ++i;
                    GiftBean giftBean=giftBeanList.get(i);
                    giftName.setText(giftBean.name);
                    img="https://qrb.shoomee.cn/upload/"+giftBean.icon;
                    Glide.with(MasterRewardSelectActivity.this).load(img).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESULT).into(gift_pic);
                    giftPrice.setText("¥ "+giftBean.price);
                    price= Integer.parseInt(giftBean.price);
                    id=giftBean.id;
                }

    }
    /**
     * 获取打赏的礼物类别
     */

    private void getGiftDetail(){
        String url= "https://qrb.shoomee.cn/qrb_api/getAllGifts";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MasterRewardSelectActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            giftBeanList = JSON.parseArray(obj.getJSONArray("data").toString(), GiftBean.class);
                            GiftBean giftBean=giftBeanList.get(i);
                            img="https://qrb.shoomee.cn/upload/"+giftBean.icon;
                            Glide.with(MasterRewardSelectActivity.this).load(img).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESULT).into(gift_pic);
                            giftName.setText(giftBean.name);
                            giftPrice.setText("¥ "+giftBean.price);
                            price= Integer.parseInt(giftBean.price);
                            id=giftBean.id;


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MasterRewardSelectActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
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
}
