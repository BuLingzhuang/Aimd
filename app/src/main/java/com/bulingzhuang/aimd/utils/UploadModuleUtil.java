package com.bulingzhuang.aimd.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bulingzhuang.aimd.R;
import com.bulingzhuang.aimd.base.AimdApplication;
import com.bulingzhuang.aimd.entity.ModuleTextEntity;
import com.bulingzhuang.aimd.entity.ModuleTitleEntity;
import com.bumptech.glide.Glide;

/**
 * Created by bulingzhuang
 * on 2017/5/8
 * E-mail:bulingzhuang@foxmail.com
 */

public class UploadModuleUtil {

    /**
     * 添加文本模块View
     *
     * @param context
     * @param parentView
     * @param data
     * @return childId
     */
    public static void uploadTextModule(Context context, LinearLayout parentView, ModuleTextEntity data) {
        TextView tv = new TextView(context);
        int childCount = parentView.getChildCount();
        int px = Tools.dp2px(context, 16);
        if (childCount == 0) {
            tv.setPadding(px, px, px, px*3 / 16);
        } else {
            tv.setPadding(px, px*3 / 16, px, px*3 / 16);
        }
        tv.setText(data.getContent());
        switch (data.getAlignment()) {
            case ModuleTextEntity.Alignment_l:
                tv.setGravity(Gravity.START);
                break;
            case ModuleTextEntity.Alignment_c:
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
            case ModuleTextEntity.Alignment_r:
                tv.setGravity(Gravity.END);
                break;
        }
        tv.setTextSize(data.getTextSize());
        SparseArray<Typeface> typefaceArray = AimdApplication.getInstance().getTypefaceArray();
        tv.setTypeface(typefaceArray.get(data.getTextTypeface()));
        tv.setLineSpacing(0, data.getLineSpacing());
        tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        parentView.addView(tv);
    }

    /**
     * 添加文本模块View
     *
     * @param context
     * @param parentView
     * @param data
     * @return childId
     */
    public static void uploadTitleModule(Context context, LinearLayout parentView, ModuleTitleEntity data) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.module_title, null);
        ImageView ivTitleBg = (ImageView) inflate.findViewById(R.id.iv_title_bg);
        TextView tvTitle = (TextView) inflate.findViewById(R.id.tv_title);
        Glide.with(context).load(data.getImageUrl()).error(R.mipmap.icon_origin)
                .placeholder(R.mipmap.icon_origin).crossFade().into(ivTitleBg);
        tvTitle.setText(data.getTitle());
        tvTitle.setTypeface(AimdApplication.getInstance().getCustomTypeface());
        switch (data.getAlignment()) {
            case ModuleTitleEntity.Alignment_l:
                tvTitle.setGravity(Gravity.START | Gravity.BOTTOM);
                break;
            case ModuleTitleEntity.Alignment_c:
                tvTitle.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                break;
            case ModuleTitleEntity.Alignment_r:
                tvTitle.setGravity(Gravity.END | Gravity.BOTTOM);
                break;
        }
        switch (data.getScaleType()) {
            case ModuleTitleEntity.ScaleType_Crop:
                ivTitleBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case ModuleTitleEntity.ScaleType_Inside:
                ivTitleBg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
        }
        if (data.isShowMask()) {
            tvTitle.setBackground(ContextCompat.getDrawable(context, R.drawable.module_title_shadow));
        } else {
            tvTitle.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        }

        parentView.addView(inflate);
    }
}
