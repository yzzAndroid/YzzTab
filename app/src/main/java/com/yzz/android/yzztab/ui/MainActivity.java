package com.yzz.android.yzztab.ui;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yzz.android.yzztab.R;
import com.yzz.android.yzztab.adapter.MyPageAdapter;
import com.yzz.android.yzztab.reflect.YzzAnn;
import com.yzz.android.yzztab.reflect.YzzAnnotation;
import com.yzz.android.yzztab.view.YzzTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements YzzTabLayout.YzzTabSelectListener{
    @YzzAnnotation(id = R.id.yzz_tab)
    YzzTabLayout yzzTab;
    @YzzAnnotation(id = R.id.vp)
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        YzzAnn<MainActivity> yzzAnn = new YzzAnn<>();
        yzzAnn.bind(this);
        List<String> list = new ArrayList<>();
        list.add("今日头条");
        list.add("今日呵呵");
        list.add("今日无趣");
        list.add("单身狗");
        list.add("今日头条");
        list.add("今日呵呵");
        list.add("今日无趣");
        list.add("单身狗");
//        list.add("今日头条");
//        list.add("今日呵呵");
//        list.add("今日无趣");
//        list.add("单身狗");
//        list.add("今日头条");
//        list.add("今日呵呵");
//        list.add("今日无趣");
//        list.add("单身狗");
//        list.add("今日头条");
//        list.add("今日呵呵");
//        list.add("今日无趣");
//        list.add("单身狗");
//        list.add("今日头条");
//        list.add("今日呵呵");
//        list.add("今日无趣");
//        list.add("单身狗");
        yzzTab.setTabList(list)
                .setYzzTabSelectListener(this)
                //.setUpWithViewPager(viewPager)
                .commit();
        MyPageAdapter adapter=  new MyPageAdapter();
        List<View> imageViews = new ArrayList<>();
        List<Integer> listID = new ArrayList<>();
        listID.add(R.color.one);
        listID.add(R.color.two);
        listID.add(R.color.three);
        listID.add(R.color.four);
        listID.add(R.color.one);
        listID.add(R.color.two);
        listID.add(R.color.three);
        listID.add(R.color.four);
//        listID.add(R.mipmap.one);
//        listID.add(R.mipmap.two);
//        listID.add(R.mipmap.three);
//        listID.add(R.mipmap.four);
//        listID.add(R.mipmap.one);
//        listID.add(R.mipmap.two);
//        listID.add(R.mipmap.three);
//        listID.add(R.mipmap.four);
//        listID.add(R.mipmap.one);
//        listID.add(R.mipmap.two);
//        listID.add(R.mipmap.three);
//        listID.add(R.mipmap.four);
//        listID.add(R.mipmap.one);
//        listID.add(R.mipmap.two);
//        listID.add(R.mipmap.three);
//        listID.add(R.mipmap.four);
        for (int i = 0; i < listID.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setImageResource(listID.get(i));
            imageViews.add(imageView);
        }
        adapter.setImageViews(imageViews);
        //viewPager.setAdapter(adapter);
    }

    @Override
    public void onSelect(View view, int position) {

    }

    @Override
    public void onReSelect(View view, int position) {

    }

    @Override
    public void onUnSelect(View view, int position) {

    }
}
