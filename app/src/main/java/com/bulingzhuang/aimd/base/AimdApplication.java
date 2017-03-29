package com.bulingzhuang.aimd.base;

import android.app.Application;
import android.graphics.Typeface;

import com.avos.avoscloud.AVOSCloud;
import com.bulingzhuang.aimd.utils.SharePreferenceUtil;

/**
 * Created by bulingzhuang
 * on 2017/3/21
 * E-mail:bulingzhuang@foxmail.com
 */

public class AimdApplication extends Application {

    public static AimdApplication sAimdApplication;
    private Typeface mTypeface;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化LeanCloud相关内容，参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "aA02xsOcM0CVLYl3JPuCR8vQ-gzGzoHsz", "0zghOzYB21nFS1kMOkWYuQRw");

        //初始化SharePreference
        SharePreferenceUtil.initializeInstance(this);

        //初始化全局使用字体
        sAimdApplication = (AimdApplication) getApplicationContext();
        mTypeface = Typeface.createFromAsset(sAimdApplication.getAssets(), "PingFangRegular.ttf");
    }

    public static AimdApplication getInstance() {
        return sAimdApplication;
    }

    public Typeface getCustomTypeface() {
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "PingFangRegular.ttf");
        }
        return mTypeface;
    }

    public void modifyCustomTypeface(Typeface typeface) {
        mTypeface = typeface;
    }
}
