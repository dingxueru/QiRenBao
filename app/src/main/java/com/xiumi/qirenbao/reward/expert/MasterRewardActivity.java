package com.xiumi.qirenbao.reward.expert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.reward.bean.GiftOrderBean;
import com.xiumi.qirenbao.reward.partnership.PartnerRewardActivity;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.xiumi.qirenbao.zfb.PayResult;
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
 * 作者 ：Created by qianbailu on 2017/3/21.
 * 达人打赏
 */

public class MasterRewardActivity extends Activity implements IWXAPIEventHandler {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.reward_head)
    TextView reward_head;
    @Bind(R.id.reward_service)
    TextView reward_service;
    @Bind(R.id.chose_cash)
    TextView choseCash;
    @Bind(R.id.replays)
    LinearLayout replays;
    @Bind(R.id.replay_waiter)
    LinearLayout replay_waiter;
    @Bind(R.id.gift_name)
    TextView giftName;
    @Bind(R.id.gift_pic)
    ImageView gift_pic;
    @Bind(R.id.gift_count)
    TextView giftCount;
    @Bind(R.id.total_free)
    TextView total_free;
    @Bind(R.id.waiter_gift_name)
    TextView waiter_gift_name;
    @Bind(R.id.waiter_gift_img)
    ImageView waiter_gift_img;
    @Bind(R.id.waiter_gift_count)
    TextView waiter_gift_count;
    @Bind(R.id.sure)
    TextView sure;
    @Bind(R.id.text_mess)
    TextView text_mess;
    private static final int SDK_PAY_FLAG = 1;
    private PopupWindow mPopuWindow;
    private View contentView;
    PayReq request ;
    IWXAPI api;
    public static final int REQUSET = 1;//金额
    public static final int GIFT = 2;//打赏团长的礼物
    public static final int GIFT1 = 3;//打赏团长的礼物
    public static  boolean isToHeader=true;
    private String money="0";
    private  String free="0",id="0",gift_for_executor_id="0",gift_for_executor_total="0";
    private int order_id,toHeaderChange=0,toWaiterChange=0;
    private String type="alipay";
    private void initPopuWindow(View parent) {
        if (mPopuWindow == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(this);
            contentView = mLayoutInflater.inflate(R.layout.chose_pay_layout, null);
            mPopuWindow=new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopuWindow.setBackgroundDrawable(cd);
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        mPopuWindow.setWidth(width);
        //产生背景变暗效果
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);

        mPopuWindow.setOutsideTouchable(true);
        mPopuWindow.setFocusable(true);
        mPopuWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopuWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopuWindow.showAtLocation((View)parent.getParent(), Gravity.BOTTOM, 0, 0);
        RadioButton zfb= (RadioButton) contentView.findViewById(R.id.menu_zfb);
        RadioButton wx= (RadioButton) contentView.findViewById(R.id.menu_wx);
        Button btnpublish= (Button) contentView.findViewById(R.id.btnpublish);
        RadioGroup layoutNav= (RadioGroup) contentView.findViewById(R.id.layout_nav);
        zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        layoutNav.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.menu_zfb:
                        //支付宝付款
                        type="alipay";
                        break;
                    case R.id.menu_wx:
                        //微信充值
                        type="wxpay";
                        break;
                }
            }
        });
        btnpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOrder(type);
                mPopuWindow.dismiss();
            }
        });
        mPopuWindow.update();
        mPopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener(){

            //在dismiss中恢复透明度
            public void onDismiss(){
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_reward);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        order_id=getIntent().getIntExtra("order_id",-1);
        String tel_service_id=getIntent().getStringExtra("tel_service_id");
        if(tel_service_id.equals("null")){
            text_mess.setVisibility(View.GONE);
            reward_service.setVisibility(View.GONE);
        }else{
            text_mess.setVisibility(View.VISIBLE);
            reward_service.setVisibility(View.VISIBLE);
        }
        setStatusBar();
        api = WXAPIFactory.createWXAPI(MasterRewardActivity.this, "wx562ae079bb7fe403");
        request= new PayReq();
        // 打赏团长
        reward_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isToHeader=true;
                Intent intent = new Intent(MasterRewardActivity.this,MasterRewardSelectActivity.class);
                startActivityForResult(intent,GIFT);
            }
        });
        replays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isToHeader=true;
                Intent intent = new Intent(MasterRewardActivity.this,MasterRewardSelectActivity.class);
                startActivityForResult(intent,GIFT);
            }
        });
        // 打赏客服
        reward_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isToHeader=false;
                Intent intent = new Intent(MasterRewardActivity.this,MasterRewardSelectActivity.class);
                startActivityForResult(intent,GIFT1);
            }
        });
        replay_waiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isToHeader=false;
                Intent intent = new Intent(MasterRewardActivity.this,MasterRewardSelectActivity.class);
                startActivityForResult(intent,GIFT1);
            }
        });
    }
    @OnClick(R.id.chose_cash)
    public void setChoseCash(View view){
        //选择金额
        Intent intent = new Intent(MasterRewardActivity.this,MasterRewardChoseActivity.class);
        startActivityForResult(intent,REQUSET);
    }
    @OnClick(R.id.sure)
    public void setSure(View view){
        //initPopuWindow(view);
        if(total>0){
            setOrder("alipay");
        }else{
            Toast.makeText(MasterRewardActivity.this, "打赏金额必须大于0", Toast.LENGTH_SHORT).show();

        }

    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(MasterRewardActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(MasterRewardActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(MasterRewardActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
    /**
     * 支付宝支付
     */
    private void AliPay(String out_trade_no, String total_fee, String subject) {
        String url= "https://qrb.shoomee.cn/api/alipay?out_trade_no="+out_trade_no+"&subject="+subject+"&total_fee="+total_fee;
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MasterRewardActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(final String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String data=obj.optString("data");
                            Log.v("支付宝rsa",data);
                            // 调起支付宝
                            try {
                                Runnable payRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        // 构造PayTask 对象
                                        PayTask alipay = new PayTask(MasterRewardActivity.this);
                                        // 调用支付接口，获取支付结果
                                        String result = alipay.pay(data, true);
                                        Message msg = new Message();
                                        msg.what = SDK_PAY_FLAG;
                                        msg.obj = result;
                                        mHandler.sendMessage(msg);
                                    }
                                };
                                // 必须异步调用
                                Thread payThread = new Thread(payRunnable);
                                payThread.start();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
    private int total=0;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
               try{
                   money=data.getStringExtra("cash");
                   choseCash.setText(data.getStringExtra("cash"));
                   total=toHeaderChange+toWaiterChange+Integer.parseInt(money);
                   total_free.setText("总值 ："+total+".00 元");
               }catch (Exception e){

               }
                break;
            case 2:
                try {
                    String name=data.getStringExtra("HeaderName");
                    Log.e("name",name);
                    String img=data.getStringExtra("img");
                    Glide.with(MasterRewardActivity.this).load(img).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESULT).into(gift_pic);
                    giftName.setText(name);
                    toHeaderChange=data.getIntExtra("toHeaderChange",0);
                    if(data.getStringExtra("gift_for_executor_total")!=null){
                        gift_for_executor_total=data.getStringExtra("gift_for_executor_total");
                        gift_for_executor_id=data.getStringExtra("gift_for_executor_id");
                        replays.setVisibility(View.VISIBLE);
                        reward_head.setVisibility(View.GONE);
                        giftCount.setText("X "+gift_for_executor_total);
                    }else{
                        replays.setVisibility(View.GONE);
                        reward_head.setVisibility(View.VISIBLE);
                    }
                   total=toHeaderChange+toWaiterChange+Integer.parseInt(money);
                    total_free.setText("总值 ："+total+".00 元");
                 }catch (Exception e){

                 }
                break;
            case 3:
                 try{
                     String waitName=data.getStringExtra("name");
                     waiter_gift_name.setText(waitName);
                     String img=data.getStringExtra("img");
                     Glide.with(MasterRewardActivity.this).load(img).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESULT).into(waiter_gift_img);
                     toWaiterChange=data.getIntExtra("toWaiterChange",0);
                     if(data.getStringExtra("free")!=null){
                         free=data.getStringExtra("free");
                         id=data.getStringExtra("id");
                         replay_waiter.setVisibility(View.VISIBLE);
                         reward_service.setVisibility(View.GONE);
                         waiter_gift_count.setText("X "+free);
                     }else{
                         replay_waiter.setVisibility(View.GONE);
                         reward_service.setVisibility(View.VISIBLE);
                     }
                    total=toHeaderChange+toWaiterChange+Integer.parseInt(money);
                     total_free.setText("总值 ："+total+".00 元");
                 }catch (Exception e){

                 }

                break;
            default:
                break;
        }
    }
    /**
     * 微信支付
     */
    private void paywxCash(String out_trade_no,String body,String total_fee){
        String url= "https://qrb.shoomee.cn/api/weixin?out_trade_no="+out_trade_no+"&body="+body+"&total_fee="+total_fee;
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MasterRewardActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject data = obj.optJSONObject("data");
                            Log.e("data",data.toString());
                            String appid=data.optString("appid");
                            String partnerid=data.optString("partnerid");
                            String prepayid=data.optString("prepayid");
                            String packageStr=data.optString("package");
                            String noncestr=data.optString("noncestr");
                            String timestamp=data.optString("timestamp");
                            String sign=data.optString("sign");
                            request.appId = data.optString("appid");
                            request.partnerId = data.optString("partnerid");
                            request.prepayId= data.optString("prepayid");
                            request.packageValue = packageStr;
                            request.nonceStr= data.optString("noncestr");
                            request.timeStamp= data.optString("timestamp");
                            request.sign= data.optString("sign");
                            request.extData	= "app data";
                            api.sendReq(request);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * 完成订单并生成打赏支付订单
     */
    private void setOrder(final String pay_type) {
        String jieguo = total_free.getText().toString().substring(4,total_free.getText().toString().indexOf("."));
        String url = "https://qrb.shoomee.cn/api/createRewardOrder";
        OkHttpUtils
                .post()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("gift_for_executor_id",gift_for_executor_id)
                .addParams("gift_for_executor_total", gift_for_executor_total)
                .addParams("gift_for_tel_id", id)
                .addParams("gift_for_tel_total", free)
                .addParams("commission_fee",money)
                .addParams("total_fee", "0.01") // jieguo.toString()
                .addParams("order_id",order_id+"")
                .addParams("pay_type",pay_type)
                .addParams("subject","Android 订单")
                .addParams("body","Android 订单")
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MasterRewardActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.v("合伙人的订单列表", obj.toString());
                            if (obj.getString("result").equals("success")) {
                                List<GiftOrderBean> temp  = JSON.parseObject(obj.optJSONArray("data").toString(), new TypeReference<ArrayList<GiftOrderBean>>() {});
                                GiftOrderBean giftOrderBean=temp.get(0);
                                if(pay_type.equals("alipay")) {
                                    AliPay(giftOrderBean.out_trade_no, giftOrderBean.total_fee, giftOrderBean.subject);
                                }else {
                                //    paywxCash(giftOrderBean.out_trade_no, giftOrderBean.body, giftOrderBean.total_fee);
                                    Toast.makeText(MasterRewardActivity.this,"微信支付功能正在完善，敬请期待",Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MasterRewardActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MasterRewardActivity.this);
            Log.e("errCode","onPayFinish,errCode="+baseResp.errCode);
            builder.setTitle("提示");
            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(baseResp.errCode)));
            builder.show();
        }
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
