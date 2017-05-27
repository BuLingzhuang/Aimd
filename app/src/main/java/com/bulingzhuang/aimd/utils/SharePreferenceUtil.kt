package com.bulingzhuang.aimd.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

/**
 * ================================================
 * 作    者：bulingzhuang
 * 版    本：1.0
 * 创建日期：2016-10-11 14:55
 * 描    述：缓存工具类
 * 修订历史：
 * ================================================
 */
class SharePreferenceUtil private constructor(context: Context) {

    init {
        sp = context.getSharedPreferences(Constants.AIMD_SHARED_PREFERENCES,
                Context.MODE_PRIVATE)
    }

    companion object {

        private var sp: SharedPreferences? = null

        private var mInstance: SharePreferenceUtil? = null

        /**

         * @param context
         * *
         * @param key
         * *
         * @param value
         * *
         * @return 是否保存成功
         */
        fun setValue(context: Context, key: String, value: Any): Boolean {
            if (sp == null) {
                sp = context.getSharedPreferences(
                        Constants.AIMD_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            }
            val edit = sp!!.edit()
            if (value is String) {
                return edit.putString(key, value).commit()
            } else if (value is Boolean) {
                return edit.putBoolean(key, value).commit()
            } else if (value is Float) {
                return edit.putFloat(key, value).commit()
            } else if (value is Int) {
                return edit.putInt(key, value).commit()
            } else if (value is Long) {
                return edit.putLong(key, value).commit()
            } else if (value is Set<*>) {
                IllegalArgumentException("Value can not be Set object!")
                return false
            }
            return false
        }

        fun getBoolean(context: Context, key: String): Boolean {
            if (sp == null) {
                sp = context.getSharedPreferences(
                        Constants.AIMD_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            }
            return sp!!.getBoolean(key, false)
        }

        fun getBooleanDefaultTrue(context: Context, key: String): Boolean {
            if (sp == null) {
                sp = context.getSharedPreferences(
                        Constants.AIMD_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            }
            return sp!!.getBoolean(key, true)
        }

        fun getString(context: Context, key: String): String {
            if (sp == null) {
                sp = context.getSharedPreferences(
                        Constants.AIMD_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            }
            return sp!!.getString(key, "")
        }

        fun getFloat(context: Context, key: String): Float {
            if (sp == null) {
                sp = context.getSharedPreferences(
                        Constants.AIMD_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            }
            return sp!!.getFloat(key, 0f)
        }

        fun getInt(context: Context, key: String): Int {
            if (sp == null) {
                sp = context.getSharedPreferences(
                        Constants.AIMD_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            }
            return sp!!.getInt(key, 0)
        }

        fun getLong(context: Context, key: String): Long {
            if (sp == null) {
                sp = context.getSharedPreferences(
                        Constants.AIMD_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            }
            return sp!!.getLong(key, 0)
        }

        /**
         * 初始化

         * @param context
         * *            Context
         */
        @Synchronized fun initializeInstance(context: Context) {
            if (mInstance == null) {
                mInstance = SharePreferenceUtil(context)
            }
        }

        /**
         * 获取实例

         * @return sharedPreference 实例
         */
        val instance: SharePreferenceUtil
            @Synchronized get() {
                if (mInstance == null) {
                    throw IllegalStateException(
                            SharePreferenceUtil::class.java.simpleName + "is not initialized, call initializeInstance() method first")
                }

                return mInstance!!
            }


        fun removeContent(context: Context, key: String) {
            if (sp == null) {
                sp = context.getSharedPreferences(
                        Constants.AIMD_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            }

            val editor = sp!!.edit()
            editor.remove(key)
            editor.apply()
        }
    }
}
