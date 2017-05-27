package com.bulingzhuang.aimd

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.transition.Fade
import android.transition.TransitionManager
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.avos.avoscloud.AVUser
import com.bulingzhuang.aimd.utils.Tools
import com.bulingzhuang.aimd.view.activity.EditorActivity
import com.bulingzhuang.aimd.view.activity.UserActivity
import com.bulingzhuang.aimd.view.adapter.AZPagerAdapter
import com.bulingzhuang.aimd.view.support.ScrollOffsetTransformer
import com.bumptech.glide.Glide

import java.util.ArrayList

import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private var iv_portrait: ImageView? = null
    private var tv_nickname: TextView? = null
    private var tv_signature: TextView? = null
    private var btn_toolbarLogin: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = ""
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        setSupportActionBar(toolbar)

        fab.setOnClickListener { v ->
            Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        init()
    }

    override fun onResume() {
        super.onResume()
        val currentUser = AVUser.getCurrentUser()
        if (currentUser != null) {
            btn_toolbarLogin!!.visibility = View.GONE
            val portrait = currentUser.get("portrait") as String
            Tools.showLogE("头像地址：" + portrait)
            if (!TextUtils.isEmpty(portrait)) {
                Glide.with(this).load(portrait).crossFade().bitmapTransform(CropCircleTransformation(this)).placeholder(R.mipmap.icon_origin).error(R.mipmap.icon_origin).into(iv_portrait)
            } else {
                iv_portrait!!.setImageResource(R.mipmap.icon_origin)
            }
            val nickname = currentUser.get("nickname") as String
            Tools.showLogE("昵称：" + nickname)
            if (!TextUtils.isEmpty(nickname)) {
                tv_nickname!!.text = nickname
            } else {
                tv_nickname!!.text = "null"
            }
            val signature = currentUser.get("signature") as String
            if (!TextUtils.isEmpty(signature)) {
                tv_signature!!.text = signature
            }
        } else {
            btn_toolbarLogin!!.visibility = View.VISIBLE
            tv_nickname!!.text = "( 未登录 )"
            iv_portrait!!.setImageResource(R.mipmap.icon_origin)
            tv_signature!!.text = "点一下试试吧 ヾ(´･ω･｀)ﾉ"
        }
    }

    private fun init() {

        //设置侧拉栏
        nav_view.setNavigationItemSelectedListener(this)
        val headerView = nav_view.getHeaderView(0)
        iv_portrait = headerView.findViewById(R.id.iv_portrait) as ImageView
        tv_nickname = headerView.findViewById(R.id.tv_nickname) as TextView
        tv_signature = headerView.findViewById(R.id.tv_signature) as TextView
        btn_toolbarLogin = findViewById(R.id.btn_toolbarLogin) as TextView

        Tools.changeFont(tv_nickname!!, tv_signature!!, btn_toolbarLogin!!)
        setViewsOnClickListener(iv_portrait!!, tv_nickname!!, tv_signature!!, btn_toolbarLogin!!)

        val viewList = ArrayList<View>()

        for (i in 0..4) {
            val inflate = LayoutInflater.from(this).inflate(R.layout.item_main_card, null) as LinearLayout
            val cv = inflate.findViewById(R.id.cv_content) as CardView
            val ivText = inflate.findViewById(R.id.iv_text) as TextView
            val ivMap = inflate.findViewById(R.id.iv_map) as ImageView
            val ivSound = inflate.findViewById(R.id.iv_sound) as ImageView
            val ivImage = inflate.findViewById(R.id.iv_image) as ImageView
            val btnAdd = inflate.findViewById(R.id.iv_add) as ImageView
            val btnAddD = inflate.findViewById(R.id.iv_add_double) as ImageView
            TransitionManager.beginDelayedTransition(inflate, Fade())
            btnAdd.setOnClickListener { v ->
                val intent = Intent(this@MainActivity, EditorActivity::class.java)
                val bundle = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity,
                        Pair<View, String>(cv, "cv_content"),
                        Pair<View, String>(ivText, "iv_text"),
                        Pair<View, String>(ivMap, "iv_map"),
                        Pair<View, String>(ivSound, "iv_sound"),
                        Pair<View, String>(ivImage, "iv_image"),
                        Pair<View, String>(btnAddD, "btn_add_finish"),
                        Pair(v, "btn_add_del")
                ).toBundle()
                startActivity(intent, bundle)
            }
            viewList.add(inflate)
        }

        vp_content.setPageTransformer(true, ScrollOffsetTransformer())
        vp_content.offscreenPageLimit = 2
        vp_content.adapter = AZPagerAdapter(viewList)
    }

    private fun setViewsOnClickListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_about) {
            Tools.showSnackBar(this, "关于", content_main)
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

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
        //头像、用户名、签名都跳转登录页面
            R.id.btn_toolbarLogin, R.id.iv_portrait, R.id.tv_signature, R.id.tv_nickname -> startActivity(Intent(this, UserActivity::class.java))
        }
    }
}
