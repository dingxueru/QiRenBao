package com.xiumi.qirenbao.mine.company;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.mine.SettingActivity;
import com.xiumi.qirenbao.utils.ImageUtil;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.widget.CircleImageView;
import com.xiumi.qirenbao.widget.SystemBarTintManager;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qianbailu on 2017/2/5.
 * [企业我的]
 */
public class MineFragment extends Fragment {
    @Bind(R.id.change_to_people)
    LinearLayout changeToPeople;
    @Bind(R.id.user_detail)
    LinearLayout userDetail;
   @Bind(R.id.setting)
   TextView setting;
    @Bind(R.id.head)
    CircleImageView head;
    @Bind(R.id.user_name)
    TextView user_name;
    @Bind(R.id.change)
    TextView change;
    private MainActivity mactivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.mine_layout, container, false);
        ButterKnife.bind(this, mView);
        mactivity = (MainActivity) getActivity();
        if(MainActivity.role.equals("3")){
            change.setText("切换为合伙人");
        }
        else{
            change.setText("切换为技能达人");
        }
        user_name.setText(MainActivity.name);
         return mView;
    }
    protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);
        //2是达人1是企业主3是合伙人
        if(MainActivity.chose==2){
            tintManager.setTintColor(getResources().getColor(R.color.orangenormal));
        }else if(MainActivity.chose==3){
            tintManager.setTintColor(getResources().getColor(R.color.title_bg));
        }else {
            tintManager.setTintColor(getResources().getColor(R.color.white));
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if(MainActivity.avatar!=null) {
            ImageUtil.displayImage("https://qrb.shoomee.cn/upload/" + MainActivity.avatar, head);
        }else{
            head.setImageDrawable(getResources().getDrawable(R.drawable.peoppe));
        }
     }
    @OnClick(R.id.change_to_people)
    public void changeToPeople(View view){
        //切换为技能达人界面
        if(MainActivity.role.equals("3")){
            MainActivity.chose = 3;
            setStatusBar();
            mactivity.changeToPartnerShip();
        }else{
            MainActivity.chose =2;
            setStatusBar();
            mactivity.changeToExpert();
        }
    }
    @OnClick(R.id.user_detail)
    public void changeUser(View view){
        //切换到修改用户信息界面
        Intent intent =new Intent(getActivity(),PeopleDetailActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.setting)
    public void setSetting(View view){
        Intent intent=new Intent(getActivity(),SettingActivity.class);
        startActivity(intent);
    }
}
