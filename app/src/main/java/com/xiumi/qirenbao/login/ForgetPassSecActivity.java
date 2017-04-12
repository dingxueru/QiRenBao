package com.xiumi.qirenbao.login;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPassSecActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.btn_code)
    TextView btnCode;
    @Bind(R.id.register_code)
    EditText code;
    @Bind(R.id.forgetpsd)
    LinearLayout forgetpsd;
    private int second = 60;
    private String mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_sec);
        ButterKnife.bind(this);
        mobile=getIntent().getStringExtra("mobile");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setStatusBar();
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
    @OnClick(R.id.forgetpsd)
    public void setForgetpsd(View view){
        keywordMiss();
    }
    @OnClick(R.id.btn_code)
    public void getCode(TextView btn_code) {
        getValCode();//获取手机验证码
        btn_code.setClickable(false);
        btn_code.setText(getResources().getString(R.string.sending_code));
    }
    @OnClick(R.id.next)
    public void nextPage(View view){
        if(StringUtils.isNotEmpty(code.getText().toString())){
            keywordMiss();
            validateValCode(code.getText().toString());
        }else{
            new AlertDialog.Builder(this).setMessage("验证码不能为空")
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
     * 键盘收回
     */
    private void keywordMiss(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(code.getWindowToken(), 0);
    }
    private Handler timeHandler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void handleMessage(Message msg) {
            try {
                if (second > 0) {
                    btnCode.setText(String.format("重新发送(%s)", second--));
                    timeHandler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    btnCode.setText("获取验证码");
                    btnCode.setClickable(true);
                    second = 60;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    /**
     * 发送验证码60秒倒计时
     */
    private void startTiming() {
        timeHandler.sendEmptyMessage(0);
    }
    /**
     * 获取手机验证码接口
     */
    private void getValCode(){
        String url= "https://qrb.shoomee.cn/qrb_api/seedSmsFP";
        Log.e("mobile",mobile);
        OkHttpUtils
                .post()
                .url(url)
                .addParams("mobile",mobile)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("request.toString()",request.toString());
                        Toast.makeText(ForgetPassSecActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String result=obj.getString("result");
                            if(!result.equals("callback")){
                                new AlertDialog.Builder(ForgetPassSecActivity.this).setMessage("请输入你注册过的手机号")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                            startTiming();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ForgetPassSecActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();
                            btnCode.setText("获取验证码");
                            btnCode.setClickable(true);
                        }
                    }
                });

    }
    /**
     * 验证手机验证码
     */
    private void validateValCode(String mob){
        String url= "https://qrb.shoomee.cn//qrb_api/checkSms?mobile="+mobile+"&verify_code="+mob;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(ForgetPassSecActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String result=obj.getString("result");
                            if(result.equals("success")){
                                Intent intent = new Intent(ForgetPassSecActivity.this,ForgetPassThirdActivity.class);
                                intent.putExtra("mobile",mobile);
                                intent.putExtra("code",code.getText().toString());
                                startActivity(intent);
                            }else{
                                new AlertDialog.Builder(ForgetPassSecActivity.this).setMessage("请检测你的验证码是否正确")
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
                            Toast.makeText(ForgetPassSecActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
