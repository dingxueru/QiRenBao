package com.xiumi.qirenbao.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class ForgetPassThirdActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.et_password)
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_third);
        ButterKnife.bind(this);
        setStatusBar();
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
    @OnClick(R.id.next)
    public void nextPage(View view){
        keywordMiss();
        if(valitate()){
            forgetPass();

        }
    }
    private boolean valitate() {
        String paswordStr=password.getText().toString();
        //20160426 modi by qianbailu end
        if(StringUtils.isEmpty(paswordStr)){
            new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.v_error_empty_password))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();
            return false;
        }

        if(paswordStr.length()<6) {
            new AlertDialog.Builder(this).setMessage("密码必须在6到8位之间")
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
    /**
     * 键盘收回
     */
    private void keywordMiss(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
    }
    /**
     *  忘记密码
     */
    private void forgetPass(){
       String  mobile=getIntent().getStringExtra("mobile");
        String code=getIntent().getStringExtra("code");
        String url= "https://qrb.shoomee.cn/qrb_api/forgetPassword";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("new_password",password.getText().toString())
                .addParams("mobile",mobile)
                .addParams("verify_code",code)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                       Toast.makeText(ForgetPassThirdActivity.this,"请检查你的验证码是否正确",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            int result=obj.getInt("result");
                            Log.e("",obj.toString());
                            if(result==1){
                                new AlertDialog.Builder(ForgetPassThirdActivity.this).setMessage("密码修改成功")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(ForgetPassThirdActivity.this,LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }else{
                                new AlertDialog.Builder(ForgetPassThirdActivity.this).setMessage("密码修改失败，请重试")
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
                            Toast.makeText(ForgetPassThirdActivity.this,"请检查你的验证码是否正确",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
