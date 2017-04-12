package com.xiumi.qirenbao.register;

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

/**
 * Created by qianbailu on 2016/01/21.
 * 注册页
 */

public class RegisterThirdActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.btn_code)
    TextView btnCode;
    @Bind(R.id.register_code)
    EditText code;
    @Bind(R.id.regist)
    LinearLayout regist;
    private int second = 60;
    private String mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_third);
        ButterKnife.bind(this);
        setStatusBar();
        mobile=getIntent().getStringExtra("mobile");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        tintManager.setTintColor(getResources().getColor(R.color.title_bg));
    }
    @OnClick(R.id.regist)
    public  void setRegist(){
        keywordMiss();
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
    @OnClick(R.id.btn_code)
    public void getCode(TextView btn_code) {
            getValCode();//获取手机验证码
            btn_code.setClickable(false);
            btn_code.setText(getResources().getString(R.string.sending_code));
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
        String url= "https://qrb.shoomee.cn//qrb_api/seedSms";
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
                        Toast.makeText(RegisterThirdActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            startTiming();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterThirdActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();
                            btnCode.setText("获取验证码");
                            btnCode.setClickable(true);
                        }
                    }
                });

    }
    /**
     * 验证手机验证码
     */
    private void validateValCode(final String mob){
        String url= "https://qrb.shoomee.cn//qrb_api/checkSms?mobile="+mobile+"&verify_code="+mob;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(RegisterThirdActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String result=obj.getString("result");
                            if(result.equals("success")){
                                int chose=getIntent().getIntExtra("chose",0);
                                String name=getIntent().getStringExtra("name");
                                Intent intent = new Intent(RegisterThirdActivity.this,RegisterFourActivity.class);
                                intent.putExtra("mobile",mobile);
                                intent.putExtra("name",name);
                                intent.putExtra("chose",chose);
                                intent.putExtra("verify_code",mob);
                                intent.putExtra("code",code.getText().toString());
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterThirdActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
