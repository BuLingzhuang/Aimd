package com.bulingzhuang.aimd.entity

/**
 * Created by bulingzhuang
 * on 2017/5/5
 * E-mail:bulingzhuang@foxmail.com
 */

class ModuleTextEntity(content: String, //对齐方式 1.左对齐 2.居中 3.右对齐
                       var alignment: Int, //文字大小
                       var textSize: Int, //行间距
                       var lineSpacing: Float, //文字字体 1-9
                       var textTypeface: Int, moduleType: BaseModuleEntity.ModuleType) : BaseModuleEntity() {

    var content: String? = content//内容

    companion object {

        val Alignment_l = 1
        val Alignment_c = 2
        val Alignment_r = 3//对齐方式
        val LineSpacing_1 = 1.0f
        val LineSpacing_2 = 1.3f
        val LineSpacing_3 = 1.6f//行间距
        val Typeface_1 = 1
        val Typeface_2 = 2
        val Typeface_3 = 3
        val Typeface_4 = 4
        val Typeface_5 = 5
        val Typeface_6 = 6
        val Typeface_7 = 7
        val Typeface_8 = 8
        val Typeface_9 = 9//字体格式
    }

    init {
        super.moduleType = moduleType
    }
}
