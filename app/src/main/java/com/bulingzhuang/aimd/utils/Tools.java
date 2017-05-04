package com.bulingzhuang.aimd.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.bulingzhuang.aimd.R;
import com.bulingzhuang.aimd.base.AimdApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by bulingzhuang
 * on 2017/3/21
 * E-mail:bulingzhuang@foxmail.com
 */

public class Tools {

    public static void showLogE(String str) {
        if (Constants.DEBUG) {
            Log.e("blz", str);
        }
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /** 被封印的禁忌♂艺术 */

    /**
     * 修改字体
     *
     * @param views
     */
    public static void changeFont(View... views) {
        for (View view : views) {
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(AimdApplication.getInstance().getCustomTypeface());
            }
        }
    }

    /**
     * 遍历整个View树，修改文字
     *
     * @param act
     */
    public static void changeFonts(Activity act) {
        ViewGroup viewGroup = (ViewGroup) act.getWindow().getDecorView().findViewById(android.R.id.content);

        changeFonts(viewGroup);
    }

    /**
     * 遍历整个View树，修改文字(递归向下查找)
     *
     * @param root
     */
    private static void changeFonts(ViewGroup root) {

        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(AimdApplication.getInstance().getCustomTypeface());
            } else if (v instanceof ViewGroup) {
                changeFonts((ViewGroup) v);
            }
        }
    }


    /**
     * 白底黑字SnackBar(Default)
     *
     * @param context
     * @param text
     * @param genView
     */
    public static void showSnackBar(Context context, String text, View genView) {
        Snackbar snackbar = Snackbar.make(genView, text, Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.snackbar_white));
        TextView tv = (TextView) snackBarLayout.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
        changeFont(tv);
        snackbar.show();
    }

    /**
     * 黑底白字SnackBar
     *
     * @param context
     * @param text
     * @param genView
     */
    public static void showSnackBarDark(Context context, String text, View genView) {
        showSnackBarDark(context, text, genView,Snackbar.LENGTH_SHORT);
    }

    /**
     * 黑底白字SnackBar
     *
     * @param context
     * @param text
     * @param genView
     */
    public static void showSnackBarDark(Context context, String text, View genView,int duration) {
        Snackbar snackbar = Snackbar.make(genView, text, duration);
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.snackbar_dark));
        TextView tv = (TextView) snackBarLayout.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context,android.R.color.white));
        changeFont(tv);
        snackbar.show();
    }

    /**
     * 根据返回错误码，弹对应提示
     *
     * @param context
     */
    public static void leanCloudExceptionHadling(Context context, AVException e,View genView) {
        int eCode = e.getCode();
        String eStr;
        switch (eCode) {
            case 202:
                eStr = context.getResources().getString(R.string.e_code_202);
                break;
            case 203:
                eStr = context.getResources().getString(R.string.e_code_203);
                break;
            case 205:
                eStr = context.getResources().getString(R.string.e_code_205);
                break;
            case 210:
                eStr = context.getResources().getString(R.string.e_code_210);
                break;
            case 211:
                eStr = context.getResources().getString(R.string.e_code_211);
                break;
            case 219:
                eStr = context.getResources().getString(R.string.e_code_219);
                break;
            default:
                eStr = e.getMessage();
                break;
        }
        Tools.showSnackBarDark(context, eStr, genView,Snackbar.LENGTH_LONG);
    }

    /**
     * 检查是否存在SDCard
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 把bitmap对象 进行jpeg格式压缩 并且输出到具体路径
     *
     * @param bitmap
     * @param path
     */
    public static String saveBitmap(Bitmap bitmap, String path) {
//		Log.i("info", "saveFile:" + path);
        File file = new File(path);
        // 若父目录不存在 则创建父目录
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                // 把bitmap输出到该文件中
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        // 把bitmap输出到该文件中
        try {
            boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(file));
            if (compress) {
                return path;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本机机器码
     *
     * @param context
     * @return
     */
    public static String getAndroidIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("HardwareIds") String deviceId = telephonyManager.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = "未知机器码";
        }
        return deviceId;
    }
}
