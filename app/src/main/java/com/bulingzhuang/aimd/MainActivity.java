package com.bulingzhuang.aimd;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Pair;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.bulingzhuang.aimd.utils.Tools;
import com.bulingzhuang.aimd.view.activity.EditorActivity;
import com.bulingzhuang.aimd.view.activity.UserActivity;
import com.bulingzhuang.aimd.view.adapter.AZPagerAdapter;
import com.bulingzhuang.aimd.view.support.ScrollOffsetTransformer;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private RelativeLayout mMainActivity;
    private TextView mTvNickname;
    private ImageView mIvPortrait;
    private TextView mTvSignature;
    private TextView mTvToolbarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            mTvToolbarLogin.setVisibility(View.GONE);
            String portrait = (String) currentUser.get("portrait");
            Tools.showLogE("头像地址：" + portrait);
            if (!TextUtils.isEmpty(portrait)) {
                Glide.with(this).load(portrait).crossFade().bitmapTransform(new CropCircleTransformation(this)).placeholder(R.mipmap.icon_origin).error(R.mipmap.icon_origin).into(mIvPortrait);
            } else {
                mIvPortrait.setImageResource(R.mipmap.icon_origin);
            }
            String nickname = (String) currentUser.get("nickname");
            Tools.showLogE("昵称：" + nickname);
            if (!TextUtils.isEmpty(nickname)) {
                mTvNickname.setText(nickname);
            } else {
                mTvNickname.setText("null");
            }
            String signature = (String) currentUser.get("signature");
            if (!TextUtils.isEmpty(signature)) {
                mTvSignature.setText(signature);
            }
        } else {
            mTvToolbarLogin.setVisibility(View.VISIBLE);
            mTvNickname.setText("( 未登录 )");
            mIvPortrait.setImageResource(R.mipmap.icon_origin);
            mTvSignature.setText("点一下试试吧 ヾ(´･ω･｀)ﾉ");
        }
    }

    private void init() {

        //设置侧拉栏
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        mIvPortrait = (ImageView) headerView.findViewById(R.id.iv_portrait);
        mTvNickname = (TextView) headerView.findViewById(R.id.tv_nickname);
        mTvSignature = (TextView) headerView.findViewById(R.id.tv_signature);
        mTvToolbarLogin = (TextView) findViewById(R.id.btn_toolbarLogin);

        mMainActivity = (RelativeLayout) findViewById(R.id.content_main);

        Tools.changeFont(mTvNickname, mTvSignature, mTvToolbarLogin);
        setViewsOnClickListener(mIvPortrait, mTvNickname, mTvSignature, mTvToolbarLogin);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_content);
        List<View> viewList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            LinearLayout inflate = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_main_card, null);
            final CardView cv = (CardView) inflate.findViewById(R.id.cv_content);
            final TextView ivText = (TextView) inflate.findViewById(R.id.iv_text);
            final ImageView ivMap = (ImageView) inflate.findViewById(R.id.iv_map);
            final ImageView ivSound = (ImageView) inflate.findViewById(R.id.iv_sound);
            final ImageView ivImage = (ImageView) inflate.findViewById(R.id.iv_image);
            ImageView btnAdd = (ImageView) inflate.findViewById(R.id.iv_add);
            final ImageView btnAddD = (ImageView) inflate.findViewById(R.id.iv_add_double);
            TransitionManager.beginDelayedTransition(inflate, new Fade());
            btnAdd.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                        new Pair<>(cv, "cv_content"),
                        new Pair<>(ivText, "iv_text"),
                        new Pair<>(ivMap, "iv_map"),
                        new Pair<>(ivSound, "iv_sound"),
                        new Pair<>(ivImage, "iv_image"),
                        new Pair<>(btnAddD, "btn_add_finish"),
                        new Pair<>(v, "btn_add_del")
                ).toBundle();
                startActivity(intent, bundle);
            });
            viewList.add(inflate);
        }

        viewPager.setPageTransformer(true, new ScrollOffsetTransformer());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new AZPagerAdapter(viewList));
    }

    private void setViewsOnClickListener(View... views) {
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
            case R.id.btn_toolbarLogin:
            case R.id.iv_portrait:
            case R.id.tv_signature:
            case R.id.tv_nickname:
                startActivity(new Intent(this, UserActivity.class));
                break;
        }
    }
}
