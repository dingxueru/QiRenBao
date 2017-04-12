package com.xiumi.qirenbao.guide;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.login.LoginActivity;
import com.xiumi.qirenbao.utils.UserInfo;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 引导页--跳转到login页
 * 2017-02-20 add by qianbailu
 */
public class GuideActivity extends Activity {
    @Bind(R.id.guide_pic)
    ImageView guidePic;
    private UserInfo userInfo;
    private static final String USER_NAME = "user_name";
    private static final String PASSWORD = "password";
    private static final String AUTOLOGIN = "autoLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        setStatusBar();
        userInfo = new UserInfo(this);

        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 判断是否要自动登录
                if (userInfo.getBooleanInfo(AUTOLOGIN)) {
                    login(userInfo.getStringInfo(USER_NAME), userInfo.getStringInfo(PASSWORD));
                } else {
                    Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.sendEmptyMessageDelayed(0, 2000);
    }

    protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(getResources().getColor(R.color.white));
    }

    /**
     * 用户登录
     */
    private void login(final String name, final String pass) {

        String url = "https://qrb.shoomee.cn/oauth/token";
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
                        Toast.makeText(GuideActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("用户登录", obj.toString());

                            String access_token = obj.getString("access_token");
                            getUserdetail(access_token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GuideActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(GuideActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
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
                            String user_lv = null;
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
                                user_lv = object.optString("user_lv");
                            }
                            Log.e("chose", login_type + "");
                            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
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
                            intent.putExtra("user_lv", user_lv);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GuideActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
