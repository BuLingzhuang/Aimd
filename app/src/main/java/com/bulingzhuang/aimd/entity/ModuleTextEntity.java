package com.bulingzhuang.aimd.entity;

/**
 * Created by bulingzhuang
 * on 2017/5/5
 * E-mail:bulingzhuang@foxmail.com
 */

public class ModuleTextEntity {

    public static final int Alignment_l = 1, Alignment_c = 2, Alignment_r = 3;//对齐方式
    public static final float LineSpacing_1 = 1.0f, LineSpacing_2 = 1.3f, LineSpacing_3 = 1.6f;//行间距
    public static final int Typeface_1 = 1, Typeface_2 = 2, Typeface_3 = 3, Typeface_4 = 4, Typeface_5 = 5,
            Typeface_6 = 6, Typeface_7 = 7, Typeface_8 = 8, Typeface_9 = 9;//字体格式

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
