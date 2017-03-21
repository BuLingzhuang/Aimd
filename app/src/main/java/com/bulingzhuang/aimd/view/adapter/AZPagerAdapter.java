package com.bulingzhuang.aimd.view.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by bulingzhuang
 * on 2017/3/21
 * E-mail:bulingzhuang@foxmail.com
 */

public class AZPagerAdapter extends PagerAdapter {

    protected List<View> views;

    public AZPagerAdapter(List<View> viewList) {
        views = viewList;
    }
    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }
}
