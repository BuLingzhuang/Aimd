package com.bulingzhuang.aimd.entity;

/**
 * Created by bulingzhuang
 * on 2017/5/9
 * E-mail:bulingzhuang@foxmail.com
 */

public class ModuleTitleEntity extends BaseModuleEntity {

    public static final int Alignment_l = 1, Alignment_c = 2, Alignment_r = 3;//对齐方式
    public static final int ScaleType_Crop = 1, ScaleType_Inside = 2;//图片格式

    private String title;
    private String imageUrl;
    private int alignment;
    private int scaleType;
    private boolean showMask;

    public ModuleTitleEntity(String title, String imageUrl, int alignment, int scaleType, boolean showMask, ModuleType moduleType) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.alignment = alignment;
        this.scaleType = scaleType;
        this.showMask = showMask;
        super.setModuleType(moduleType);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public int getScaleType() {
        return scaleType;
    }

    public void setScaleType(int scaleType) {
        this.scaleType = scaleType;
    }

    public boolean isShowMask() {
        return showMask;
    }

    public void setShowMask(boolean showMask) {
        this.showMask = showMask;
    }
}
