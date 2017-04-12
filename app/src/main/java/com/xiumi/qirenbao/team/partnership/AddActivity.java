package com.xiumi.qirenbao.team.partnership;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.timepicker.TimePopupWindow;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by qianbailu on 2017/1/25.
 * [合伙]我的团队---新建活动
 */
public class AddActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.reject)
    RadioButton reject;
    @Bind(R.id.distribute)
    RadioButton disreibute;
    @Bind(R.id.activity_name)
    EditText activityName;//活动主题名字
    @Bind(R.id.activity_time)
    TextView activityTime;//活动时间
    @Bind(R.id.activity_address)
    EditText activityAddress;//活动地址
    @Bind(R.id.activity_content)
    EditText activityContent;//活动详情
    @Bind(R.id.total)
    RelativeLayout total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
        setStatusBar();
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        activityTime.setText(str);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @OnClick(R.id.reject)
    public void setReject(View view){
        keywordMiss();
        //提交活动信息
        if(valitate()){
            addActivity();
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

    @OnClick(R.id.total)
    public void setTotal(View view){
        keywordMiss();
    }
    @OnClick(R.id.distribute)
    public void setDisreibute(View view){
        //取消活动
        keywordMiss();
        activityName.setText("");
        activityAddress.setText("");
        activityContent.setText("");
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        activityTime.setText(str);
    }
    private boolean valitate() {
        //20160426 modi by qianbailu end
        if (StringUtils.isEmpty(activityName.getText().toString())) {
            new AlertDialog.Builder(this).setMessage("活动主题不能为空")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();

            return false;
        }
        if (StringUtils.isEmpty(activityTime.getText().toString())) {
            new AlertDialog.Builder(this).setMessage("请输入活动时间")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();
            return false;
        }
        if(StringUtils.isEmpty(activityAddress.getText().toString())){
            new AlertDialog.Builder(this).setMessage("请输入活动地址")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();
            return false;
        }

        if(StringUtils.isEmpty(activityContent.getText().toString())) {
            new AlertDialog.Builder(this).setMessage("请输入活动详情")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();
            return false;
        }

        return true;
    }
    @OnClick(R.id.activity_time)
    public void choseTime(View view){
        //弹出选择时间界面
        keywordMiss();
        TimePopupWindow timePopupWindow=new TimePopupWindow(AddActivity.this, TimePopupWindow.Type.ALL);
        timePopupWindow.showAtLocation(view,Gravity.BOTTOM,0,0,new Date());
        timePopupWindow.setCyclic(true);
        timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
                String str = formatter.format(date);
                activityTime.setText(str);
            }
        });
    }

    /**
     * 键盘收回
     */
    private void keywordMiss(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activityName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(activityAddress.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(activityContent.getWindowToken(), 0);
    }
    /**
     *合伙人创建活动
     */
    private void addActivity(){
        String url= "https://qrb.shoomee.cn//api/createActivity";
        OkHttpUtils
                .post()
                .addHeader("Accept","application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("team_id",MainActivity.team_id)
                .addParams("title",activityName.getText().toString())
                .addParams("start_at",activityTime.getText().toString())
                .addParams("address",activityAddress.getText().toString())
                .addParams("description",activityContent.getText().toString())
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(AddActivity.this,"网络不给力,请重试",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                             if(obj.getString("result").equals("success")){
                                 new AlertDialog.Builder(AddActivity.this).setMessage("活动创建成功,快邀请你的成员参加吧")
                                         .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                             @Override
                                             public void onClick(DialogInterface dialog, int which) {
                                                 finish();
                                             }
                                         })
                                         .setCancelable(false)
                                         .show();
                             }else{
                                 new AlertDialog.Builder(AddActivity.this).setMessage("活动时间必须大于当前时间")
                                         .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                             @Override
                                             public void onClick(DialogInterface dialog, int which) {
                                             }
                                         })
                                         .setCancelable(false)
                                         .show();
                             }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
