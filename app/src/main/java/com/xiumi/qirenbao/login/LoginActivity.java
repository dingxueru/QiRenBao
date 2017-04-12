package com.xiumi.qirenbao.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.mine.company.PeopleDetailActivity;
import com.xiumi.qirenbao.register.RegisterFirstActivity;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.utils.UserInfo;
import com.xiumi.qirenbao.utils.ValitateUtils;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qianbailu on 2016/01/21.
 * 登录页
 */
public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.login_btn)
    Button loginBtn;
    @Bind(R.id.login_regist)
    Button loginRregist;
    @Bind(R.id.login_forgetpsd)
    TextView forgetPsd;
    @Bind(R.id.loginActivity_phone)
    EditText phone;
    @Bind(R.id.loginActivity_password)
    EditText password;
    @Bind(R.id.login_layout)
    LinearLayout loginLayout;
    @Bind(R.id.sina)
    ImageView login_sina;
    @Bind(R.id.qq)
    ImageView login_qq;
    @Bind(R.id.weixin)
    ImageView login_weixin;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.link)
    TextView link;
    @Bind(R.id.link1)
    TextView link1;
    private ProgressDialog mProgressDialog = null;
    // 自动登录
    private UserInfo userInfo;
    private static final String USER_NAME = "user_name";
    private static final String PASSWORD = "password";
    private static final String ISSAVEPASS = "savePassWord";
    private static final String AUTOLOGIN = "autoLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setStatusBar();
        userInfo = new UserInfo(this);

        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.jmui_back_btn);
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setTitle("");
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, WebActivity.class);
                intent.putExtra("UrlFromAD", "https://qrb.shoomee.cn/userApplyInfo");
                intent.putExtra("TitleFromAD", "");
                startActivity(intent);
            }
        });
        link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, WebActivity.class);
                intent.putExtra("UrlFromAD", "https://qrb.shoomee.cn/userApplyInfo");
                intent.putExtra("TitleFromAD", "");
                startActivity(intent);
            }
        });
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android
                .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        String pathString = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/QiRenBao";//文件夹的路径
        File file = new File(pathString);
        DeleteFile(file);
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

    @OnClick(R.id.login_btn)
    public void login(View view) {
        //点击登录跳转到MainActivity
        if (valitate()) {
            keywordMiss();
            loginBtn.setClickable(false);
            login(phone.getText().toString(), password.getText().toString());
        }

    }

    public void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.login_forgetpsd)
    public void forgetPsd(View view) {
        //点击跳转到忘记密码界面
        Intent intent = new Intent(LoginActivity.this, ForgetpassActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_layout)
    public void keyMiss() {
        keywordMiss();
    }

    @OnClick(R.id.login_regist)
    public void regist(View view) {
        //点击登录跳转到注册页面
        keywordMiss();
        Intent intent = new Intent(LoginActivity.this, RegisterFirstActivity.class);
        startActivity(intent);
    }

    private boolean valitate() {
        String mobileStr = phone.getText().toString();
        String paswordStr = password.getText().toString();
        //20160426 modi by qianbailu end
        if (StringUtils.isEmpty(mobileStr)) {
            new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.v_error_empty_phone))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();

            return false;
        }
        if (!ValitateUtils.validate(mobileStr, ValitateUtils.PHONE_FORMAT)) {
            new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.v_error_format_phone))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();
            return false;
        }
        if (StringUtils.isEmpty(paswordStr)) {
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

        if (paswordStr.length() < 6) {
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
     * 第三方登录 sina 登录
     */
    @OnClick(R.id.sina)
    public void sina_login(View view) {

    }

    /**
     * 第三方登录 qq 登录
     */
    @OnClick(R.id.qq)
    public void qq_login(View view) {

    }

    /**
     * 第三方登录 wechat 登录  打包签名apk,然后才能产生微信的登录
     */
    @OnClick(R.id.weixin)
    public void wechat_login(View view) {

    }

    private void initProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.setMessage(message);

    }

    /**
     * 键盘收回
     */
    private void keywordMiss() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(phone.getWindowToken(), 0);
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
                        new AlertDialog.Builder(LoginActivity.this).setMessage("您输入的用户名或密码不正确,请查实后再输入")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        loginBtn.setClickable(true);
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("用户登录", obj.toString());
                            // 设置自动登录
                            userInfo.setUserInfo(USER_NAME, name);
                            userInfo.setUserInfo(PASSWORD, pass);
                            userInfo.setUserInfo(ISSAVEPASS, true);
                            userInfo.setUserInfo(AUTOLOGIN, true);

                            String access_token = obj.getString("access_token");
                            getUserdetail(access_token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
                            Toast.makeText(LoginActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    /**
     * 第三方登录昵称表情过滤
     */
    public static String filterEmoji(String source) {
        if (source != null) {
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                source = emojiMatcher.replaceAll("");
                return source;
            }
            return source;
        }
        return source;
    }
}
