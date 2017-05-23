package com.bulingzhuang.aimd.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bulingzhuang
 * on 2017/5/12
 * E-mail:bulingzhuang@foxmail.com
 */

public class ModuleImageEntity extends BaseModuleEntity {
    private int picNum;
    private int type;
    private List<PicEntity> picList;

    public ModuleImageEntity(ModuleType moduleType) {
        type = -1;
        picList = new ArrayList<>();
        super.setModuleType(moduleType);
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<PicEntity> getPicList() {
        return picList;
    }

    public void setPicList(List<PicEntity> picList) {
        this.picList = picList;
    }

    public static class PicEntity {
        private String name;
        private String uri;

        public PicEntity() {
        }

        public PicEntity(String name, String uri) {
            this.name = name;
            this.uri = uri;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }
}
