package com.bulingzhuang.aimd.base;

import android.app.Application;
import android.graphics.Typeface;
import android.util.SparseArray;

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
    private SparseArray<Typeface> mTypefaceArray;

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

    public SparseArray<Typeface> getTypefaceArray() {
        if (mTypefaceArray == null) {
            mTypefaceArray = new SparseArray<>();
            mTypefaceArray.append(2, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFJinHei_Noncommercial_Regular.ttf"));
            mTypefaceArray.append(3, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFKeKe_Noncommercial_Regular.ttf"));
            mTypefaceArray.append(4, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFKeSong_Noncommercial_Regular.ttf"));
            mTypefaceArray.append(5, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFLangQian_Noncommercial_Regular.ttf"));
            mTypefaceArray.append(6, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFLangSong_Noncommercial_Regular.ttf"));
            mTypefaceArray.append(7, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFManYu_Noncommercial_Regular.ttf"));
            mTypefaceArray.append(8, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFShangHei_Noncommercial_Regular.ttf"));
            mTypefaceArray.append(9, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFYueYuan_Noncommercial_Regular.ttf"));
            mTypefaceArray.append(1,getCustomTypeface());
        }
        return mTypefaceArray;
    }

    public void setTypefaceArray(SparseArray<Typeface> typefaceArray) {
        typefaceArray.append(1,getCustomTypeface());
        mTypefaceArray = typefaceArray;
    }
}
