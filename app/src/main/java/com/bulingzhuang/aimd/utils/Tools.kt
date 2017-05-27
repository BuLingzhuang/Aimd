package com.bulingzhuang.aimd.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.avos.avoscloud.AVException
import com.bulingzhuang.aimd.R
import com.bulingzhuang.aimd.base.AimdApplication

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by bulingzhuang
 * on 2017/3/21
 * E-mail:bulingzhuang@foxmail.com
 */

object Tools {

    fun showLogE(str: String) {
        if (Constants.DEBUG) {
            Log.e("blz", str)
        }
    }

    /**
     * dp转px

     * @param context
     * *
     * @param dpValue
     * *
     * @return
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px转dp

     * @param context
     * *
     * @param pxValue
     * *
     * @return
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /** 被封印的禁忌♂艺术  */

    /**
     * 修改字体(使用SparseArray字体)

     * @param views
     */
    fun changeFont(code: Int, vararg views: View) {
        views.filterIsInstance<TextView>().forEach { it.typeface = AimdApplication.instance!!.typefaceArray.get(code, AimdApplication.instance!!.customTypeface) }
    }

    /**
     * 修改字体

     * @param views
     */
    fun changeFont(vararg views: View) {
        views.filterIsInstance<TextView>().forEach { it.typeface = AimdApplication.instance!!.customTypeface }
    }

    /**
     * 遍历整个View树，修改文字

     * @param act
     */
    fun changeFonts(act: Activity) {
        val viewGroup = act.window.decorView.findViewById(android.R.id.content) as ViewGroup

        changeFonts(viewGroup)
    }

    /**
     * 遍历整个View树，修改文字(递归向下查找)

     * @param root
     */
    private fun changeFonts(root: ViewGroup) {

        (0..root.childCount - 1)
                .map { root.getChildAt(it) }
                .forEach {
                    if (it is TextView) {
                        it.typeface = AimdApplication.instance!!.customTypeface
                    } else if (it is ViewGroup) {
                        changeFonts(it)
                    }
                }
    }


    /**
     * 白底黑字SnackBar(Default)

     * @param context
     * *
     * @param text
     * *
     * @param genView
     */
    fun showSnackBar(context: Context, text: String, genView: View) {
        val snackbar = Snackbar.make(genView, text, Snackbar.LENGTH_SHORT)
        val snackBarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackBarLayout.background = ContextCompat.getDrawable(context, R.drawable.snackbar_white)
        val tv = snackBarLayout.findViewById(android.support.design.R.id.snackbar_text) as TextView
        tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        changeFont(tv)
        snackbar.show()
    }

    /**
     * 黑底白字SnackBar

     * @param context
     * *
     * @param text
     * *
     * @param genView
     */
    @JvmOverloads fun showSnackBarDark(context: Context, text: String, genView: View, duration: Int = Snackbar.LENGTH_SHORT) {
        val snackbar = Snackbar.make(genView, text, duration)
        val snackBarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackBarLayout.background = ContextCompat.getDrawable(context, R.drawable.snackbar_dark)
        val tv = snackBarLayout.findViewById(android.support.design.R.id.snackbar_text) as TextView
        tv.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        changeFont(tv)
        snackbar.show()
    }

    /**
     * 根据返回错误码，弹对应提示

     * @param context
     */
    fun leanCloudExceptionHadling(context: Context, e: AVException, genView: View) {
        val eCode = e.code
        val eStr: String
        when (eCode) {
            202 -> eStr = context.resources.getString(R.string.e_code_202)
            203 -> eStr = context.resources.getString(R.string.e_code_203)
            205 -> eStr = context.resources.getString(R.string.e_code_205)
            210 -> eStr = context.resources.getString(R.string.e_code_210)
            211 -> eStr = context.resources.getString(R.string.e_code_211)
            219 -> eStr = context.resources.getString(R.string.e_code_219)
            else -> eStr = e.message!!
        }
        Tools.showSnackBarDark(context, eStr, genView, Snackbar.LENGTH_LONG)
    }

    /**
     * 检查是否存在SDCard

     * @return
     */
    fun hasSdcard(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED
    }

    /**
     * 把bitmap对象 进行jpeg格式压缩 并且输出到具体路径

     * @param bitmap
     * *
     * @param path
     */
    fun saveBitmap(bitmap: Bitmap, path: String): String? {
        Log.e("blz", "saveFile:" + path)
        val file = File(path)
        // 若父目录不存在 则创建父目录
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        if (!file.exists()) {
            try {
                file.createNewFile()
                // 把bitmap输出到该文件中
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        // 把bitmap输出到该文件中
        try {
            val compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    FileOutputStream(file))
            if (compress) {
                return path
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获取本机机器码

     * @param context
     * *
     * @return
     */
    fun getAndroidIMEI(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        @SuppressLint("HardwareIds") var deviceId = telephonyManager.deviceId
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = "未知机器码"
        }
        return deviceId
    }
}