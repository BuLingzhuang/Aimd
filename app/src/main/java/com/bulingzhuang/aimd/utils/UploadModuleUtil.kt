package com.bulingzhuang.aimd.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Point
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
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

    /**
     * 添加图片模块
     */
    fun uploadImageModule(context: Context, parentView: LinearLayout, data: ModuleImageEntity) {
        val type = data.type
        val picList = data.picList
        var unrestrictedH = false
        var inflate: View? = null
        when (picList!!.size) {
            1 -> {
                inflate = LayoutInflater.from(context).inflate(R.layout.module_image_1_0, null)
                Tools.showLogE("加载的1-0")
                unrestrictedH = true
            }
            2 -> when (type) {
                0 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_2_0, null)
                    Tools.showLogE("加载的2-0")
                }
                1 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_2_1, null)
                    Tools.showLogE("加载的2-1")
                    unrestrictedH = true
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

            5->when(type){
                0 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_5_0, null)
                    Tools.showLogE("加载的5-0")
                }
                1 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_5_1, null)
                    Tools.showLogE("加载的5-1")
                }
                2 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_5_2, null)
                    Tools.showLogE("加载的5-2")
                }
                3 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_5_3, null)
                    Tools.showLogE("加载的5-3")
                }
                4 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_5_4, null)
                    Tools.showLogE("加载的5-4")
                }
                5 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_5_5, null)
                    Tools.showLogE("加载的5-5")
                }
            }

            6->when(type) {
                0 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_6_0, null)
                    Tools.showLogE("加载的6-0")
                }
            }

            7,8,9->
                when(type) {
                0 -> {
                    inflate = LayoutInflater.from(context).inflate(R.layout.module_image_max, null)
                    Tools.showLogE("加载的${picList.size}-0")
                }
            }
        }
        if (inflate != null) {
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inJustDecodeBounds = true
            for (i in picList.indices) {
                var iv: ImageView? = null
                when (i) {
                    0 -> iv = inflate.findViewById(R.id.iv_0) as ImageView
                    1 -> iv = inflate.findViewById(R.id.iv_1) as ImageView
                    2 -> iv = inflate.findViewById(R.id.iv_2) as ImageView
                    3 -> iv = inflate.findViewById(R.id.iv_3) as ImageView
                    4 -> iv = inflate.findViewById(R.id.iv_4) as ImageView
                    5 -> iv = inflate.findViewById(R.id.iv_5) as ImageView
                    6 -> iv = inflate.findViewById(R.id.iv_6) as ImageView
                    7 -> iv = inflate.findViewById(R.id.iv_7) as ImageView
                    8 -> iv = inflate.findViewById(R.id.iv_8) as ImageView
                }
                if (iv != null) {
                    if (unrestrictedH) {
                        val point = Point()
                        (context as AppCompatActivity).windowManager.defaultDisplay.getSize(point)
                        val viewW = (point.x - Tools.dp2px(context, 62f))
                        BitmapFactory.decodeFile(picList[i].uri, bitmapOptions)
                        val imageW = bitmapOptions.outWidth
                        val imageH = bitmapOptions.outHeight
                        val viewH = imageH * viewW / imageW
                        val layoutParams = iv.layoutParams
                        layoutParams.height = viewH
                        layoutParams.width = viewW
                        iv.layoutParams = layoutParams
                    }
//                    val viewH = Tools.dp2px(context,160f)
//                    if (imageH < viewH || imageW < viewW) {
//                        //其中一边或双边小于控件宽高
//
//                        Glide.with(context).load(picList[i].uri).error(R.mipmap.icon_origin).crossFade().dontTransform().into(iv)
//                    } else {
//                        //双边均大于等于宽高
//                        if ((imageW / viewW) >= (imageH / viewH)) {
//                            val rBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picList[i].uri), imageW * viewH / imageH, viewH, true)
////                            Glide.with(context).load(rBitmap).error(R.mipmap.icon_origin).crossFade().into(iv)
//                            iv.setImageBitmap(rBitmap)
//
//                        }else{
//                            val rBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picList[i].uri), viewW, imageH * viewW / imageW, true)
////                            Glide.with(context).load(rBitmap).error(R.mipmap.icon_origin).crossFade().into(iv)
//                            iv.setImageBitmap(rBitmap)
//
//                        }
//                    }
                    Glide.with(context).load(picList[i].uri).error(R.mipmap.icon_origin).crossFade().dontTransform().into(iv)
                }
            }
            parentView.addView(inflate)
        }
    }
}
