package com.xiumi.qirenbao.officebuilding.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianbailu on 2017/3/27.
 */

public class ImgChangeAdapter extends PagerAdapter {
    private Context mContext;
    private List<View> resources = new ArrayList<>();

    public void setResources(List<View> data) {
        resources.clear();
        if (data != null)
            resources.addAll(data);
    }

    public ImgChangeAdapter(Context mContext, List<View> data) {
        this.mContext = mContext;
        this.resources = data;
    }

    @Override
    public int getCount() {
        if (resources == null)
            return 0;
        return resources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(resources.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(resources.get(position), 0);
        return resources.get(position);
    }
}
