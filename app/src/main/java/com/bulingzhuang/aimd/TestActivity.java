package com.bulingzhuang.aimd;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bulingzhuang.aimd.view.adapter.AZPagerAdapter;
import com.bulingzhuang.aimd.view.support.ScrollOffsetTransformer;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
    }

    private void init() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_test);
        List<View> viewList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.item_main_card, null);
            TextView tvTextView = (TextView) inflate.findViewById(R.id.tv_content);
            tvTextView.setText("Page-" + i);
            viewList.add(inflate);
        }

        viewPager.setPageTransformer(true, new ScrollOffsetTransformer());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new AZPagerAdapter(viewList));
    }
}
