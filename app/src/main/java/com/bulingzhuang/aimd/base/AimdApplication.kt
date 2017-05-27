package com.bulingzhuang.aimd.base

import android.app.Application
import android.graphics.Typeface
import android.util.SparseArray

import com.avos.avoscloud.AVOSCloud
import com.bulingzhuang.aimd.utils.SharePreferenceUtil

/**
 * Created by bulingzhuang
 * on 2017/3/21
 * E-mail:bulingzhuang@foxmail.com
 */

class AimdApplication : Application() {
    private var mTypeface: Typeface? = null
    private var mTypefaceArray: SparseArray<Typeface>? = null

    override fun onCreate() {
        super.onCreate()

        //初始化LeanCloud相关内容，参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "aA02xsOcM0CVLYl3JPuCR8vQ-gzGzoHsz", "0zghOzYB21nFS1kMOkWYuQRw")

        //初始化SharePreference
        SharePreferenceUtil.initializeInstance(this)

        //初始化全局使用字体
        instance = applicationContext as AimdApplication
        mTypeface = Typeface.createFromAsset(instance!!.assets, "PingFangRegular.ttf")

    }

    val customTypeface: Typeface?
        get() {
            if (mTypeface == null) {
                mTypeface = Typeface.createFromAsset(applicationContext.assets, "PingFangRegular.ttf")
            }
            return mTypeface
        }

    var typefaceArray: SparseArray<Typeface>
        get() {
            if (mTypefaceArray == null) {
                mTypefaceArray = SparseArray<Typeface>()
                mTypefaceArray!!.append(2, Typeface.createFromAsset(applicationContext.assets, "MFJinHei_Noncommercial_Regular.ttf"))
                mTypefaceArray!!.append(3, Typeface.createFromAsset(applicationContext.assets, "MFKeKe_Noncommercial_Regular.ttf"))
                mTypefaceArray!!.append(4, Typeface.createFromAsset(applicationContext.assets, "MFKeSong_Noncommercial_Regular.ttf"))
                mTypefaceArray!!.append(5, Typeface.createFromAsset(applicationContext.assets, "MFLangQian_Noncommercial_Regular.ttf"))
                mTypefaceArray!!.append(6, Typeface.createFromAsset(applicationContext.assets, "MFLangSong_Noncommercial_Regular.ttf"))
                mTypefaceArray!!.append(7, Typeface.createFromAsset(applicationContext.assets, "MFManYu_Noncommercial_Regular.ttf"))
                mTypefaceArray!!.append(8, Typeface.createFromAsset(applicationContext.assets, "MFShangHei_Noncommercial_Regular.ttf"))
                mTypefaceArray!!.append(9, Typeface.createFromAsset(applicationContext.assets, "MFYueYuan_Noncommercial_Regular.ttf"))
                mTypefaceArray!!.append(1, customTypeface)
            }
            return mTypefaceArray as SparseArray<Typeface>
        }
        set(typefaceArray) {
            typefaceArray.append(1, customTypeface)
            mTypefaceArray = typefaceArray
        }

    companion object {

        var instance: AimdApplication? = null
    }
}
