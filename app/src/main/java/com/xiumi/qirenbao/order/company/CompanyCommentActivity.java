package com.xiumi.qirenbao.order.company;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.order.OrderCommentActivity;
import com.xiumi.qirenbao.order.adapter.ComentAdapter;
import com.xiumi.qirenbao.order.bean.CommentBean;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.widget.CircleImageView;
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
 * Created by qianbailu on 2017/3/16.
 * 企业对达人合伙评价
 */
public class CompanyCommentActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.team_girde)
    GridView teamGirde;
    @Bind(R.id.first)
    TextView first;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.second)
    TextView second;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.waiter)
    GridView  waiter;
    @Bind(R.id.name_comment)
    TextView nameComment;
    @Bind(R.id.commit)
    Button commit;
    @Bind(R.id.export_name)
    TextView export_name;
    @Bind(R.id.parter_name)
    TextView parter_name;
    @Bind(R.id.head)
    CircleImageView head;
    private boolean isCompany=false;
    ComentAdapter teamAdapter,waiterAdapter;//对企业的评价
    private String name,waiterName,order_id,service_user_id,avatar,tel_service_id,partner_user_id;
    private boolean isFirst=true;
    private List<CommentBean> waiterList=new ArrayList<CommentBean>();//对客服的评价
    private List<CommentBean> teamBean=new ArrayList<CommentBean>();//对客服的评价
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_comment_layout);
        ButterKnife.bind(this);
        setStatusBar();
        name=getIntent().getStringExtra("name");
        waiterName=getIntent().getStringExtra("waiter");
        order_id=getIntent().getStringExtra("order_id");
        service_user_id=getIntent().getStringExtra("service_user_id");
        tel_service_id=getIntent().getStringExtra("tel_service_id");
        partner_user_id=getIntent().getStringExtra("partner_user_id");
        if(service_user_id.equals(partner_user_id)){
            parter_name.setVisibility(View.VISIBLE);
            export_name.setVisibility(View.GONE);
        }else{
            parter_name.setVisibility(View.GONE);
            export_name.setVisibility(View.VISIBLE);
        }
        avatar=getIntent().getStringExtra("avatar");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" +avatar,head);
        if(StringUtils.isNotEmpty(tel_service_id)){
            second.setVisibility(View.VISIBLE);
            line2.setVisibility(View.INVISIBLE);
        }else{
            second.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
        }
        teamAdapter = new ComentAdapter(CompanyCommentActivity.this,teamBean);
        teamGirde.setAdapter(teamAdapter);
        waiterAdapter = new ComentAdapter(CompanyCommentActivity.this,waiterList);
        waiter.setAdapter(waiterAdapter);
        getCommentLaber();
        nameComment.setText(name);
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
    public void setFirst(View view){
        isFirst=true;
        if(service_user_id.equals("service_user_id")){
            parter_name.setVisibility(View.VISIBLE);
            export_name.setVisibility(View.GONE);
        }else{
            parter_name.setVisibility(View.GONE);
            export_name.setVisibility(View.VISIBLE);
        }
        ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" +avatar,head);
        nameComment.setText(name);
        teamGirde.setVisibility(View.VISIBLE);
        waiter.setVisibility(View.GONE);
        first.setTextColor(getResources().getColor(R.color.title_bg));
        line1.setVisibility(View.VISIBLE);
        second.setTextColor(getResources().getColor(R.color.black));
        line2.setVisibility(View.GONE);
    }
    @OnClick(R.id.second)
    public void setSecond(View view){
        isFirst=false;
        export_name.setVisibility(View.GONE);
        parter_name.setVisibility(View.GONE);
        nameComment.setText(waiterName);
        teamGirde.setVisibility(View.GONE);
        head.setImageDrawable(getResources().getDrawable(R.drawable.tel_service));
        waiter.setVisibility(View.VISIBLE);
        first.setTextColor(getResources().getColor(R.color.black));
        line1.setVisibility(View.GONE);
        second.setTextColor(getResources().getColor(R.color.title_bg));
        line2.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.commit)
    public void setCommit(View view){
        Log.e("ComentAdapter.SeleckStr",ComentAdapter.SeleckStr);
        if(StringUtils.isNotEmpty(ComentAdapter.SeleckStr)){
            if(isFirst){
                setComment();
            }else{
                setWaiterComment();
            }
        }
    }
    /**
     * 获取评价标签
     */
    private void getCommentLaber(){
        String url= "https://qrb.shoomee.cn/qrb_api/getCommentTags";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(CompanyCommentActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("获取评价标签",obj.toString());
                            List<CommentBean> temp = JSON.parseArray(obj.getJSONArray("data").toString(), CommentBean.class);
                            List<CommentBean> waitList=new ArrayList<CommentBean>();
                            List<CommentBean> teamList=new ArrayList<CommentBean>();
                            for(int i=0;i<temp.size();i++){
                                if(temp.get(i).type.equals("2")){
                                    //对团队的评价
                                    teamList.add(temp.get(i));
                                }else  if(temp.get(i).type.equals("3")){
                                    //对客服的评价
                                    waitList.add(temp.get(i));
                                }
                            }
                            waiterList.addAll(waitList);
                            teamBean.addAll(teamList);
                            waiterAdapter.notifyDataSetChanged();
                            teamAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CompanyCommentActivity.this,"网络异常",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    /**
     * 提交合伙人评价内容
     */
    private void setComment(){

        String url= "https://qrb.shoomee.cn/api/c2pComment";
        OkHttpUtils
                .post()
                .addHeader("Accept","application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("order_id",order_id)
                .addParams("service_user_id",service_user_id)
                .addParams("labels",ComentAdapter.SeleckStr)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(CompanyCommentActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("获取评价标签",obj.toString());
                            if(StringUtils.isNotEmpty(tel_service_id)){
                                if(isCompany==true){
                                    new AlertDialog.Builder(CompanyCommentActivity.this).setMessage("评价已完成")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .setCancelable(false)
                                            .show();
                                }else {
                                    new AlertDialog.Builder(CompanyCommentActivity.this).setMessage("合伙人评价完成，别忘了也给客服评价哦，这样订单才算完成~~")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    isCompany = true;
                                                }
                                            })
                                            .setCancelable(false)
                                            .show();
                                }

                            }else{
                                new AlertDialog.Builder(CompanyCommentActivity.this).setMessage("合伙人评价完成")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CompanyCommentActivity.this,"网络异常",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    /**
     * 提交客服评价内容
     */
    private void setWaiterComment(){

        String url= "https://qrb.shoomee.cn/api/c2tComment";
        OkHttpUtils
                .post()
                .addHeader("Accept","application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("order_id",order_id)
                .addParams("tel_service_id",tel_service_id)
                .addParams("labels",ComentAdapter.SeleckStr)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(CompanyCommentActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(isCompany==true){
                                new AlertDialog.Builder(CompanyCommentActivity.this).setMessage("评价已完成")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }else {
                                new AlertDialog.Builder(CompanyCommentActivity.this).setMessage("客服评价已完成，只有对合伙人和客服都评价了这单才算完成哦~~")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                isCompany = true;
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                         } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CompanyCommentActivity.this,"网络异常",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
