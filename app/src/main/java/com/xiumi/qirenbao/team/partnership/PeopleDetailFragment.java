package com.xiumi.qirenbao.team.partnership;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.home.bean.ParterDetailBean;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qianbailu on 2017/3/9.
 * 团员个人详情
 */

public class PeopleDetailFragment extends Fragment {

    @Bind(R.id.work_years)
    TextView workYears;
    @Bind(R.id.user_sex)
    TextView userSex;
    @Bind(R.id.work_title)
    TextView workTitle;
    @Bind(R.id.work_duty)
    TextView workDuty;
    @Bind(R.id.user_address)
    TextView userAddress;
    @Bind(R.id.description)
    TextView description;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_team_member, container, false);
        ButterKnife.bind(this, mView);
        Log.e("use_id",ParnerMemberDetailActivity.user_id);
       getpeopleSerch(ParnerMemberDetailActivity.user_id);
        return mView;
    }
    /**
     * 用户详情
     */
    private void getpeopleSerch(final String use_id){
        String url= "https://qrb.shoomee.cn/qrb_api/getPartner?user_id="+use_id;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(),"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("合伙人搜索",obj.toString());
                            String result=obj.getString("result");
                            if(result.equals("success")){
                                Log.e("data",obj.optJSONArray("data").toString());
                                List<ParterDetailBean> temp  = JSON.parseObject(obj.optJSONArray("data").toString(), new TypeReference<ArrayList<ParterDetailBean>>() {});
                                ParterDetailBean parterDetailBean=temp.get(0);
                                workYears.setText(parterDetailBean.user_info.work_years);
                               if(parterDetailBean.user_info.sex.equals("1")){
                                   userSex.setText("男");
                               }else if(parterDetailBean.user_info.sex.equals("2")){
                                   userSex.setText("女");
                               }
                                workTitle.setText(parterDetailBean.user_info.work_duty);
                                workDuty.setText(parterDetailBean.user_info.work_title);
                                userAddress.setText(parterDetailBean.user_info.company);
                                description.setText(parterDetailBean.user_info.description);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
