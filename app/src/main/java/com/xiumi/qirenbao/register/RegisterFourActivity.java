package com.xiumi.qirenbao.register;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.login.LoginActivity;
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

public class RegisterFourActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.commit)
    Button commit;
    @Bind(R.id.password)
    EditText pasword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_four);
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

    @OnClick(R.id.commit)
    public void commitPage(View view) {
        if (valitate()) {
            keywordMiss();
            //校验成功登陆,登陆成功跳到主页面
            register();
        }
    }

    @OnClick(R.id.regist)
    public void setRegist() {
        keywordMiss();
    }

    /**
     * 校验信息
     */
    private boolean valitate() {
        String paswordStr = pasword.getText().toString();

        if (paswordStr.length() < 6 || paswordStr.length() > 8) {
            Toast.makeText(this, "密码必须在6到8位之间", Toast.LENGTH_SHORT);
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
    private void keywordMiss() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pasword.getWindowToken(), 0);
    }

    /**
     * App用户注册
     */
    private void register() {
        int chose = getIntent().getIntExtra("chose", 0);
        String name = getIntent().getStringExtra("name");
        String code = getIntent().getStringExtra("code");
        final String mobile = getIntent().getStringExtra("mobile");
        String url = "https://qrb.shoomee.cn/qrb_api/reg";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("mobile", mobile)
                .addParams("email", mobile + "@163.com")
                .addParams("password", pasword.getText().toString())
                .addParams("reg_type", chose + "")
                .addParams("login_type", chose + "")
                .addParams("name", name)
                .addParams("verify_code", getIntent().getStringExtra("verify_code"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(RegisterFourActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            Log.e("register", obj.toString());
                            if (result.equals("success")) {
                                login(mobile, pasword.getText().toString());
                            } else {
                                Toast.makeText(RegisterFourActivity.this, "该手机号已被注册过，请更换手机号", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterFourActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    /**
     * 用户登录
     */
    private void login(String name, String pass) {

        String url = "https://qrb.shoomee.cn//oauth/token";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("grant_type", "password")
                .addParams("client_id", "2")
                .addParams("client_secret", "2paijmElt4VL01Flrcq7hj63f9GwTV62oS1gp9FL")
                .addParams("username", name)
                .addParams("password", pass)
                .addParams("scope", "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(RegisterFourActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String access_token = obj.getString("access_token");
                            getUserdetail(access_token);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterFourActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    /**
     * 获取用户信息
     */
    private void getUserdetail(final String access_token) {
        String url = "https://qrb.shoomee.cn/api/getInitInfo";
        Log.e("Bearer+access_token", "Bearer " + access_token);
        OkHttpUtils
                .get()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + access_token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(RegisterFourActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("获取用户信息", obj.toString());
                            JSONObject data = obj.optJSONObject("data");
                            int login_type = data.getInt("login_type");
                            String id = data.getString("id");
                            String name = data.optString("name");
                            String team_id = null;
                            String avatar = null;
                            String growth_value = "0";
                            String sex = null;
                            String role = null;
                            String work_duty = null;
                            String work_years = null;
                            String work_title = null;
                            String company = null;
                            String class_id = null;
                            String description = null;
                            JSONObject json = data.optJSONObject("in_team");
                            if (json != null) {
                                team_id = String.valueOf(json.optInt("team_id"));
                            }
                            JSONObject object = data.optJSONObject("user_info");
                            if (null != obj) {
                                if (!TextUtils.isEmpty(object.optString("avatar"))) {
                                    avatar = object.optString("avatar");
                                }
                                growth_value = object.optString("growth_value");
                                sex = object.optString("sex");
                                role = object.optString("role");
                                work_duty = object.optString("work_duty");
                                class_id = object.optString("class_id");
                                work_years = object.optString("work_years");
                                work_title = object.optString("work_title");
                                company = object.optString("company");
                                description = object.optString("description");
                            }
                            Log.e("chose", login_type + "");
                            Intent intent = new Intent(RegisterFourActivity.this, MainActivity.class);
                            intent.putExtra("chose", login_type);
                            intent.putExtra("access_token", access_token);
                            intent.putExtra("id", id);
                            intent.putExtra("name", name);
                            intent.putExtra("team_id", team_id);
                            intent.putExtra("avatar", avatar);
                            intent.putExtra("growth_value", growth_value);
                            intent.putExtra("sex", sex);
                            intent.putExtra("class_id", class_id);
                            intent.putExtra("work_duty", work_duty);
                            intent.putExtra("company", company);
                            intent.putExtra("work_title", work_title);
                            intent.putExtra("work_years", work_years);
                            intent.putExtra("role", role);
                            intent.putExtra("description", description);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterFourActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
