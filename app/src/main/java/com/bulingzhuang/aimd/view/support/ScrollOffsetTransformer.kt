package com.bulingzhuang.aimd.view.support

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Created by bulingzhuang
 * on 2017/3/21
 * E-mail:bulingzhuang@foxmail.com
 */

class ScrollOffsetTransformer : ViewPager.PageTransformer {
    /**
     * position参数指明给定页面相对于屏幕中心的位置。它是一个动态属性，会随着页面的滚动而改变。
     * 当一个页面（page)填充整个屏幕时，positoin值为0；
     * 当一个页面（page)刚刚离开屏幕右(左）侧时，position值为1（-1）；
     * 当两个页面分别滚动到一半时，其中一个页面是-0.5，另一个页面是0.5。
     * 基于屏幕上页面的位置，通过诸如setAlpha()、setTranslationX()或setScaleY()方法来设置页面的属性，创建自定义的滑动动画。
     */
    override fun transformPage(page: View, position: Float) {
        //        if (position > 0) {
        //右侧的缓存页往左偏移
        page.translationX = -8f * 24f * position
        //        }
    }
}
