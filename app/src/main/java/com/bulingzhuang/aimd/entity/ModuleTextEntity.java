package com.bulingzhuang.aimd.entity;

/**
 * Created by bulingzhuang
 * on 2017/5/5
 * E-mail:bulingzhuang@foxmail.com
 */

public class ModuleTextEntity {
    private String content;//内容
    private int alignment;//对齐方式 1.左对齐 2.居中 3.右对齐
    private int textSize;//文字大小
    private float lineSpacing;//行间距
    private int textTypeface;//文字字体 1-9

    public ModuleTextEntity() {
    }

    public ModuleTextEntity(String content, int alignment, int textSize, float lineSpacing, int textTypeface) {
        this.content = content;
        this.alignment = alignment;
        this.textSize = textSize;
        this.lineSpacing = lineSpacing;
        this.textTypeface = textTypeface;
    }

    public float getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(float lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextTypeface() {
        return textTypeface;
    }

    public void setTextTypeface(int textTypeface) {
        this.textTypeface = textTypeface;
    }
}
