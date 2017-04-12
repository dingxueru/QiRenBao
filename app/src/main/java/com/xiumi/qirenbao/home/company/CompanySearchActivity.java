package com.xiumi.qirenbao.home.company;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.home.adapter.CommendHistoryAdapter;
import com.xiumi.qirenbao.home.adapter.HistoryAdapter;
import com.xiumi.qirenbao.home.bean.HistoryBean;
import com.xiumi.qirenbao.home.bean.RecommendSerchBean;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qianbailu on 2017/2/3.
 * 企业搜索
 */
public class CompanySearchActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.recomend_girde)
    GridView recomendGride;
    @Bind(R.id.history_girde)
    GridView historyGride;
    @Bind(R.id.tab_search_search)
    EditText search;
    @Bind(R.id.tab_search_)
    ImageView img;
    @Bind(R.id.total)
    LinearLayout total;
    public static CompanySearchActivity COMPANY = null;
    private CommendHistoryAdapter commendHistoryAdapter;//推荐
    private List<RecommendSerchBean> serchBeanList=new ArrayList<>();
    private List<HistoryBean> historyBeanList=new ArrayList<>();
    private HistoryAdapter historyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_search);
        ButterKnife.bind(this);
        setStatusBar();
        COMPANY=this;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        commendHistoryAdapter = new CommendHistoryAdapter(serchBeanList,CompanySearchActivity.this);
        recomendGride.setAdapter(commendHistoryAdapter);
        recomendGride.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //点击推荐历史记录
                String keyword = serchBeanList.get(i).name;
                startActivityBySearch(keyword);
            }
        });
        historyAdapter = new HistoryAdapter(historyBeanList,CompanySearchActivity.this);
        historyGride.setAdapter(historyAdapter);
        historyGride.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //点击了历史记录
                String keyword = historyBeanList.get(i).content;
                startActivityBySearch(keyword);
            }
        });
           getSerchLaber();//推荐搜索记录
           getHistoryLaber(MainActivity.id);//历史记录
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(
                            v.getApplicationWindowToken(), 0);
                }
            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }
                    String keyword =search.getText().toString().trim();
                    startActivityBySearch(keyword);

                    return true;
                }
                return false;
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
    private void startActivityBySearch(String keyword){

        if(!TextUtils.isEmpty(keyword)){
            Intent intent = new Intent(CompanySearchActivity.this,SerchResultActivity.class);
            intent.putExtra("keyword",keyword);
            CompanySearchActivity.this.startActivity(intent);
            historyBeanList.clear();
            getHistoryLaber(MainActivity.id);
        }
    }
  @OnClick(R.id.tab_search_)
  public void setSearch(View view){
      String keyword =search.getText().toString().trim();
      startActivityBySearch(keyword);
  }


    /**
     * 推荐搜索标签
     */
    private void getSerchLaber(){
        String url= "https://qrb.shoomee.cn//qrb_api/getRecommendSearchLabel";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(CompanySearchActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("RecommendSerchBean",obj.toString());
                            List<RecommendSerchBean> temp  = JSON.parseArray(obj.optJSONArray("data").toString(), RecommendSerchBean.class);
                            serchBeanList.addAll(temp);
                            commendHistoryAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CompanySearchActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    /**
     * 历史搜索标签
     */
    private void getHistoryLaber(String id){
        String url= "https://qrb.shoomee.cn/qrb_api/getSearchLogs?user_id="+id;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(CompanySearchActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("RecommendSerchBean",obj.toString());
                            List<HistoryBean> temp  = JSON.parseArray(obj.optJSONArray("data").toString(), HistoryBean.class);
                            historyBeanList.addAll(temp);
                            historyAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CompanySearchActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
