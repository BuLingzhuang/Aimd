package com.bulingzhuang.aimd.entity

import java.util.ArrayList

/**
 * Created by bulingzhuang
 * on 2017/5/12
 * E-mail:bulingzhuang@foxmail.com
 */

class ModuleImageEntity(moduleType: BaseModuleEntity.ModuleType) : BaseModuleEntity() {
    private val picNum: Int = 0
    var type: Int = 0
    var picList: List<PicEntity>? = null

    init {
        type = -1
        picList = ArrayList<PicEntity>()
        super.moduleType = moduleType
    }

    class PicEntity(name: String, uri: String) {
        var name: String? = name
        var uri: String? = uri

    }
}
