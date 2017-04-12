package com.xiumi.qirenbao.officebuilding;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
 * Created by qianbailu on 2017/2/3.
 * 写字楼列表详情页按钮2
 */
public class DetailButTowActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.subscribe)
    TextView subscribe;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.total)
    RelativeLayout total;
    @Bind(R.id.close)
    TextView close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_but_tow);
        ButterKnife.bind(this);
        if (toolbar != null){
            toolbar.setNavigationIcon(R.drawable.jmui_back_btn);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setTitle("");
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordMiss();
            }
        });
        setStatusBar();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @OnClick(R.id.subscribe)
    public void setSubscribe(View view){
        //提交预约
        if(valitate()) {
            keywordMiss();
            setSubscribeBuild();
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
        imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(phone.getWindowToken(), 0);
    }
    /**
     * 提交写字楼预约
     */
    private void setSubscribeBuild(){
        Log.e("getIntent()",getIntent().getStringExtra("id"));
        String url= "https://qrb.shoomee.cn/qrb_api/createBuildOrder";
        OkHttpUtils
                .post()
                .addParams("building_id",getIntent().getStringExtra("id"))
                .addParams("order_name",name.getText().toString())
                .addParams("order_phone",phone.getText().toString())
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(DetailButTowActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        Log.e("网络异常",e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("获取评价标签",obj.toString());
                            new AlertDialog.Builder(DetailButTowActivity.this).setMessage("预约成功，稍后会有客服联系你~~")
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
                            Toast.makeText(DetailButTowActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    private boolean valitate() {
        String mobileStr=phone.getText().toString();
        String nameStr=name.getText().toString();
        //20160426 modi by qianbailu end
        if (StringUtils.isEmpty(mobileStr)) {
            Toast.makeText(DetailButTowActivity.this,"联系的手机号不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!ValitateUtils.validate(mobileStr, ValitateUtils.PHONE_FORMAT)) {
            Toast.makeText(DetailButTowActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();

            return false;
        }
        if(StringUtils.isEmpty(nameStr)){
            Toast.makeText(DetailButTowActivity.this,"请输入你的称谓",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
