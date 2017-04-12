package com.xiumi.qirenbao.home.company;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.okhttp.Request;
import com.stx.xhb.xbanner.XBanner;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.home.adapter.TypeAdapter;
import com.xiumi.qirenbao.home.bean.BuildChangebean;
import com.xiumi.qirenbao.home.bean.RecPartnersBean;
import com.xiumi.qirenbao.home.bean.RecommendTypeBean;
import com.xiumi.qirenbao.location.LocationActivity;
import com.xiumi.qirenbao.login.LoginActivity;
import com.xiumi.qirenbao.mine.SettingActivity;
import com.xiumi.qirenbao.mine.company.ChangeNameActivity;
import com.xiumi.qirenbao.mine.company.PeopleDetailActivity;
import com.xiumi.qirenbao.officebuilding.AskActivity;
import com.xiumi.qirenbao.officebuilding.BuildingDetailActivity;
import com.xiumi.qirenbao.officebuilding.DetailButtonActivity;
import com.xiumi.qirenbao.officebuilding.OfficeBuildingActivity;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.widget.CircleImageView;
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
 * 企业主页
 */
public class CompanyFragment extends Fragment implements XBanner.XBannerAdapter {

    @Bind(R.id.first)
    TextView first;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.layout1)
    LinearLayout layout1;
    @Bind(R.id.second)
    TextView second;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.layout2)
    LinearLayout layout2;
    @Bind(R.id.location)
    TextView location;
    @Bind(R.id.serch)
    TextView serch;
    @Bind(R.id.hot_gridview)
    GridView hotGridView;
    @Bind(R.id.office_building)
    ImageView officeBuild;
    @Bind(R.id.xbanner)
    XBanner mXBanner;
    @Bind(R.id.login)
    TextView login;
    @Bind(R.id.head)
    CircleImageView head;
    @Bind(R.id.ask_question)
    ImageView ask_question;
    @Bind(R.id.tell_people)
    ImageView tell_people;
    @Bind(R.id.tell_phone)
    ImageView tell_phone;
    private TypeAdapter typeAdapter;//推荐分类
    private List<RecommendTypeBean> typeBeanList = new ArrayList<>();
    // 轮播图 list
    private ArrayList<RecPartnersBean> bannerlist = new ArrayList<RecPartnersBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.company_home_layout, container, false);
        ButterKnife.bind(this, mView);
        getBuildDetail();

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PeopleDetailActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 轮播图
         */
        mXBanner = (XBanner) mView.findViewById(R.id.xbanner);
        //设置图片切换速度
        mXBanner.setPageChangeDuration(1000);
        //获取首页推荐的合伙人
        getParterPeople();
        //加载广告图片
        mXBanner.setmAdapter(this);
        // 设置广告图片点击事件
        mXBanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, int position) {
                //跳转到选择合伙人列表
                Intent intent = new Intent(getActivity(), PlaceOrderActivity.class);
                intent.putExtra("user_id", bannerlist.get(position).user_id);
                getActivity().startActivity(intent);
            }
        });
        typeAdapter = new TypeAdapter(typeBeanList, getActivity());
        hotGridView.setAdapter(typeAdapter);
        getRecommendType();
        hotGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SerchResultActivity.class);
                intent.putExtra("keyword", typeBeanList.get(position).name);
                startActivity(intent);
            }
        });
        return mView;
    }

    @OnClick(R.id.tell_people)
    public void setTell_people(View view) {
        getPhone();
    }

    @OnClick(R.id.tell_phone)
    public void setTell_phone(View view) {
        getPhone();
    }

    @OnClick(R.id.ask_question)
    public void setQuestion(View view) {
        //跳转到提问界面
        Intent intent = new Intent(getActivity(), AskActivity.class);
        startActivity(intent);

    }

    protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);
        //2是达人1是企业主3是合伙人
        if (MainActivity.chose == 2) {
            tintManager.setTintColor(getResources().getColor(R.color.orangenormal));
        } else if (MainActivity.chose == 3) {
            tintManager.setTintColor(getResources().getColor(R.color.title_bg));
        } else {
            tintManager.setTintColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setStatusBar();
        if (StringUtils.isEmpty(MainActivity.id)) {
            //未登录状态
            login.setVisibility(View.VISIBLE);
            head.setVisibility(View.GONE);
        } else {
            if (MainActivity.avatar != null) {
                ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + MainActivity.avatar, head);
            } else {
                head.setImageDrawable(getResources().getDrawable(R.drawable.peoppe));
            }
            login.setVisibility(View.GONE);
            head.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.login)
    public void setLogin(View view) {
        if (StringUtils.isEmpty(MainActivity.id)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            //切换到修改用户信息界面
            Intent intent = new Intent(getActivity(), PeopleDetailActivity.class);
            startActivity(intent);
        }

    }

    @OnClick(R.id.layout1)
    public void setLayout1(View view) {
        line1.setVisibility(View.VISIBLE);
        line2.setVisibility(View.INVISIBLE);
        second.setTextColor(getResources().getColor(R.color.colorPrimary));
        first.setTextColor(getResources().getColor(R.color.title_bg));
    }

    @OnClick(R.id.layout2)
    public void setLayout2(View view) {
        line1.setVisibility(View.INVISIBLE);
        line2.setVisibility(View.VISIBLE);
        second.setTextColor(getResources().getColor(R.color.title_bg));
        first.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @OnClick(R.id.office_building)
    public void setOfficeBuild(View view) {
        //写字楼页面
        Intent intent = new Intent(getActivity(), OfficeBuildingActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到搜索界面
     */
    @OnClick(R.id.serch)
    public void setSerch(View view) {
        Intent intent = new Intent(getActivity(), CompanySearchActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * 跳转到切换城市页面
     */
  /*  @OnClick(R.id.location)
    public void changeLocation(View view){
        Intent intent=new Intent(getActivity(),LocationActivity.class);
        getActivity().startActivity(intent);
    }
*/
    @Override
    public void loadBanner(XBanner banner, View view, int position) {
        Glide.with(getActivity()).load("https://qrb.shoomee.cn/upload/" + bannerlist.get(position).recommend_pic).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESULT).into((ImageView) view);
    }

    /**
     * 获取手机号码
     */
    private void getPhone() {
        String url = "https://qrb.shoomee.cn/getIndexMobiles";
        OkHttpUtils
                .get()
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
                            if (obj.optString("result").equals("success")) {
                                JSONObject data = obj.optJSONObject("data");
                                String phone = data.optString("qynx");
                                Intent intent = new Intent(getActivity(), DetailButtonActivity.class);
                                intent.putExtra("telephone", phone);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    /**
     * 获取轮播的写字楼
     */
    private void getBuildDetail() {
        String url = "https://qrb.shoomee.cn//qrb_api/getBuildingSliders?city_id=1";
        OkHttpUtils
                .get()
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
                            JSONObject data = obj.optJSONObject("data");
                            List<BuildChangebean> buildChangebeanList = JSON.parseArray(data.getJSONArray("data").toString(), BuildChangebean.class);
                            String img = "https://qrb.shoomee.cn/upload/" + buildChangebeanList.get(0).slider_pic;
                            Glide.with(getActivity()).load(img).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESULT).into(officeBuild);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    /**
     * 获取推荐分类
     */
    private void getRecommendType() {
        String url = "https://qrb.shoomee.cn//qrb_api/getRecClasses";
        OkHttpUtils
                .get()
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
                            List<RecommendTypeBean> buildChangebeanList = JSON.parseArray(obj.getJSONArray("data").toString(), RecommendTypeBean.class);
                            typeBeanList.addAll(buildChangebeanList);
                            typeAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    /**
     * 首页推荐合伙人
     */
    private void getParterPeople() {
        String url = "https://qrb.shoomee.cn/qrb_api/getRecPartners";
        OkHttpUtils
                .get()
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
                            Log.e("首页推荐合伙人", obj.toString());
                            ArrayList<RecPartnersBean> temp = JSON.parseObject(obj.getJSONArray("data").toString().toString(), new TypeReference<ArrayList<RecPartnersBean>>() {
                            });
                            bannerlist.addAll(temp);
                            // 添加广告数据
                            mXBanner.setData(bannerlist, null);  //第二个参数为提示文字资源集合

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
