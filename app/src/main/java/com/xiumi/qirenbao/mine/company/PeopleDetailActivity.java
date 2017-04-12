package com.xiumi.qirenbao.mine.company;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.home.bean.RecommendTypeBean;
import com.xiumi.qirenbao.login.LoginActivity;
import com.xiumi.qirenbao.mine.adapter.SkillItemAdapter;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.utils.UserInfo;
import com.xiumi.qirenbao.widget.CircleImageView;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 完善用户信息
 * 20170303 add by qianbailu
 */
public class PeopleDetailActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.job_operation)
    EditText jobOperation;//从业年限
    @Bind(R.id.job)
    EditText job;//职务
    @Bind(R.id.technical_post)
    EditText technicalPost;//职称
    @Bind(R.id.address)
    EditText address;//地址
    @Bind(R.id.introduction)
    EditText introduction;//简介
    @Bind(R.id.submit)
    TextView submit;
    @Bind(R.id.change_info)
    Button change_info;
    @Bind(R.id.avatar)
    CircleImageView avatar;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.growth_value)
    TextView growth_value;
    @Bind(R.id.skill)
    TextView skill;
    @Bind(R.id.girde)
    GridView gride;
    @Bind(R.id.skill_layout)
    LinearLayout skill_layout;
    @Bind(R.id.toolbar)
    RelativeLayout toolbar;
    @Bind(R.id.sure_change)
    Button sure_change;
    @Bind(R.id.introduction_show)
    TextView introduction_show;
    @Bind(R.id.scoll)
    ScrollView scoll;
    private int sexNo;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private String sex;
    private SkillItemAdapter skillItemAdapter;
    private List<RecommendTypeBean> typeBeanList = new ArrayList<>();
    private String class_id;
    // 自动登录
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_detail);
        ButterKnife.bind(this);
        setStatusBar();
        userInfo = new UserInfo(this);
        jobOperation.setFocusableInTouchMode(false);
        jobOperation.clearFocus();
        job.setFocusableInTouchMode(false);
        job.clearFocus();
        technicalPost.setFocusableInTouchMode(false);
        technicalPost.clearFocus();
        address.setFocusableInTouchMode(false);
        address.clearFocus();
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeopleDetailActivity.this, ChangeNameActivity.class);
                startActivity(intent);
            }
        });
        if (MainActivity.sex != null)
            name.setText(MainActivity.name);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (!MainActivity.work_years.equals("null"))
            jobOperation.setText(MainActivity.work_years);
        if (!MainActivity.growth_value.equals("null"))
            growth_value.setText(MainActivity.growth_value);
        data_list = new ArrayList<String>();
        if (MainActivity.sex.equals("1")) {
            data_list.add("男");
            data_list.add("女");
            sexNo = 1;
        } else {
            data_list.add("女");
            data_list.add("男");
            sexNo = 2;
        }
        if (!MainActivity.work_duty.equals("null"))
            technicalPost.setText(MainActivity.work_title);
        if (!MainActivity.work_title.equals("null"))
            job.setText(MainActivity.work_duty);
        if (!MainActivity.company.equals("null"))
            address.setText(MainActivity.company);
        if (!MainActivity.description.equals("null")) {
            introduction_show.setText(MainActivity.description);
            introduction.setText(MainActivity.description);
        }
        //适配器
        arr_adapter = new ArrayAdapter<String>(PeopleDetailActivity.this, R.layout.spinner_layout, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                sex = data_list.get(arg2);
                Log.e("sex", sex);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        if (MainActivity.chose == 1) {
            //企业
            skill_layout.setVisibility(View.GONE);
            toolbar.setBackgroundColor(getResources().getColor(R.color.title_bg));
            change_info.setBackgroundColor(getResources().getColor(R.color.title_bg));
            sure_change.setBackgroundColor(getResources().getColor(R.color.title_bg));
            class_id = MainActivity.class_id;
        } else {
            skillItemAdapter = new SkillItemAdapter(typeBeanList, PeopleDetailActivity.this);
            gride.setAdapter(skillItemAdapter);
            getRecommendType();
        }
        if (MainActivity.chose == 3) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.title_bg));
            change_info.setBackgroundColor(getResources().getColor(R.color.title_bg));
            sure_change.setBackgroundColor(getResources().getColor(R.color.title_bg));
        }
        gride.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeBeanList.get(position).isCheck = true;
                for (int i = 0; i < typeBeanList.size(); i++) {
                    if (i != position) {
                        typeBeanList.get(i).isCheck = false;
                    }
                }
                class_id = typeBeanList.get(position).id;
                skillItemAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MainActivity.avatar != null) {
            ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + MainActivity.avatar, avatar);
        } else {
            avatar.setImageDrawable(getResources().getDrawable(R.drawable.peoppe));
        }
    }

    protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);
        if (MainActivity.chose == 2) {
            tintManager.setTintColor(getResources().getColor(R.color.orangenormal));
        } else {
            tintManager.setTintColor(getResources().getColor(R.color.title_bg));
        }
    }

    @OnClick(R.id.change_info)
    public void setChange_info(View view) {
        introduction.setVisibility(View.VISIBLE);
        introduction_show.setVisibility(View.GONE);
        introduction.setFocusableInTouchMode(true);
        introduction.requestFocus();
        jobOperation.setFocusableInTouchMode(true);
        jobOperation.requestFocus();
        job.setFocusableInTouchMode(true);
        job.requestFocus();
        technicalPost.setFocusableInTouchMode(true);
        technicalPost.requestFocus();
        address.setFocusableInTouchMode(true);
        address.requestFocus();
        change_info.setVisibility(View.GONE);
        sure_change.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.sure_change)
    public void setSure_change(View view) {
        if (valitate()) {
            setpeopleInfo();
        }
    }

    private boolean valitate() {
        if (StringUtils.isEmpty(jobOperation.getText().toString())) {
            new AlertDialog.Builder(this).setMessage("请输入你的从业年限")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();

            return false;
        }
        if (MainActivity.avatar == null) {
            new AlertDialog.Builder(this).setMessage("头像不能为空哦")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();

            return false;
        }
        if (StringUtils.isEmpty(job.getText().toString())) {
            new AlertDialog.Builder(this).setMessage("请填入你的职务")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();
            return false;
        }
        if (StringUtils.isEmpty(technicalPost.getText().toString())) {
            new AlertDialog.Builder(this).setMessage("请输入你的职称")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();
            return false;
        }
        if (StringUtils.isEmpty(class_id)) {
            new AlertDialog.Builder(this).setMessage("请选择你擅长的技能")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();
            return false;
        }

        if (StringUtils.isEmpty(address.getText().toString())) {
            new AlertDialog.Builder(this).setMessage("请输入你的公司")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();
            return false;
        }
        if (StringUtils.isEmpty(address.getText().toString())) {
            new AlertDialog.Builder(this).setMessage("请输入你的地址")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();
            return false;
        }
        if (StringUtils.isEmpty(introduction.getText().toString())) {
            new AlertDialog.Builder(this).setMessage("请输入你的简介")
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

    @OnClick(R.id.submit)
    public void setSubmit(View view) {
        new AlertDialog.Builder(PeopleDetailActivity.this).setMessage("是否确认注销？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(PeopleDetailActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                userInfo.clear();
                MainActivity.chose = -1;
                MainActivity.INSTANCE.finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    /**
     * 完善用户信息
     */
    private void setpeopleInfo() {
        if (sex.equals("男"))
            sexNo = 1;
        else
            sexNo = 2;
        Log.e("avatar", MainActivity.avatar);
        Log.e("work_years", jobOperation.getText().toString().trim());
        Log.e("work_duty", job.getText().toString().trim());
        Log.e("work_title", technicalPost.getText().toString().trim());
        Log.e("company", address.getText().toString().trim());
        Log.e("description", introduction.getText().toString().trim());
        Log.e("class_id", class_id);
        Log.e("sex", sex);
        String url = "https://qrb.shoomee.cn/api/updateUserInfo";
        OkHttpUtils
                .post()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addParams("avatar", MainActivity.avatar)
                .addParams("work_years", jobOperation.getText().toString().trim())
                .addParams("work_title", technicalPost.getText().toString().trim())
                .addParams("work_duty", job.getText().toString().trim())
                .addParams("company", address.getText().toString().trim())
                .addParams("description", introduction.getText().toString().trim())
                .addParams("class_id", class_id)
                .addParams("sex", sexNo + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(PeopleDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            new AlertDialog.Builder(PeopleDetailActivity.this).setMessage("提交信息成功")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            MainActivity.work_years = jobOperation.getText().toString().trim();
                                            MainActivity.work_duty = job.getText().toString().trim();
                                            MainActivity.work_title = technicalPost.getText().toString().trim();
                                            MainActivity.company = address.getText().toString().trim();
                                            MainActivity.description = introduction.getText().toString().trim();
                                            MainActivity.class_id = class_id;
                                            MainActivity.sex = sexNo + "";
                                            change_info.setVisibility(View.VISIBLE);
                                            sure_change.setVisibility(View.GONE);
                                            jobOperation.setFocusableInTouchMode(false);
                                            jobOperation.clearFocus();
                                            job.setFocusableInTouchMode(false);
                                            job.clearFocus();
                                            technicalPost.setFocusableInTouchMode(false);
                                            technicalPost.clearFocus();
                                            address.setFocusableInTouchMode(false);
                                            address.clearFocus();
                                            introduction.setFocusableInTouchMode(false);
                                            introduction.clearFocus();
                                            scoll.scrollTo(0, 0);
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PeopleDetailActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    /**
     * 获取所有分类
     */
    private void getRecommendType() {
        String url = "https://qrb.shoomee.cn/qrb_api/getServiceClasses";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(PeopleDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            List<RecommendTypeBean> buildChangebeanList = JSON.parseArray(obj.getJSONArray("data").toString(), RecommendTypeBean.class);
                            typeBeanList.addAll(buildChangebeanList);
                            Log.e("typeBeanList", typeBeanList.get(1).name);
                            skillItemAdapter.notifyDataSetChanged();
                            //达人
                            Log.e("MainActivity.class_id", MainActivity.class_id);
                            if (!MainActivity.class_id.equals("null")) {
                                Log.e("typeBeanList", typeBeanList.get(1).name);
                                for (int i = 0; i < buildChangebeanList.size(); i++) {
                                    if (buildChangebeanList.get(i).id.equals(MainActivity.class_id)) {
                                        skill.setText(buildChangebeanList.get(i).name);
                                    }
                                }
                                if (MainActivity.chose == 3) {
                                    class_id = MainActivity.class_id;
                                    skill.setVisibility(View.VISIBLE);
                                    gride.setVisibility(View.GONE);
                                } else {
                                    toolbar.setBackgroundColor(getResources().getColor(R.color.orangenormal));
                                    if (MainActivity.class_id != null) {
                                        class_id = MainActivity.class_id;
                                        skill.setVisibility(View.VISIBLE);
                                        gride.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                skill.setVisibility(View.GONE);
                                gride.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PeopleDetailActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
