package com.bulingzhuang.aimd.entity;

/**
 * Created by bulingzhuang
 * on 2017/5/8
 * E-mail:bulingzhuang@foxmail.com
 */

public class BaseModuleEntity {
    public enum ModuleType {
        TITLE, TEXT, IMAGE, MIC, LOCATION
    }

    private ModuleType mModuleType;

    public ModuleType getModuleType() {
        return mModuleType;
    }

    public void setModuleType(ModuleType moduleType) {
        mModuleType = moduleType;
    }
}
