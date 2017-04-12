package com.xiumi.qirenbao.order;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.order.adapter.ComentAdapter;
import com.xiumi.qirenbao.order.bean.CommentBean;
import com.xiumi.qirenbao.order.company.CompanyCommentActivity;
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
 * 达人 合伙人对企业评价
 */
public class OrderCommentActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.recomend_girde)
    GridView recomendGride;
    @Bind(R.id.first)
    TextView first;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.second)
    TextView second;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.company_name)
    TextView company_name;
    @Bind(R.id.waiter)
    GridView  waiter;
    @Bind(R.id.name_comment)
    TextView nameComment;//获取的企业名称和客服名称对他进行评价
    @Bind(R.id.commit)
    Button commit;
    @Bind(R.id.head)
    CircleImageView head;
    @Bind(R.id.toolbar)
    RelativeLayout toolbar;
    private boolean isCompany=false;
    ComentAdapter comentAdapter,waiterAdapter;//对企业的评价
   private String company_user_name,tel_name,avatar,tel_service_id,order_id,company_user_id;
    private List<CommentBean> commentList=new ArrayList<CommentBean>();//对企业的评价
    private List<CommentBean> waiterList=new ArrayList<CommentBean>();//对客服的评价
    private boolean isFirst=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_comment);
        ButterKnife.bind(this);
        setStatusBar();
        //2是达人1是企业主3是合伙人
        if(MainActivity.chose==2){
            first.setTextColor(getResources().getColor(R.color.orangenormal));
            line2.setBackgroundColor(getResources().getColor(R.color.orangenormal));
            line1.setBackgroundColor(getResources().getColor(R.color.orangenormal));
            toolbar.setBackgroundColor(getResources().getColor(R.color.orangenormal));
        }else{
            first.setTextColor(getResources().getColor(R.color.title_bg));
            line2.setBackgroundColor(getResources().getColor(R.color.title_bg));
            line1.setBackgroundColor(getResources().getColor(R.color.title_bg));
            toolbar.setBackgroundColor(getResources().getColor(R.color.title_bg));
            commit.setBackgroundColor(getResources().getColor(R.color.title_bg));
        }
        company_user_name=getIntent().getStringExtra("company_user_name");
        tel_name=getIntent().getStringExtra("tel_name");
        avatar=getIntent().getStringExtra("avatar");
        tel_service_id=getIntent().getStringExtra("tel_service_id");
        if(StringUtils.isNotEmpty(tel_service_id)){
            second.setVisibility(View.VISIBLE);
            line2.setVisibility(View.INVISIBLE);
        }else{
            second.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
        }
        order_id=getIntent().getStringExtra("order_id");
        company_user_id=getIntent().getStringExtra("company_user_id");
        ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" +avatar,head);
        nameComment.setText(company_user_name);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        comentAdapter = new ComentAdapter(OrderCommentActivity.this,commentList);
        recomendGride.setAdapter(comentAdapter);
        waiterAdapter = new ComentAdapter(OrderCommentActivity.this,waiterList);
        waiter.setAdapter(waiterAdapter);
        getCommentLaber();
    }
    protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);
        //2是达人1是企业主3是合伙人
        if(MainActivity.chose==2){
            tintManager.setTintColor(getResources().getColor(R.color.orangenormal));
        }else{
            tintManager.setTintColor(getResources().getColor(R.color.title_bg));
        }

    }
    @OnClick(R.id.first)
    public void setFirst(View view){
        isFirst=true;
        company_name.setVisibility(View.VISIBLE);
        ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" +avatar,head);
        recomendGride.setVisibility(View.VISIBLE);
        nameComment.setText(company_user_name);
        waiter.setVisibility(View.GONE);
        //2是达人1是企业主3是合伙人
        if(MainActivity.chose==2){
            first.setTextColor(getResources().getColor(R.color.orangenormal));
        }else{
            first.setTextColor(getResources().getColor(R.color.title_bg));
        }
        line1.setVisibility(View.VISIBLE);
        second.setTextColor(getResources().getColor(R.color.black));
        line2.setVisibility(View.GONE);
    }
    @OnClick(R.id.second)
    public void setSecond(View view){
        company_name.setVisibility(View.GONE);
        isFirst=false;
        head.setImageDrawable(getResources().getDrawable(R.drawable.tel_service));
        recomendGride.setVisibility(View.GONE);
        nameComment.setText(tel_name);
        waiter.setVisibility(View.VISIBLE);
        first.setTextColor(getResources().getColor(R.color.black));
        line1.setVisibility(View.GONE);
        //2是达人1是企业主3是合伙人
        if(MainActivity.chose==2){
            second.setTextColor(getResources().getColor(R.color.orangenormal));
        }else{
            second.setTextColor(getResources().getColor(R.color.title_bg));
        }
        line2.setVisibility(View.VISIBLE);
          }
    @OnClick(R.id.commit)
    public void setCommit(View view){
        //点击评价
        if(isFirst){
            setComment();
        }else{
            setWaiterComment();
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
                        Toast.makeText(OrderCommentActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("获取评价标签",obj.toString());
                            List<CommentBean> temp = JSON.parseArray(obj.getJSONArray("data").toString(), CommentBean.class);
                            List<CommentBean> commList=new ArrayList<CommentBean>();
                            List<CommentBean> waitList=new ArrayList<CommentBean>();
                            List<CommentBean> teamList=new ArrayList<CommentBean>();
                            for(int i=0;i<temp.size();i++){
                                if(temp.get(i).type.equals("1")){
                                    //对企业主评价
                                    commList.add(temp.get(i));
                                }else  if(temp.get(i).type.equals("2")){
                                    //对团队的评价
                                    teamList.add(temp.get(i));
                                }else  if(temp.get(i).type.equals("3")){
                                    //对客服的评价
                                    waitList.add(temp.get(i));
                                }
                            }
                            waiterList.addAll(waitList);
                            commentList.addAll(commList);
                            waiterAdapter.notifyDataSetChanged();
                            comentAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(OrderCommentActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    /**
     * 提交客服评价内容
     */
    private void setWaiterComment(){
        String url= "https://qrb.shoomee.cn/api/s2tComment";
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
                        Toast.makeText(OrderCommentActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(isCompany==true){
                                new AlertDialog.Builder(OrderCommentActivity.this).setMessage("评价已完成")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }else {
                                new AlertDialog.Builder(OrderCommentActivity.this).setMessage("客服评价已完成，只有对合伙人和客服都评价了这单才算完成哦~~")
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
                            Toast.makeText(OrderCommentActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    /**
     * 提交企业评价内容
     */
    private void setComment(){
        Log.e("ComentAdapter.SeleckStr",ComentAdapter.SeleckStr);
        String url= "https://qrb.shoomee.cn/api/s2cComment";
        OkHttpUtils
                .post()
                .addHeader("Accept","application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("order_id",order_id)
                .addParams("company_user_id",company_user_id)
                .addParams("labels",ComentAdapter.SeleckStr)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(OrderCommentActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("提交企业评价内容",obj.toString());
                            if(StringUtils.isNotEmpty(tel_service_id)){
                                if(isCompany==true){
                                    new AlertDialog.Builder(OrderCommentActivity.this).setMessage("评价已完成")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .setCancelable(false)
                                            .show();
                                }else {
                                    new AlertDialog.Builder(OrderCommentActivity.this).setMessage("企业评价已完成，别忘了给客服也评价哦，这样订单才算完成~~")
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
                                new AlertDialog.Builder(OrderCommentActivity.this).setMessage("合伙人评价完成")
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
                            Toast.makeText(OrderCommentActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
