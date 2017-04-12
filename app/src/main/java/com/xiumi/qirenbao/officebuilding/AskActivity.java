package com.xiumi.qirenbao.officebuilding;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.utils.ValitateUtils;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qianbailu on 2017/3/27.
 * 添加提问
 */
public class AskActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.description)
    EditText description;
    @Bind(R.id.commit)
    TextView commit;
    @Bind(R.id.total)
    LinearLayout total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        ButterKnife.bind(this);
        setStatusBar();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @OnClick(R.id.total)
    public void setOntotal(View view){
        keywordMiss();
    }
    @OnClick(R.id.commit)
    public void setCommit(View view){
        keywordMiss();
        if(valitate()){
            setAsk();
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
    private boolean valitate() {
        String nameStr = name.getText().toString();
        String phoneStr = phone.getText().toString();
        String descriptionStr=description.getText().toString();
        //20160426 modi by qianbailu end
        if (StringUtils.isEmpty(nameStr)) {
            Toast.makeText(AskActivity.this,"称谓不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isEmpty(phoneStr)) {
            Toast.makeText(AskActivity.this,"联系方式不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (nameStr.length() > 50) {
            Toast.makeText(AskActivity.this,"联系方式不得超过50个字符",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StringUtils.isEmpty(descriptionStr)){
            Toast.makeText(AskActivity.this,"问题描述不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(descriptionStr.length()>500){
            Toast.makeText(AskActivity.this,"问题描述不得超过500个字符",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    /**
     * 提交意见
     */
    private void setAsk(){
        String url= "https://qrb.shoomee.cn/qrb_api/createQuestion";
        OkHttpUtils
                .post()
                .addParams("name",name.getText().toString())
                .addParams("contact",phone.getText().toString())
                .addParams("description",description.getText().toString())
                .addParams("device","android")
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(AskActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        Log.e("网络异常",e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("获取评价标签",obj.toString());
                            new android.app.AlertDialog.Builder(AskActivity.this).setMessage("问题提交成功，稍后会有客服联系你~~")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AskActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    /**
     * 键盘收回
     */
    private void keywordMiss() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(description.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(phone.getWindowToken(), 0);
    }
}
