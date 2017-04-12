package com.xiumi.qirenbao.location;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.location.adapter.LocationAdapter;
import com.xiumi.qirenbao.location.bean.AreasBean;
import com.xiumi.qirenbao.location.bean.LocationBean;
import com.xiumi.qirenbao.login.LoginActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qianbailu on 2017/2/27.
 */
public class LocationActivity extends Activity{

    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.location_girde)
    GridView locationGirde;
    @Bind(R.id.city_name)
    TextView cityName;
     private  List<AreasBean> areasBeanList=new ArrayList<>();
    private LocationAdapter locationAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        locationAdapter = new LocationAdapter(areasBeanList, LocationActivity.this);
        locationGirde.setAdapter(locationAdapter);
        getCityNames();
    }
    /**
     * 获取推荐分类
     */
    private void getCityNames(){
        String url= "https://qrb.shoomee.cn//qrb_api/getCities";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(LocationActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray data= obj.getJSONArray("data");
                            List<LocationBean> temp = JSON.parseArray(obj.getJSONArray("data").toString(), LocationBean.class);
                            LocationBean locationBean=temp.get(0);
                            cityName.setText(locationBean.name);
                            List<AreasBean> areas=locationBean.areas;
                            areasBeanList.addAll(areas);
                            locationAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LocationActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}