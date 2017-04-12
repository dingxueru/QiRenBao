package com.xiumi.qirenbao.order.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
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
import com.xiumi.qirenbao.order.OrderCommentActivity;
import com.xiumi.qirenbao.order.bean.PartnerOrderListBean;
import com.xiumi.qirenbao.reward.expert.MasterRewardActivity;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.widget.PullToRefreshLayout;
import com.xiumi.qirenbao.zfb.PayResult;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 作者 ：Created by DXR on 2017/3/18.
 * 达人订单 adapter 进行中
 */

public class MasterOrderListAdapter extends BaseAdapter implements IWXAPIEventHandler {
    private static final int SDK_PAY_FLAG = 1;
    private Activity context;
    private ArrayList<PartnerOrderListBean> orderlist = new ArrayList<PartnerOrderListBean>();
    PullToRefreshLayout mPullRefreshScrollView;
    PayReq request = new PayReq();
    IWXAPI api = WXAPIFactory.createWXAPI(context, "wx562ae079bb7fe403");

    public MasterOrderListAdapter(Activity context, ArrayList<PartnerOrderListBean> orderlist, PullToRefreshLayout mPullRefreshScrollView) {
        this.context = context;
        this.orderlist = orderlist;
        this.mPullRefreshScrollView = mPullRefreshScrollView;
    }

    @Override
    public int getCount() {
        return orderlist.size();
    }

    @Override
    public Object getItem(int position) {
        return orderlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PartnerOrderListBean item = orderlist.get(position);
        ViewHolder viewHolder = null;
        viewHolder = new ViewHolder();
        if (item.status == 1) {
            convertView = LayoutInflater.from(context).inflate(R.layout.master_order_list_item_layout, null, false);
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.master_order_list_item_new_layout, null, false);

        }
        convertView.setTag(viewHolder);

        switch (item.status) {
            case 1:
                // 标题：接受，谢绝
                viewHolder.order_title = (TextView) convertView.findViewById(R.id.order_title);
                viewHolder.order_time = (TextView) convertView.findViewById(R.id.order_time);
                viewHolder.distribute_log = (TextView) convertView.findViewById(R.id.distribute_log);
                viewHolder.service_name = (TextView) convertView.findViewById(R.id.service_name);
                viewHolder.company_service = (TextView) convertView.findViewById(R.id.company_service);
                viewHolder.reject = (RadioButton) convertView.findViewById(R.id.reject);
                viewHolder.receive = (RadioButton) convertView.findViewById(R.id.receive);
                // 赋值
                if (!TextUtils.isEmpty(item.created_at)) {
                    viewHolder.order_time.setText(item.created_at);
                }
                // 订单标题
                if (!TextUtils.isEmpty(item.name)) {
                    viewHolder.order_title.setText(item.name);
                } else {
                    viewHolder.order_title.setText("暂无");
                }
                // 达人接受
                viewHolder.receive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReceiveOrder(item.id);
                    }
                });
                // 达人谢绝
                viewHolder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RefuseOrder(item.id);
                    }
                });
                // 跟进人
                if (item.service_user != null) {
                    if (!TextUtils.isEmpty(item.service_user.name)) {
                        viewHolder.distribute_log.setText("队长分配给" + item.service_user.name);
                    }
                }
                // 客服
                if (item.tel_service_user != null) {
                    if (!TextUtils.isEmpty(item.tel_service_user.name)) {
                        viewHolder.service_name.setText(item.tel_service_user.name);
                    }
                }
                // 企业主
                if (item.company_user != null) {
                    if (!TextUtils.isEmpty(item.company_user.name)) {
                        viewHolder.company_service.setText(item.company_user.name);
                    }
                } else {
                    viewHolder.company_service.setText("暂无");
                }

                break;
            case 2:
                //  标题：打赏
                viewHolder.out_title = (TextView) convertView.findViewById(R.id.out_title);
                viewHolder.out_time = (TextView) convertView.findViewById(R.id.out_time);
                viewHolder.master_name = (TextView) convertView.findViewById(R.id.master_name);
                viewHolder.out_service = (TextView) convertView.findViewById(R.id.out_service);
                viewHolder.out_company = (TextView) convertView.findViewById(R.id.out_company);
                viewHolder.reward = (TextView) convertView.findViewById(R.id.reward);
                viewHolder.pay = (TextView) convertView.findViewById(R.id.pay);
                // 赋值
                if (!TextUtils.isEmpty(item.created_at)) {
                    viewHolder.out_time.setText(item.created_at);
                }
                // 订单标题
                if (!TextUtils.isEmpty(item.name)) {
                    viewHolder.out_title.setText(item.name);
                }
                // 跟进人
                if (item.service_user != null) {
                    if (!TextUtils.isEmpty(item.service_user.name)) {
                        viewHolder.master_name.setText(item.service_user.name);
                    }
                }
                // 客服
                if (item.tel_service_user != null) {
                    if (!TextUtils.isEmpty(item.tel_service_user.name)) {
                        viewHolder.out_service.setText(item.tel_service_user.name);
                    }
                }
                // 企业主
                if (item.company_user != null) {
                    if (!TextUtils.isEmpty(item.company_user.name)) {
                        viewHolder.out_company.setText(item.company_user.name);
                    }
                }
                /**
                 * 需判断 有无生成订单 是 “继续支付”，还是“打赏”
                 */
                if (item.pay_order != null) {
                    viewHolder.pay.setVisibility(View.VISIBLE);
                    // 判断是哪种支付方式
                    if (item.pay_order.pay_type.equals("alipay")) {
                        /**
                         * 需完善，继续调起 支付宝 支付
                         */
                        viewHolder.pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AliPay(item.pay_order.out_trade_no, item.pay_order.total_fee, item.pay_order.subject);
                            }
                        });

                    } else {
                        /**
                         * 需完善，继续调起 微信 支付
                         */
                        viewHolder.pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // paywxCash(item.pay_order.out_trade_no, item.pay_order.body, item.pay_order.total_fee);
                                Toast.makeText(context, "微信支付功能正在完善，敬请期待", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    viewHolder.reward.setVisibility(View.VISIBLE);
                    viewHolder.reward.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, MasterRewardActivity.class);
                            intent.putExtra("order_id", item.id);
                            intent.putExtra("tel_service_id", item.tel_service_id + "");
                            context.startActivity(intent);
                        }
                    });
                }
                break;
            case 3:
                //  标题：已拒绝
                viewHolder.out_title = (TextView) convertView.findViewById(R.id.out_title);
                viewHolder.out_time = (TextView) convertView.findViewById(R.id.out_time);
                viewHolder.master_name = (TextView) convertView.findViewById(R.id.master_name);
                viewHolder.out_service = (TextView) convertView.findViewById(R.id.out_service);
                viewHolder.out_company = (TextView) convertView.findViewById(R.id.out_company);
                viewHolder.refuse = (TextView) convertView.findViewById(R.id.refuse);
                viewHolder.refuse.setVisibility(View.VISIBLE);
                // 赋值
                if (!TextUtils.isEmpty(item.created_at)) {
                    viewHolder.out_time.setText(item.created_at);
                }
                // 订单标题
                if (!TextUtils.isEmpty(item.name)) {
                    viewHolder.out_title.setText(item.name);
                }
                // 企业主
                if (item.company_user != null) {
                    if (!TextUtils.isEmpty(item.company_user.name)) {
                        viewHolder.out_company.setText(item.company_user.name);
                    }
                }
                break;
            case 4:
                /**
                 * 判断order_comment
                 p2t_labels 达人对客服  或
                 p2c_labels 达人对企业主-
                 c2p_labels 企业主对团队
                 c2t_labels 企业主对客服
                 对应值为空，按钮数量：1   标题：评价
                 对应值不为空，按钮数量：1   标题：完成
                 */
                viewHolder.out_title = (TextView) convertView.findViewById(R.id.out_title);
                viewHolder.out_time = (TextView) convertView.findViewById(R.id.out_time);
                viewHolder.master_name = (TextView) convertView.findViewById(R.id.master_name);
                viewHolder.out_service = (TextView) convertView.findViewById(R.id.out_service);
                viewHolder.out_company = (TextView) convertView.findViewById(R.id.out_company);
                viewHolder.evaluate = (TextView) convertView.findViewById(R.id.evaluate); //去评价
                viewHolder.complete = (TextView) convertView.findViewById(R.id.complete); //完成
                viewHolder.my_evaluate = (TextView) convertView.findViewById(R.id.my_evaluate);
                if (item.order_comment != null) {
                    // 如果有客服介入
                    if (item.tel_service_user != null) {
                        if (StringUtils.isEmpty(item.order_comment.p2t_labels) || StringUtils.isEmpty(item.order_comment.p2c_labels)) {
                            viewHolder.evaluate.setVisibility(View.VISIBLE);
                            /**
                             *  跳转到评价页面
                             */
                            viewHolder.evaluate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, OrderCommentActivity.class);
                                    intent.putExtra("company_user_name", item.company_user.name);
                                    intent.putExtra("tel_name", item.tel_service_user.name);
                                    intent.putExtra("tel_service_id", item.tel_service_user.id + "");
                                    intent.putExtra("order_id", item.id + "");
                                    intent.putExtra("company_user_id", item.company_user_id + "");
                                    intent.putExtra("avatar", item.company_user.user_info.avatar);
                                    context.startActivity(intent);
                                }
                            });
                        } else {
                            // 企业主对客服，企业主对团队
                            if (StringUtils.isEmpty(item.order_comment.c2p_labels) || StringUtils.isEmpty(item.order_comment.c2t_labels)) {
                                viewHolder.my_evaluate.setVisibility(View.VISIBLE); // 已评价
                            } else {
                                viewHolder.complete.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        // 没有客服介入，判断达人对企业主
                        if (StringUtils.isEmpty(item.order_comment.p2c_labels)) {
                            viewHolder.evaluate.setVisibility(View.VISIBLE);
                            /**
                             *  跳转到评价页面
                             */
                            viewHolder.evaluate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, OrderCommentActivity.class);
                                    intent.putExtra("company_user_name", item.company_user.name);
                                    intent.putExtra("order_id", item.id + "");
                                    intent.putExtra("company_user_id", item.company_user_id + "");
                                    intent.putExtra("avatar", item.company_user.user_info.avatar);
                                    context.startActivity(intent);
                                }
                            });
                        } else {
                            if (StringUtils.isEmpty(item.order_comment.c2p_labels)) {
                                viewHolder.my_evaluate.setVisibility(View.VISIBLE);
                            } else {
                                viewHolder.complete.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    /**
                     *  跳转到评价页面
                     */
                    viewHolder.evaluate.setVisibility(View.VISIBLE);
                    viewHolder.evaluate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, OrderCommentActivity.class);
                            intent.putExtra("company_user_name", item.company_user.name);
                            if (item.tel_service_user != null) {
                                intent.putExtra("tel_name", item.tel_service_user.name);
                                intent.putExtra("tel_service_id", item.tel_service_user.id + "");
                            }
                            intent.putExtra("order_id", item.id + "");
                            intent.putExtra("company_user_id", item.company_user_id + "");
                            intent.putExtra("avatar", item.company_user.user_info.avatar);
                            context.startActivity(intent);

                        }
                    });
                }
                // 赋值
                if (!TextUtils.isEmpty(item.created_at)) {
                    viewHolder.out_time.setText(item.created_at);
                }
                // 订单标题
                if (!TextUtils.isEmpty(item.name)) {
                    viewHolder.out_title.setText(item.name);
                }
                // 跟进人
                if (item.service_user != null) {
                    if (!TextUtils.isEmpty(item.service_user.name)) {
                        viewHolder.master_name.setText(item.service_user.name);
                    }
                }
                // 客服
                if (item.tel_service_user != null) {
                    if (!TextUtils.isEmpty(item.tel_service_user.name)) {
                        viewHolder.out_service.setText(item.tel_service_user.name);
                    }
                }
                // 企业主
                if (item.company_user != null) {
                    if (!TextUtils.isEmpty(item.company_user.name)) {
                        Log.v("company_user.name", item.company_user.name);
                        viewHolder.out_company.setText(item.company_user.name);
                    } else {
                        viewHolder.out_company.setText("");
                    }
                }
                break;
            default:
                break;
        }
        return convertView;
    }

    /**
     * 达人接单
     */
    private void ReceiveOrder(int order_id) {
        String url = "https://qrb.shoomee.cn/api/receiveOrder";
        OkHttpUtils
                .post()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("order_id", order_id + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                        Log.v("Exception", e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.v("达人接单", object.toString());
                            // 待数据完善，添加页面逻辑交互
                            if (object.optString("result").equals("success")) {
                                Toast.makeText(context, "接单成功", Toast.LENGTH_SHORT).show();
                                mPullRefreshScrollView.autoRefresh();
                            } else {
                                Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }

    /**
     * 达人拒绝合伙人分配的订单
     */
    private void RefuseOrder(int order_id) {
        String url = "https://qrb.shoomee.cn/api/refuseOrder";
        OkHttpUtils
                .post()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("order_id", order_id + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.v("达人拒绝", object.toString());
                            // 待数据完善，添加页面逻辑交互
                            if (object.optString("result").equals("success")) {
                                Toast.makeText(context, "拒绝成功", Toast.LENGTH_SHORT).show();
                                mPullRefreshScrollView.autoRefresh();
                            } else {
                                Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            Log.e("errCode", "onPayFinish,errCode=" + baseResp.errCode);
            builder.setTitle("提示");
            builder.setMessage(context.getString(R.string.pay_result_callback_msg, String.valueOf(baseResp.errCode)));
            builder.show();
        }
    }

    // 各个布局的控件资源
    private class ViewHolder {
        // status为1
        TextView order_title;
        TextView order_time;
        TextView distribute_log;
        TextView service_name;
        TextView company_service; // 企业主
        RadioButton reject; // 拒绝
        RadioButton receive; // 接受

        // status 除 1 以外的状态
        TextView out_title;
        TextView out_time;
        TextView master_name;
        TextView out_service;
        TextView out_company; //  企业主

        TextView reward;// 打赏 按钮
        TextView pay; // 继续支付 按钮
        TextView refuse; // 已拒绝 按钮
        TextView evaluate; // 去评价 按钮
        TextView complete; // 完成 按钮
        TextView my_evaluate; // 自己已评价

    }

    /**
     * 支付宝支付
     */
    private void AliPay(String out_trade_no, String total_fee, String subject) {
        String url = "https://qrb.shoomee.cn/api/alipay?out_trade_no=" + out_trade_no + "&subject=" + subject + "&total_fee=" + total_fee;
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(final String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            final String data = obj.optString("data");
                            Log.v("支付宝rsa", data);
                            // 调起支付宝
                            try {
                                Runnable payRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        // 构造PayTask 对象
                                        PayTask alipay = new PayTask(context);
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
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 微信支付
     */
    private void paywxCash(String out_trade_no, String body, String total_fee) {
        String url = "https://qrb.shoomee.cn/api/weixin?out_trade_no=" + out_trade_no + "&body=" + body + "&total_fee=" + total_fee;
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject data = obj.optJSONObject("data");
                            Log.e("data", data.toString());
                            String appid = data.optString("appid");
                            String partnerid = data.optString("partnerid");
                            String prepayid = data.optString("prepayid");
                            String packageStr = data.optString("package");
                            String noncestr = data.optString("noncestr");
                            String timestamp = data.optString("timestamp");
                            String sign = data.optString("sign");
                            request.appId = data.optString("appid");
                            request.partnerId = data.optString("partnerid");
                            request.prepayId = data.optString("prepayid");
                            request.packageValue = packageStr;
                            request.nonceStr = data.optString("noncestr");
                            request.timeStamp = data.optString("timestamp");
                            request.sign = data.optString("sign");
                            request.extData = "app data";
                            api.sendReq(request);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


}
