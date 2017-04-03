package com.yzz.android.yzztab.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * @name YzzTab
 * @class name：com.yzz.android.yzztab.adapter
 * @anthor yzz
 * @Email:yzzandroid@163.com
 * @time 2017/4/three 0003 下午 12:32
 */
public class MyPageAdapter extends PagerAdapter {
    private List<View> imageViews;

    public void setImageViews(List<View> imageViews) {
        this.imageViews = imageViews;
    }

    @Override
    public int getCount() {
        return imageViews==null?0:imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViews.get(position));
        return imageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews.get(position));
    }

}

