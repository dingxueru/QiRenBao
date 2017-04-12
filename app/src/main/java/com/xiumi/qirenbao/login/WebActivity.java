package com.xiumi.qirenbao.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.widget.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qianbailu on 2016/01/21.
 * 用户协议
 */
public class WebActivity extends AppCompatActivity {
    @Bind(R.id.ad_webview)
    WebView mWebView;
    @Bind(R.id.back)
    TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        String Url = getIntent().getStringExtra("UrlFromAD");
        String Title = getIntent().getStringExtra("TitleFromAD");
        setTitle(Title);
        setStatusBar();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(Url);
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
}
