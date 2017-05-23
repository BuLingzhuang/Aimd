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
import com.bulingzhuang.aimd.entity.ModuleImageEntity;
import com.bulingzhuang.aimd.entity.ModuleTextEntity;
import com.bulingzhuang.aimd.entity.ModuleTitleEntity;
import com.bumptech.glide.Glide;

import java.util.List;

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
            tv.setPadding(px, px, px, px * 3 / 16);
        } else {
            tv.setPadding(px, px * 3 / 16, px, px * 3 / 16);
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

    public static void uploadImageModule(Context context, LinearLayout parentView, ModuleImageEntity data) {
        int type = data.getType();
        List<ModuleImageEntity.PicEntity> picList = data.getPicList();
        View inflate = null;
        switch (picList.size()) {
            case 1:
                inflate = LayoutInflater.from(context).inflate(R.layout.module_image_1_0, null);
                Tools.showLogE("加载的1-0");
                break;
            case 2:
                switch (type) {
                    case 0:
                        inflate = LayoutInflater.from(context).inflate(R.layout.module_image_2_0, null);
                        Tools.showLogE("加载的2-0");
                        break;
                    case 1:
                        inflate = LayoutInflater.from(context).inflate(R.layout.module_image_2_1, null);
                        Tools.showLogE("加载的2-1");
                        break;
                }
                break;
            case 3:
                switch (type) {
                    case 0:
                        inflate = LayoutInflater.from(context).inflate(R.layout.module_image_3_0, null);
                        Tools.showLogE("加载的3-0");
                        break;
                    case 1:
                        inflate = LayoutInflater.from(context).inflate(R.layout.module_image_3_1, null);
                        Tools.showLogE("加载的3-1");
                        break;
                    case 2:
                        inflate = LayoutInflater.from(context).inflate(R.layout.module_image_3_2, null);
                        Tools.showLogE("加载的3-2");
                        break;
                    case 3:
                        inflate = LayoutInflater.from(context).inflate(R.layout.module_image_3_3, null);
                        Tools.showLogE("加载的3-3");
                        break;
                }
                break;

            case 4:
                switch (type) {
                    case 0:
                        inflate = LayoutInflater.from(context).inflate(R.layout.module_image_4_0, null);
                        Tools.showLogE("加载的4-0");
                        break;
                    case 1:
                        inflate = LayoutInflater.from(context).inflate(R.layout.module_image_4_1, null);
                        Tools.showLogE("加载的4-1");
                        break;
                    case 2:
                        inflate = LayoutInflater.from(context).inflate(R.layout.module_image_4_2, null);
                        Tools.showLogE("加载的4-2");
                        break;
                    case 3:
                        inflate = LayoutInflater.from(context).inflate(R.layout.module_image_4_3, null);
                        Tools.showLogE("加载的4-3");
                        break;
                    case 4:
                        inflate = LayoutInflater.from(context).inflate(R.layout.module_image_4_4, null);
                        Tools.showLogE("加载的4-4");
                        break;
                }
                break;
        }
        if (inflate != null) {
            for (int i = 0; i < picList.size(); i++) {
                ImageView iv = null;
                switch (i) {
                    case 0:
                        iv = (ImageView) inflate.findViewById(R.id.iv_0);
                        break;
                    case 1:
                        iv = (ImageView) inflate.findViewById(R.id.iv_1);
                        break;
                    case 2:
                        iv = (ImageView) inflate.findViewById(R.id.iv_2);
                        break;
                    case 3:
                        iv = (ImageView) inflate.findViewById(R.id.iv_3);
                        break;
                }
                if (iv != null) {
                    Glide.with(context).load(picList.get(i).getUri()).error(R.mipmap.icon_origin).crossFade().into(iv);
                }
            }
            parentView.addView(inflate);
        }
    }
}
