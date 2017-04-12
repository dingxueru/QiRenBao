package com.xiumi.qirenbao.home.partnership;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.home.bean.UserAllBean;
import com.xiumi.qirenbao.mine.company.ChangeNameActivity;
import com.xiumi.qirenbao.mine.company.PeopleDetailActivity;
import com.xiumi.qirenbao.myWallet.MyWalletActivity;
import com.xiumi.qirenbao.myWallet.bean.RecentlyBean;
import com.xiumi.qirenbao.mygift.bean.GrowthLogBean;
import com.xiumi.qirenbao.mygift.bean.MasterGiftBean;
import com.xiumi.qirenbao.mygift.partnership.PartnerGiftActivity;
import com.xiumi.qirenbao.mygift.partnership.PartnerGrowthLogsActivity;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.widget.CircleImageView;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qianbailu on 2017/2/4.
 * 合伙-首页
 */
public class CompanyHome extends Fragment {
    public static final String city = "扬州";
    public static final int REQUSET = 1;
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.group_no)
    TextView groupNo;
    @Bind(R.id.watch_wallet)
    TextView watchWallet;
    @Bind(R.id.new_get)
    TextView newGet;//钱包最近一笔收益
    @Bind(R.id.change_messg)
    TextView changeMessg;
    @Bind(R.id.partner_img)
    CircleImageView partner_img;
    @Bind(R.id.partner_name)
    TextView partner_name;
    @Bind(R.id.change_to_company)
    LinearLayout changeToCompany;
    @Bind(R.id.change_type)
    TextView change_type;
    @Bind(R.id.go_gift)
    TextView go_gift;
    // 最新一次经验值
    @Bind(R.id.last_growth)
    TextView last_growth;
    @Bind(R.id.last_growth_content)
    TextView last_growth_content;
    @Bind(R.id.last_growth_time)
    TextView last_growth_time;

    // 最新一次收到的打赏礼物
    @Bind(R.id.last_receive_time)
    TextView last_receive_time;
    @Bind(R.id.last_receive_gift)
    TextView last_receive_gift;
    @Bind(R.id.last_receive_content)
    TextView last_receive_content;
    @Bind(R.id.gift_icon)
    ImageView gift_icon;
    // 有礼物布局
    @Bind(R.id.is_gift)
    LinearLayout is_gift;
    @Bind(R.id.no_gift)
    TextView no_gift;
    // 性别 工作  成长值
    @Bind(R.id.sex)
    ImageView sex;
    @Bind(R.id.work_duty)
    TextView work_duty;
    @Bind(R.id.growth_value)
    TextView growth_value;
    @Bind(R.id.to_wallet)
    ImageView to_wallet;
    @Bind(R.id.watch_gift)
    ImageView watch_gift;
    @Bind(R.id.watch_group)
    ImageView watch_group;
    @Bind(R.id.member)
    TextView member;
    @Bind(R.id.activity)
    TextView activity;
    @Bind(R.id.export)
    TextView export;
    @Bind(R.id.city)
    TextView setCity;
    @Bind(R.id.member1)
    TextView member1;
    @Bind(R.id.activity1)
    TextView activity1;
    @Bind(R.id.export1)
    TextView export1;
    @Bind(R.id.city1)
    TextView setCity1;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.name1)
    TextView name1;
    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.img1)
    ImageView img1;
    @Bind(R.id.wallet_time)
    TextView wallet_time;  // 钱包最后一次收益时间
    private MainActivity mActivity;
    public static   String growthValue,team_users,activities,partners,sign_cities;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.com_home_layout, container, false);
        ButterKnife.bind(this, mView);
        // 获取钱包的最近一次收益
        getRecently();
        mActivity = (MainActivity) getActivity();
        // 获取我收到的礼物
        ReceiveGifts();
        // 经验值成长记录
        GrowthLogs();
        getMyCard();
        // 设置静态数据
        partner_name.setText(MainActivity.name);

        // 当前时间显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = format.format(curDate);
        wallet_time.setText(str);
        last_receive_time.setText(str);
        last_growth_time.setText(str);
        return mView;
    }

    @Override
    public void onResume(){
        super.onResume();
        setStatusBar();

        if (MainActivity.sex.equals("2")) {
            sex.setImageDrawable(getResources().getDrawable(R.drawable.women));
        } else {
            sex.setImageDrawable(getResources().getDrawable(R.drawable.men));
        }
        work_duty.setText(MainActivity.work_duty);
        if(MainActivity.avatar!=null) {
            ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + MainActivity.avatar, partner_img);
        }else{
            partner_img.setImageDrawable(getResources().getDrawable(R.drawable.peoppe));
        } }
    protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);
        if(MainActivity.chose==2){
            tintManager.setTintColor(getResources().getColor(R.color.orangenormal));
        }else if(MainActivity.chose==3){
            tintManager.setTintColor(getResources().getColor(R.color.title_bg));
        }else {
            tintManager.setTintColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == REQUSET && resultCode == getActivity().RESULT_OK) {
            location.setText(data.getStringExtra("location"));
        }
    }

    @OnClick(R.id.change_messg)
    public void setChangeMessg(View view) {
        //[企业]完善信息
        Intent intent = new Intent(getActivity(), PeopleDetailActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.group_no)
    public void getgroupNo(View view) {
        //获取成长值
        Intent intent = new Intent(getActivity(), PartnerGrowthLogsActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.watch_group)
    public void setWatchGroup(View view){
        //获取成长值
        Intent intent = new Intent(getActivity(), PartnerGrowthLogsActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.watch_wallet)
    public void toWallet(View view) {
        Intent intent = new Intent(getActivity(), MyWalletActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.to_wallet)
    public void setWallet(View view) {
        Intent intent = new Intent(getActivity(), MyWalletActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.go_gift)
    public void toGift(View view) {
        Intent intent = new Intent(getActivity(), PartnerGiftActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.watch_gift)
    public void setWatchWallet(View view){
        Intent intent = new Intent(getActivity(), PartnerGiftActivity.class);
        startActivity(intent);
    }
    /**
     * 合伙人成长值记录
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
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.e("合伙人成长值记录", object.toString());
                            if (object.getString("result").equals("success")) {
                                JSONObject json = object.optJSONObject("data");
                                JSONArray jsonArray = json.optJSONArray("data");
                                ArrayList<GrowthLogBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<GrowthLogBean>>() {
                                });
                                if (temp.size() > 0) {
                                    if (!TextUtils.isEmpty(temp.get(0).title)) {
                                        last_growth_content.setText(temp.get(0).title);
                                    }
                                    if (!TextUtils.isEmpty(temp.get(0).growths_value_change)) {
                                        last_growth.setText("+" + temp.get(0).growths_value_change);
                                    }
                                    if (!TextUtils.isEmpty(temp.get(0).created_at)) {
                                        last_growth_time.setText(temp.get(0).created_at);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "数据解析错误", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }
    /**
     * 我收到的礼物
     */
    private void ReceiveGifts() {
        String url = "https://qrb.shoomee.cn/api/receiveGifts";
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.e("我收到的礼物", object.toString());
                            if (object.getString("result").equals("success")) {
                                JSONObject json = object.optJSONObject("data");
                                JSONArray jsonArray = json.optJSONArray("data");
                                ArrayList<MasterGiftBean> temp = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<MasterGiftBean>>() {
                                });
                                if (temp.size() > 0) {
                                    is_gift.setVisibility(View.VISIBLE);
                                    if (!TextUtils.isEmpty(temp.get(0).gift.name)) {
                                        last_receive_gift.setText(temp.get(0).total + "个" + temp.get(0).gift.name);
                                    }
                                    if (!TextUtils.isEmpty(temp.get(0).gift.created_at)) {
                                        last_receive_time.setText(temp.get(0).gift.created_at);
                                    }
                                    if (!TextUtils.isEmpty(temp.get(0).gift.icon)) {
                                        ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + temp.get(0).gift.icon, gift_icon);
                                    }
                                    // 待完善打赏对象
                                    last_receive_content.setText("队员打赏给你的礼物");
                                }else {
                                    no_gift.setVisibility(View.VISIBLE);
                                }
                            }else {
                                no_gift.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "数据解析错误", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }

    /**
     * 获取钱包的最近一次收益
     */
    private void getRecently() {
        String url = "https://qrb.shoomee.cn//api/lastAddMoney?user_id=" + MainActivity.id;
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("获取钱包的最近一次收益", obj.toString());
                            List<RecentlyBean> temp = JSON.parseArray(obj.getJSONArray("data").toString(), RecentlyBean.class);
                            if (temp.size() > 0) {
                                RecentlyBean recentlyBean = temp.get(0);
                                if (recentlyBean.change_type.equals("REDUCE")) {
                                    newGet.setText("-" + recentlyBean.change_num);
                                } else {
                                    newGet.setText("+" + recentlyBean.change_num);
                                }
                                wallet_time.setText(recentlyBean.created_at);
                                change_type.setText(recentlyBean.log_title);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "数据解析错误", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @OnClick(R.id.change_to_company)
    public void setChangeToCompany(View view) {
        MainActivity.chose = 1;
        //切换到企业主
        setStatusBar();
        mActivity.changeToCompany();
    }
    /**
     * 成长值
     */
    private void getAllCard() {
        String url = "https://qrb.shoomee.cn/qrb_api/getAllCard";
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("getAllCard", obj.toString());
                            if (obj.getString("result").equals("success")) {
                                List<UserAllBean> temp = JSON.parseObject(obj.optJSONArray("data").toString(), new TypeReference<ArrayList<UserAllBean>>() {
                                });
                                for(int i=0;i<temp.size();i++){
                                    if(temp.get(i).lv.equals(MainActivity.user_lv)){
                                        member.setText("队员数    "+team_users+"/"+temp.get(i).team_users);
                                        activity.setText("活动数   "+activities+"/"+temp.get(i).activities);
                                        export.setText("发展合伙人  "+partners+"/"+temp.get(i).partners);
                                        setCity.setText("签约城市  "+sign_cities+"/"+temp.get(i).sign_cities);
                                        member1.setText("队员数    "+team_users+"/"+temp.get(i+1).team_users);
                                        activity1.setText("活动数   "+activities+"/"+temp.get(i+1).activities);
                                        export1.setText("发展合伙人  "+partners+"/"+temp.get(i+1).partners);
                                        setCity1.setText("签约城市  "+sign_cities+"/"+temp.get(i+1).sign_cities);
                                        growth_value.setText(growthValue+"/"+temp.get(i+1).growth_value);
                                        name.setText(temp.get(i).name);
                                        name1.setText(temp.get(i+1).name);
                                         if(temp.get(i).id.equals("2")){
                                            Log.e("temp.get(i).id",temp.get(i).id);
                                            img.setImageDrawable(getResources().getDrawable(R.drawable.master_home_platinum));
                                            img1.setImageDrawable(getResources().getDrawable(R.drawable.partner_home_diamonds));
                                        }else if(temp.get(i).id.equals("3")){
                                            Log.e("temp.get(i).id",temp.get(i).id);
                                            img.setImageDrawable(getResources().getDrawable(R.drawable.partner_home_diamonds));
                                            img1.setImageDrawable(getResources().getDrawable(R.drawable.img_first));
                                        }else  if(temp.get(i).id.equals("3")){
                                            Log.e("temp.get(i).id",temp.get(i).id);
                                            img.setImageDrawable(getResources().getDrawable(R.drawable.img_first));
                                            img1.setImageDrawable(getResources().getDrawable(R.drawable.img_first));
                                        }
                                    }

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "数据解析错误", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }
    /**
     * 成长值
     */
    private void getMyCard() {
        String url = "https://qrb.shoomee.cn/api/getUserCard  ";
        OkHttpUtils
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("getAllCard", obj.toString());
                            if (obj.getString("result").equals("success")) {
                                JSONObject data = obj.optJSONObject("data");
                                growthValue=data.optString("growth_value");
                                team_users=data.optString("team_users");
                                activities=data.optString("activities");
                                partners=data.optString("partners");
                                sign_cities=data.optString("sign_cities");
                                getAllCard();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "数据解析错误", Toast.LENGTH_SHORT).show();
                            Log.v("Exception", e.toString());
                        }
                    }
                });
    }
}
