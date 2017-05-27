package com.bulingzhuang.aimd.entity

/**
 * Created by bulingzhuang
 * on 2017/5/9
 * E-mail:bulingzhuang@foxmail.com
 */

class ModuleTitleEntity(var title: String, var imageUrl: String, var alignment: Int, var scaleType: Int, var isShowMask: Boolean, moduleType: BaseModuleEntity.ModuleType) : BaseModuleEntity() {

    init {
        super.moduleType = moduleType
    }

    companion object {
        //对齐方式
        val Alignment_l = 1
        val Alignment_c = 2
        val Alignment_r = 3
        //图片格式
        val ScaleType_Crop = 1
        val ScaleType_Inside = 2
    }
}
