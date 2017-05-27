package com.bulingzhuang.aimd.utils

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.util.SparseArray
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.bulingzhuang.aimd.R
import com.bulingzhuang.aimd.base.AimdApplication
import com.bulingzhuang.aimd.entity.ModuleImageEntity
import com.bulingzhuang.aimd.entity.ModuleTextEntity
import com.bulingzhuang.aimd.entity.ModuleTitleEntity
import com.bumptech.glide.Glide

/**
 * Created by bulingzhuang
 * on 2017/5/8
 * E-mail:bulingzhuang@foxmail.com
 */

object UploadModuleUtil {

    /**
     * 添加文本模块View

     * @param context
     * *
     * @param parentView
     * *
     * @param data
     * *
     * @return childId
     */
    fun uploadTextModule(context: Context, parentView: LinearLayout, data: ModuleTextEntity) {
        val tv = TextView(context)
        val childCount = parentView.childCount
        val px = Tools.dp2px(context, 16f)
        if (childCount == 0) {
            tv.setPadding(px, px, px, px * 3 / 16)
        } else {
            tv.setPadding(px, px * 3 / 16, px, px * 3 / 16)
        }
        tv.text = data.content
        when (data.alignment) {
            ModuleTextEntity.Alignment_l -> tv.gravity = Gravity.START
            ModuleTextEntity.Alignment_c -> tv.gravity = Gravity.CENTER_HORIZONTAL
            ModuleTextEntity.Alignment_r -> tv.gravity = Gravity.END
        }
        tv.textSize = data.textSize.toFloat()
        val typefaceArray = AimdApplication.instance!!.typefaceArray
        tv.typeface = typefaceArray.get(data.textTypeface)
        tv.setLineSpacing(0f, data.lineSpacing)
        tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        parentView.addView(tv)
    }

    /**
     * 添加文本模块View

     * @param context
     * *
     * @param parentView
     * *
     * @param data
     * *
     * @return childId
     */
    fun uploadTitleModule(context: Context, parentView: LinearLayout, data: ModuleTitleEntity) {
        val inflate = LayoutInflater.from(context).inflate(R.layout.module_title, null)
        val ivTitleBg = inflate.findViewById(R.id.iv_title_bg) as ImageView
        val tvTitle = inflate.findViewById(R.id.tv_title) as TextView
        Glide.with(context).load(data.imageUrl).error(R.mipmap.icon_origin)
                .placeholder(R.mipmap.icon_origin).crossFade().into(ivTitleBg)
        tvTitle.text = data.title
        tvTitle.typeface = AimdApplication.instance!!.customTypeface
        when (data.alignment) {
            ModuleTitleEntity.Alignment_l -> tvTitle.gravity = Gravity.START or Gravity.BOTTOM
            ModuleTitleEntity.Alignment_c -> tvTitle.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
            ModuleTitleEntity.Alignment_r -> tvTitle.gravity = Gravity.END or Gravity.BOTTOM
        }
        when (data.scaleType) {
            ModuleTitleEntity.ScaleType_Crop -> ivTitleBg.scaleType = ImageView.ScaleType.CENTER_CROP
            ModuleTitleEntity.ScaleType_Inside -> ivTitleBg.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
        if (data.isShowMask) {
            tvTitle.background = ContextCompat.getDrawable(context, R.drawable.module_title_shadow)
        } else {
            tvTitle.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        }

        parentView.addView(inflate)
    }

    fun uploadImageModule(context: Context, parentView: LinearLayout, data: ModuleImageEntity) {
        val type = data.type
        val picList = data.picList
        var inflate: View? = null
        when (picList!!.size) {
            1 -> {
                inflate = LayoutInflater.from(context).inflate(R.layout.module_image_1_0, null)
                Tools.showLogE("加载的1-0")
            }
            2 -> when (type) {
                0 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_2_0, null)
                    Tools.showLogE("加载的2-0")
                }
                1 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_2_1, null)
                    Tools.showLogE("加载的2-1")
                }
            }
            3 -> when (type) {
                0 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_3_0, null)
                    Tools.showLogE("加载的3-0")
                }
                1 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_3_1, null)
                    Tools.showLogE("加载的3-1")
                }
                2 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_3_2, null)
                    Tools.showLogE("加载的3-2")
                }
                3 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_3_3, null)
                    Tools.showLogE("加载的3-3")
                }
            }

            4 -> when (type) {
                0 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_4_0, null)
                    Tools.showLogE("加载的4-0")
                }
                1 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_4_1, null)
                    Tools.showLogE("加载的4-1")
                }
                2 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_4_2, null)
                    Tools.showLogE("加载的4-2")
                }
                3 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_4_3, null)
                    Tools.showLogE("加载的4-3")
                }
                4 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_4_4, null)
                    Tools.showLogE("加载的4-4")
                }
            }
        }
        if (inflate != null) {
            for (i in picList.indices) {
                var iv: ImageView? = null
                when (i) {
                    0 -> iv = inflate.findViewById(R.id.iv_0) as ImageView
                    1 -> iv = inflate.findViewById(R.id.iv_1) as ImageView
                    2 -> iv = inflate.findViewById(R.id.iv_2) as ImageView
                    3 -> iv = inflate.findViewById(R.id.iv_3) as ImageView
                }
                if (iv != null) {
                    Glide.with(context).load(picList[i].uri).error(R.mipmap.icon_origin).crossFade().into(iv)
                }
            }
            parentView.addView(inflate)
        }
    }
}
