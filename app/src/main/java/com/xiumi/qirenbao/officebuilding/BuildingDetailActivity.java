package com.xiumi.qirenbao.officebuilding;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.login.LoginActivity;
import com.xiumi.qirenbao.officebuilding.bean.BuildDetailBean;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

import static com.xiumi.qirenbao.R.id.item;

/**
 * Created by qianbailu on 2017/1/20.
 * 写字楼列表详情页
 */
public class BuildingDetailActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_img)
    ImageView ivImg;
    @Bind(R.id.building_price)
    TextView price;
    @Bind(R.id.building_address)
    TextView buildingAddress;
    @Bind(R.id.building_name)
    TextView buildingName;
    @Bind(R.id.building_content)
    TextView buildingContent;
    @Bind(R.id.true_number)
    TextView trueNumber;
    @Bind(R.id.consult)
    TextView consult;
    @Bind(R.id.subscribe)
    TextView subscribe;
    private  String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);
        ButterKnife.bind(this);
        setStatusBar();
        getBuildDetail();
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
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //预约看房
                if(StringUtils.isEmpty(MainActivity.id)){
                    new AlertDialog.Builder(BuildingDetailActivity.this).setMessage("请先登录，才可以预约看房哦！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(BuildingDetailActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }else{
                    Intent intent=new Intent(BuildingDetailActivity.this,DetailButTowActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            }
        });
        setTitle("");
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
     * 获取写字楼详情
     */
    private void getBuildDetail(){
        String url= "https://qrb.shoomee.cn//qrb_api/getBuildingDetail?id="+getIntent().getStringExtra("id");
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(BuildingDetailActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                           List<BuildDetailBean> buildBeanList = JSON.parseArray(obj.getJSONArray("data").toString(), BuildDetailBean.class);
                            //Log.e("data.toString()",data.toString());
                            Log.e("buildChangebeanList",buildBeanList.get(0).pic);
                             final BuildDetailBean buildDetailBean=buildBeanList.get(0);
                            String imgs="https://qrb.shoomee.cn/upload/"+buildDetailBean.pic;
                            Glide.with(BuildingDetailActivity.this).load(imgs).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESULT).into(ivImg);
                            price.setText("¥ "+buildDetailBean.price);
                            buildingName.setText(buildDetailBean.name);
                            buildingAddress.setText(buildDetailBean.address);
                            buildingContent.setText(buildDetailBean.content);
                            id=buildDetailBean.id;
                            consult.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //咨询按钮
                                    Intent intent=new Intent(BuildingDetailActivity.this,DetailButtonActivity.class);
                                    intent.putExtra("telephone",buildDetailBean.telephone);
                                    startActivity(intent);
                                }
                            });
                            trueNumber.setText("实景图片 ("+buildDetailBean.building_imgs.size()+")");
                            if(buildDetailBean.building_imgs.size()>0){
                             final ArrayList<String> pic=new ArrayList<String>();
                                for(int i=0;i<buildDetailBean.building_imgs.size();i++){
                                    pic.add(buildDetailBean.building_imgs.get(i).url);
                                }
                                trueNumber.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //跳转到放大图
                                        //   buildDetailBean.buildingImgs.url;
                                        Intent intent=new Intent(BuildingDetailActivity.this, PicStyleActivity.class);
                                        intent.putExtra(PicStyleActivity.HEAD,true);
                                        intent.putStringArrayListExtra(PicStyleActivity.PICVIEW_KEY,pic);
                                        startActivity(intent);
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BuildingDetailActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
