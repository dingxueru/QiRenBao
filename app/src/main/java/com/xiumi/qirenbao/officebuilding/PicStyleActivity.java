package com.xiumi.qirenbao.officebuilding;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.officebuilding.adapter.ImgChangeAdapter;
import com.xiumi.qirenbao.widget.PicViewPager;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by qianbailu on 2017/3/20.
 * 图片放大
 */
public class PicStyleActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.picview_viewpager)
    PicViewPager guideViewPager;

    private List<View> data;
    private int currentPagerNumber = 0 ;
    public static final String PICVIEW_KEY = "picview_res";
    public static final String PICPAGER = "NOWPAGER";
    public static final String HEAD = "USERHEAD";
    private boolean isHead = true ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_style);
        ButterKnife.bind(this);
        initData();

        ImgChangeAdapter adapter = new ImgChangeAdapter(PicStyleActivity.this, data);
        guideViewPager.setAdapter(adapter);
        guideViewPager.setCurrentItem(currentPagerNumber);
        guideViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void initData() {
        data = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);
        List<String> picRes = getIntent().getStringArrayListExtra(PICVIEW_KEY);
        isHead = getIntent().getBooleanExtra(HEAD,false);
        Log.e("POSITION" ,isHead+"");


        if(!TextUtils.isEmpty(getIntent().getStringExtra(PICPAGER) )){
            currentPagerNumber = Integer.parseInt(getIntent().getStringExtra(PICPAGER));
        }else{
            currentPagerNumber = 0 ;
        }


        if(picRes == null){
            View picErrorView = inflater.inflate(R.layout.activity_pic_view, null);
            data.add(picErrorView);
            return;
        }
        for (int i = 0; i < picRes.size(); i++) {
            View guideItem = inflater.inflate(R.layout.activity_pic_view, null);
            guideItem.setOnClickListener(this);
            PhotoView imageView = (PhotoView) guideItem.findViewById(R.id.pic_res_item);

            //单击退出当前的页面
            imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    finish();
                }
            });
            Glide.with(this).load("https://qrb.shoomee.cn/upload/" +picRes.get(i)).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);

            data.add(guideItem);
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
