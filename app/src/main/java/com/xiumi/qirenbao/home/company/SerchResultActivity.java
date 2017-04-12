package com.xiumi.qirenbao.home.company;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.home.adapter.ChoseTeamAdapter;
import com.xiumi.qirenbao.home.bean.ChoseTeamBean;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SerchResultActivity extends AppCompatActivity {
/*
   搜索结果页面
   201700302 add by qianbailu
 */
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.base_list)
    ListView  teamList;
    @Bind(R.id.no_content)
    LinearLayout no_content;
    @Bind(R.id.name_title)
    TextView name_title;
    private ChoseTeamAdapter choseTeamAdapter;
    private ArrayList<ChoseTeamBean> choseTeamBeen = new ArrayList<ChoseTeamBean>();
    public static SerchResultActivity SERCH = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serch_result);
        ButterKnife.bind(this);
        setStatusBar();
        SERCH=this;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String keyword=getIntent().getStringExtra("keyword");
        name_title.setText(keyword);
        choseTeamAdapter = new ChoseTeamAdapter(choseTeamBeen,SerchResultActivity.this);
        teamList.setAdapter(choseTeamAdapter);
        getpeopleSerch(keyword);
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
     * 合伙人搜索
     */
    private void getpeopleSerch(final String keyword){
        String url= "https://qrb.shoomee.cn/qrb_api/searchPartner";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("word",keyword)
                .addParams("origin","Android")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(SerchResultActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("合伙人搜索",obj.toString());
                            String result=obj.getString("result");
                            if(result.equals("success")){
                                Log.e("data",obj.optJSONArray("data").toString());
                                List<ChoseTeamBean> temp  = JSON.parseObject(obj.optJSONArray("data").toString(), new TypeReference<ArrayList<ChoseTeamBean>>() {});
                                Log.v("1117-temp", temp.size()+"");
                                if (temp.size() > 0) {
                                    choseTeamBeen.addAll(temp);
                                    Log.v("1117-temp", choseTeamBeen.size()+"");
                                    choseTeamAdapter.notifyDataSetChanged();
                                }
                            }else{
                                teamList.setVisibility(View.GONE);
                                no_content.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SerchResultActivity.this,"网络异常",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
