package com.bulingzhuang.aimd.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView

import com.bulingzhuang.aimd.R

/**
 * Created by bulingzhuang
 * on 16/4/13
 * E-mail:bulingzhuang@foxmail.com
 */
class SelectPopUtil(private val mContext: Context) {

    interface SelectPopListener {
        fun send(flag: String)
    }

    private var listener: SelectPopListener? = null

    fun setListener(listener: SelectPopListener) {
        this.listener = listener
    }

    private var popupWindow: PopupWindow? = null

    /**
     * 弹窗
     */
    fun showPopupWindow(text0: String, text1: String) {
        val mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val menuView = mLayoutInflater.inflate(R.layout.popwindow_sel_pop_list, null, true)
        popupWindow = PopupWindow(menuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true)

        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.isFocusable = true
        //添加淡入淡出动画
        popupWindow!!.animationStyle = R.style.anim_popup_window
        //必须设置
        val win_bg = ContextCompat.getDrawable(mContext, R.drawable.pop_shadow)
        popupWindow!!.setBackgroundDrawable(win_bg)

        //添加pop窗口关闭事件
        popupWindow!!.setOnDismissListener(poponDismissListener())

        popupWindow!!.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        popupWindow!!.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE

        //弹出窗口消失方法
        popupWindow!!.setTouchInterceptor { _, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                popupWindow!!.dismiss()
            }
            false
        }


        val ll_pop_main = menuView.findViewById(R.id.ll_pop_main) as LinearLayout
        val ll_list_1 = menuView.findViewById(R.id.ll_list_1) as LinearLayout
        val list_0 = menuView.findViewById(R.id.tv_list_0) as TextView
        val list_1 = menuView.findViewById(R.id.tv_list_1) as TextView
        val cancel = menuView.findViewById(R.id.tv_cancel) as TextView

        Tools.changeFont(list_0, list_1, cancel)

        list_0.text = text0
        if (TextUtils.isEmpty(text1)) {
            ll_list_1.visibility = View.GONE
        } else {
            ll_list_1.visibility = View.VISIBLE
            list_1.text = text1
        }

        ll_pop_main.setOnClickListener { popupWindow!!.dismiss() }

        list_0.setOnClickListener {
            listener!!.send("0") //0第一条事件  1第二条事件
            popupWindow!!.dismiss()
        }

        list_1.setOnClickListener {
            listener!!.send("1") //0第一条事件  1第二条事件
            popupWindow!!.dismiss()
        }

        cancel.setOnClickListener { popupWindow!!.dismiss() }

        val rootView = ((mContext as Activity).window.decorView.findViewById(android.R.id.content) as ViewGroup).getChildAt(0)
        popupWindow!!.showAtLocation(rootView, Gravity.BOTTOM, 0, 0) //设置窗口显示位置

        popupWindow!!.update()

    }


    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来

     * @author cg
     */
    internal inner class poponDismissListener : PopupWindow.OnDismissListener {

        override fun onDismiss() {

        }

    }
}
