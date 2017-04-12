package com.xiumi.qirenbao.home.company;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.home.adapter.ChoseSkillAdapter;
import com.xiumi.qirenbao.home.adapter.CommendAdapter;
import com.xiumi.qirenbao.home.adapter.SkillNameAdapter;
import com.xiumi.qirenbao.home.bean.ParterDetailBean;
import com.xiumi.qirenbao.login.LoginActivity;
import com.xiumi.qirenbao.order.OrderCommentActivity;
import com.xiumi.qirenbao.order.bean.CommentBean;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.widget.CircleImageView;
import com.xiumi.qirenbao.widget.FullGridView;
import com.xiumi.qirenbao.widget.ListViewForScrollView;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiumi.qirenbao.home.company.CompanySearchActivity.COMPANY;
import static com.xiumi.qirenbao.home.company.SerchResultActivity.SERCH;

/**
 * Created by qianbailu on 2017/3/7.
 * 下单界面
 */

public class PlaceOrderActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.head)
    CircleImageView head;
    @Bind(R.id.captain_name)
    TextView captainName;
    @Bind(R.id.captain_occupation)
    TextView captainOccupation;
    @Bind(R.id.user_lv)
    TextView userLv;
    @Bind(R.id.work_years)
    TextView workYears;
    @Bind(R.id.work_duty)
    TextView workDuty;
    @Bind(R.id.company)
    TextView company;
    @Bind(R.id.description)
    TextView description;
    @Bind(R.id.base_list)
    ListViewForScrollView command;
    @Bind(R.id.commend_item)
    TextView commend_item;
    @Bind(R.id.pay_order)
    Button payOrder;
    @Bind(R.id.sex)
    ImageView sex;
    @Bind(R.id.skill_girde)
    FullGridView skill_girde;
    @Bind(R.id.name_title)
    TextView name_title;
    private CommendAdapter commendAdapter = null;
    private String team_id, user_id;
    private PopupWindow mPopuWindow;
    private View contentView;
    private MainActivity mactivity;
    List<String> skillName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        ButterKnife.bind(this);
        setStatusBar();
        getCommentLaber();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String keyword = getIntent().getStringExtra("user_id");
        String user_name = getIntent().getStringExtra("user_name");
        name_title.setText(user_name);
        getpeopleSerch(keyword);
    }

    @OnClick(R.id.pay_order)
    public void setPayOrder(final View view) {
        //下单
        if (StringUtils.isEmpty(MainActivity.id)) {
            new AlertDialog.Builder(PlaceOrderActivity.this).setMessage("你还没有登录，请先登陆再下单")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(PlaceOrderActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }

                    }).show();
        } else {
            initPopuWindow(view);
        }
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
     * 获取评价标签
     */
    List<String> commentId = new ArrayList<>();
    List<String> commentName = new ArrayList<>();

    private void getCommentLaber() {
        String url = "https://qrb.shoomee.cn/qrb_api/getCommentTags";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(PlaceOrderActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("获取评价标签", obj.toString());
                            List<CommentBean> temp = JSON.parseArray(obj.getJSONArray("data").toString(), CommentBean.class);
                            for (int i = 0; i < temp.size(); i++) {
                                //对团队的评价
                                commentName.add(temp.get(i).content);
                                commentId.add(temp.get(i).id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PlaceOrderActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    /**
     * 企业主订单创建
     */
    private void setOrder(String name) {

        String url = "https://qrb.shoomee.cn/api/createOrder";
        OkHttpUtils
                .post()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("team_id", team_id)
                .addParams("partner_user_id", user_id)
                .addParams("name", name)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(PlaceOrderActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("企业主订单创建", obj.toString());
                            String result = obj.getString("result");
                            if (result.equals("success")) {
                                new AlertDialog.Builder(PlaceOrderActivity.this).setMessage("下单成功，稍后会有客服联系您")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                MainActivity.isOrder = true;
                                                SERCH.finish();
                                                try {
                                                    COMPANY.finish();
                                                } catch (Exception e) {

                                                }
                                                finish();

                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PlaceOrderActivity.this, "网络异常", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    /**
     * 用户详情
     */
    private void initPopuWindow(View parent) {
        if (mPopuWindow == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(this);
            contentView = mLayoutInflater.inflate(R.layout.skill_layout, null);
            mPopuWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopuWindow.setBackgroundDrawable(cd);
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        mPopuWindow.setWidth(width);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f;
        getWindow().setAttributes(lp);

        mPopuWindow.setOutsideTouchable(true);
        mPopuWindow.setFocusable(true);
        mPopuWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopuWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopuWindow.showAtLocation((View) parent.getParent(), Gravity.CENTER, 0, 0);
        final ListViewForScrollView list = (ListViewForScrollView) contentView.findViewById(R.id.list);
        ChoseSkillAdapter choseSkillAdapter = new ChoseSkillAdapter(skillName, PlaceOrderActivity.this);
        list.setAdapter(choseSkillAdapter);
        choseSkillAdapter.notifyDataSetChanged();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(PlaceOrderActivity.this).setMessage("你确定要给该用户的" + skillName.get(position) + "下单吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setOrder(skillName.get(position));
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }

                        }).show();

            }
        });
        mPopuWindow.update();
        mPopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

    }

    /*
    获取合伙人详情
     */
    private void getpeopleSerch(String keyword) {
        String url = "https://qrb.shoomee.cn/qrb_api/getPartner?user_id=" + keyword;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(PlaceOrderActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("合伙人", obj.toString());
                            String result = obj.getString("result");
                            command.setAdapter(null);
                            if (result.equals("success")) {
                                Log.e("data", obj.optJSONArray("data").toString());
                                List<ParterDetailBean> temp = JSON.parseObject(obj.optJSONArray("data").toString(), new TypeReference<ArrayList<ParterDetailBean>>() {
                                });
                                ParterDetailBean parterDetailBean = temp.get(0);
                                captainName.setText(parterDetailBean.name);
                                if (parterDetailBean.user_info.sex.equals("0")) {
                                    sex.setBackgroundDrawable(getResources().getDrawable(R.drawable.men));
                                } else {
                                    sex.setBackgroundDrawable(getResources().getDrawable(R.drawable.women));
                                }
                                ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + parterDetailBean.user_info.avatar, head);
                                captainOccupation.setText(parterDetailBean.user_info.work_duty);
                                userLv.setText(parterDetailBean.user_info.growth_value + "");
                                workYears.setText(parterDetailBean.user_info.work_years);
                                workDuty.setText(parterDetailBean.user_info.work_title);
                                company.setText(parterDetailBean.user_info.company);
                                description.setText(parterDetailBean.user_info.description);
                                team_id = parterDetailBean.team.id;
                                user_id = parterDetailBean.team.user_id;
                                if (StringUtils.isNotEmpty(parterDetailBean.team.vr_words)) {
                                    String sp = "[]";
                                    StringTokenizer pic = new StringTokenizer(parterDetailBean.team.vr_words, sp);

                                    while (pic.hasMoreTokens())
                                        skillName.add(pic.nextToken());
                                    for (int i = 0; i < skillName.size(); i++)
                                        Log.e("skillName", skillName.get(i).toString());
                                    SkillNameAdapter skillNameAdapter = new SkillNameAdapter(skillName, PlaceOrderActivity.this);
                                    skill_girde.setAdapter(skillNameAdapter);
                                    skillNameAdapter.notifyDataSetChanged();
                                }
                                if (parterDetailBean.team.order != null) {
                                    List<ParterDetailBean.TeamBean.OrderBean> beans = new ArrayList<ParterDetailBean.TeamBean.OrderBean>();
                                    for (int i = 0; i < parterDetailBean.team.order.size(); i++) {
                                        if (parterDetailBean.team.order.get(i).order_comment != null && StringUtils.isNotEmpty(parterDetailBean.team.order.get(i).order_comment.c2p_labels)) {
                                            beans.add(parterDetailBean.team.order.get(i));
                                        }
                                    }
                                    if (beans.size() > 0) {
                                        List<ParterDetailBean.TeamBean.OrderBean> bean1 = new ArrayList<ParterDetailBean.TeamBean.OrderBean>();
                                        for (int i = beans.size() - 1; i >= 0; i--) {
                                            bean1.add(beans.get(i));
                                        }
                                        commend_item.setText("评价 （" + bean1.size() + "）");
                                        commend_item.setVisibility(View.VISIBLE);
                                        commendAdapter = new CommendAdapter(bean1, commentId, commentName, PlaceOrderActivity.this);
                                        command.setAdapter(commendAdapter);
                                        commendAdapter.notifyDataSetChanged();
                                    }


                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PlaceOrderActivity.this, "网络异常", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
