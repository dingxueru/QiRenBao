package com.xiumi.qirenbao.message.expert;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 ：Created by DXR on 2017/3/13.
 * 消息详情
 */

public class MessageDetailActivity extends Activity{
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.content)
    TextView content;
    // 消息 id
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_detail_layout);
        ButterKnife.bind(this);
        setStatusBar();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            id = bundle.getInt("id");
            Log.v("message_id",id + "");
            /**
             * 获取消息详情
             */
            ReadMessage(id);
        }

    }
    /**
     * 获取用户的消息详情
     */
    private void ReadMessage(int id){
        String url= "https://qrb.shoomee.cn//api/readMessage";
        OkHttpUtils
                .post()
                .addHeader("Accept","application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("id",id + "")
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MessageDetailActivity.this,"网络不给力,请重试",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("用户信息详情",obj.toString());
                            if (obj.optString("result").equals("success")) {
                                JSONArray jsonArray = obj.optJSONArray("data");
                                JSONObject json = jsonArray.getJSONObject(0);

                                title.setText(json.optString("title"));
                                time.setText(json.optString("created_at"));
                                content.setText(json.optString("content"));
                            }  else {
                                Toast.makeText(MessageDetailActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MessageDetailActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
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

        tintManager.setTintColor(getResources().getColor(R.color.orangenormal));
    }
}
