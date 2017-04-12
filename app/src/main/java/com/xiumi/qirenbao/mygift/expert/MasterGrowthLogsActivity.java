package com.xiumi.qirenbao.mygift.expert;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.mygift.adapter.GrowthLogAdapter;
import com.xiumi.qirenbao.mygift.bean.GrowthLogBean;
import com.xiumi.qirenbao.widget.CustomerListView;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者 ：Created by DXR on 2017/3/21.
 * 达人-成长记录
 */

public class MasterGrowthLogsActivity extends Activity{

    @Bind(R.id.growth_list)
    CustomerListView growth_list;
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.total_growth)
    TextView total_growth;

    private GrowthLogAdapter growthLogAdapter = null;
    private ArrayList<GrowthLogBean> growthlist = new ArrayList<GrowthLogBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_growth_logs_layout);
        ButterKnife.bind(this);
        setStatusBar();
        total_growth.setText(MainActivity.growth_value);
        // 获取成长经验值
        GrowthLogs();

        // adapter 初始化
        growthLogAdapter = new GrowthLogAdapter(MasterGrowthLogsActivity.this,growthlist);
        growth_list.setAdapter(growthLogAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        tintManager.setTintColor(getResources().getColor(R.color.orangenormal));
    }

    /**
     * 达人成长值记录
     */
    private void GrowthLogs () {
        String url = "https://qrb.shoomee.cn/api/growthLogs";
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(MasterGrowthLogsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.e("达人成长值记录", object.toString());
                            if (object.getString("result").equals("success")) {
                                JSONObject json = object.optJSONObject("data");
                                JSONArray jsonArray = json.optJSONArray("data");
                                ArrayList<GrowthLogBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<GrowthLogBean>>() {
                                });
                                if (temp.size() > 0) {

                                    growthlist.addAll(temp);
                                    growthLogAdapter.notifyDataSetChanged();

                                } else if (temp.size() == 0) {

                                }
                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MasterGrowthLogsActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }
}
