package com.bulingzhuang.aimd.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bulingzhuang.aimd.R;
import com.bulingzhuang.aimd.base.AimdApplication;

/**
 * Created by bulingzhuang
 * on 2017/3/21
 * E-mail:bulingzhuang@foxmail.com
 */

public class Tools {

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
     * 白底灰字SnackBar(Default)
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
}
