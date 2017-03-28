package com.bulingzhuang.aimd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bulingzhuang.aimd.utils.Tools;
import com.bulingzhuang.aimd.view.activity.UserActivity;
import com.bulingzhuang.aimd.view.adapter.AZPagerAdapter;
import com.bulingzhuang.aimd.view.support.ScrollOffsetTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private RelativeLayout mMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        init();
    }

    private void init() {

        //设置侧拉栏
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        ImageView ivPortrait = (ImageView) headerView.findViewById(R.id.iv_portrait);
        TextView tvUsername = (TextView) headerView.findViewById(R.id.tv_username);
        TextView tvSignature = (TextView) headerView.findViewById(R.id.tv_signature);
        TextView tvToolbarLogin = (TextView) findViewById(R.id.btn_toolbarLogin);

        mMainActivity = (RelativeLayout) findViewById(R.id.content_main);

        Tools.changeFont(tvUsername,tvSignature,tvToolbarLogin);
        setViewsOnClickListener(ivPortrait,tvUsername,tvSignature,tvToolbarLogin);
        
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_content);
        List<View> viewList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.item_main_card, null);
            TextView tvTextView = (TextView) inflate.findViewById(R.id.tv_content);
            tvTextView.setText("Page-" + i);
            Tools.changeFont(tvTextView);
            viewList.add(inflate);
        }

        viewPager.setPageTransformer(true, new ScrollOffsetTransformer());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new AZPagerAdapter(viewList));
    }

    private void setViewsOnClickListener(View... views){
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
//      设置右侧三点
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            Tools.showSnackBar(this, "关于", mMainActivity);
        }
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
                //头像、用户名、签名都跳转登录页面
            // TODO: 2017/3/27 测试
            case R.id.btn_toolbarLogin:
                startActivity(new Intent(this,TestActivity.class));
                break;
            case R.id.iv_portrait:
            case R.id.tv_signature:
            case R.id.tv_username:
                startActivity(new Intent(this, UserActivity.class));
                break;
        }
    }
}
