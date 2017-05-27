package com.bulingzhuang.aimd.entity

/**
 * Created by bulingzhuang
 * on 2017/5/8
 * E-mail:bulingzhuang@foxmail.com
 */

open class BaseModuleEntity {
    enum class ModuleType {
        TITLE, TEXT, IMAGE, MIC, LOCATION
    }

    var moduleType: ModuleType? = null
}
