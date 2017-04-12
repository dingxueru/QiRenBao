package com.xiumi.qirenbao.myWallet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.myWallet.bean.ChangeBean;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.utils.ValitateUtils;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by qianbailu on 2017/3/18.
 * 钱包提现
 */
public class WithdrawActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.total)
    LinearLayout total;
    @Bind(R.id.cash)
    EditText cash;
    @Bind(R.id.account)
    EditText account;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.commit)
    Button commit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ButterKnife.bind(this);
        setStatusBar();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordMiss();
            }
        });
    }
    @OnClick(R.id.commit)
    public void setCommit(View view){
        keywordMiss();
        if(valitate()){
            setWithdraw();
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
     * 键盘收回
     */
    private void keywordMiss(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cash.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(account.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
    }
    private boolean valitate() {
        String cashStr=cash.getText().toString();
        String accountStr=account.getText().toString();
        String nameStr=name.getText().toString();
        if (StringUtils.isEmpty(cashStr)) {
            Toast.makeText(this,"提现金额不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StringUtils.isEmpty(accountStr)){
            Toast.makeText(this,"支付宝账号不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!ValitateUtils.validate(accountStr, ValitateUtils.PHONE_FORMAT)&&!ValitateUtils.validate(accountStr, ValitateUtils.EMAIL_FORMAT)){
            Toast.makeText(this,"请输入正确的支付宝账号！",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StringUtils.isEmpty(nameStr)){
            Toast.makeText(this,"支付宝账号名不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(nameStr.length()>5){
            Toast.makeText(this,"请输入真实用户名！",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    /**
     * 钱包提现
     */
    private void setWithdraw(){
        String url= "https://qrb.shoomee.cn/api/withdrawMoney";
        OkHttpUtils
                .post()
                .addHeader("Accept","application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("total_fee",cash.getText().toString())
                .addParams("ali_id",account.getText().toString())
                .addParams("ali_true_name",name.getText().toString())
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(WithdrawActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        Log.e("网络异常",e.toString());
                    }
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("钱包提现",obj.toString());
                            String result = obj.optString("result");
                            if(result.equals("success")){
                                new AlertDialog.Builder(WithdrawActivity.this).setMessage("提现操作已成功，正在处理中...")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent=new Intent(WithdrawActivity.this,MyWalletActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }else{
                                new AlertDialog.Builder(WithdrawActivity.this).setMessage("提现失败，请稍后重试")
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
                            Toast.makeText(WithdrawActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
