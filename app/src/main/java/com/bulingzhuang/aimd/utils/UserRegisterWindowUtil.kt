package com.bulingzhuang.aimd.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Message
import android.transition.Fade
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView

import com.avos.avoscloud.AVException
import com.bulingzhuang.aimd.R

/**
 * Created by bulingzhuang
 * on 2017/3/28
 * E-mail:bulingzhuang@foxmail.com
 */

class UserRegisterWindowUtil(private val mContext: Context, private val mViewGroup: ViewGroup) {

    private var mLine_0: View? = null
    private var mLine_1: View? = null
    private var mLine_2: View? = null
    private var mPencil: ImageView? = null
    private var mPopupWindow: PopupWindow? = null
    private var mTvTips: TextView? = null
    private var mListener: AnimEndListener? = null
    private var mTips = "注册成功"

    interface AnimEndListener {
        fun end()
    }

    private var currentNum: Int = 0
    private var currentNumP: Int = 0
    private var pLines: Int = 0
    private var lines: Int = 0
    private var mScale = 0f
    private var mIsSucceed: Boolean = false
    private var mSet: AnimatorSet? = null

    fun setCurrentNumP(currentNumP: Int) {
        this.currentNumP = currentNumP
    }

    fun setCurrentNum(currentNum: Int) {
        this.currentNum = currentNum
    }

    fun execute(listener: AnimEndListener) {
        mListener = listener
        showPopupWindow()
    }

    fun finishPopupWindow(e: AVException?) {
        mIsSucceed = true
        if (e == null) {
            mTips = "注册成功"
        } else {
            mTips = "注册失败"
        }
    }

    /**
     * 显示上传popupWindow
     */
    private fun showPopupWindow() {

        @SuppressLint("InflateParams") val inflate = LayoutInflater.from(mContext).inflate(R.layout.popup_user_register, null)
        mLine_0 = inflate.findViewById(R.id.line_0)
        mLine_1 = inflate.findViewById(R.id.line_1)
        mLine_2 = inflate.findViewById(R.id.line_2)
        mTvTips = inflate.findViewById(R.id.tv_tips) as TextView
        mPencil = inflate.findViewById(R.id.pencil) as ImageView
        Tools.changeFont(mTvTips!!)

        mPopupWindow = PopupWindow(inflate, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true)
        mPopupWindow!!.isTouchable = true
        mPopupWindow!!.setBackgroundDrawable(ColorDrawable(0x00000000))
        mPopupWindow!!.animationStyle = R.style.popupWindow_sh_anim_style

        mPopupWindow!!.setOnDismissListener {
            mPopupWindow = null
            if (mListener != null) {
                mListener!!.end()
            }
        }
        mPopupWindow!!.isOutsideTouchable = true
        mPopupWindow!!.setTouchInterceptor(View.OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE && !mPopupWindow!!.isFocusable) {
                return@OnTouchListener true
            }
            false
        })

        mPopupWindow!!.showAtLocation(mViewGroup, Gravity.NO_GRAVITY, 0, 0)
        startAnim()
    }

    private fun startAnim() {
        getAnim()
        mSet!!.start()
    }

    private fun getAnim() {
        if (mSet == null) {
            mSet = AnimatorSet()
            val anim = ObjectAnimator.ofInt(this, "currentNum", dp2px(mContext, 50f))
            anim.duration = 1000
            anim.repeatCount = 2
            anim.interpolator = AccelerateDecelerateInterpolator()
            anim.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
                    val value = valueAnimator.getAnimatedValue("currentNum") as Int
                    when (lines) {
                        0 -> changeWidth(value, mLine_0!!)
                        1 -> changeWidth(value, mLine_1!!)
                        2 -> changeWidth(value, mLine_2!!)
                    }

                }

                private fun changeWidth(value: Int, view: View) {
                    val layoutParams = view.layoutParams as RelativeLayout.LayoutParams
                    layoutParams.width = value
                    view.layoutParams = layoutParams
                }
            })
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {}

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {
                    ++lines
                }
            })

            val animP = ObjectAnimator.ofInt(this, "currentNumP", dp2px(mContext, 50f))
            animP.duration = 1000
            animP.repeatCount = 2
            animP.interpolator = AccelerateDecelerateInterpolator()
            animP.addUpdateListener { valueAnimator ->
                val value = valueAnimator.getAnimatedValue("currentNumP") as Int
                val layoutParams = mPencil!!.layoutParams as RelativeLayout.LayoutParams
                layoutParams.leftMargin = dp2px(mContext, 30f) + value
                layoutParams.topMargin = dp2px(mContext, 12f) + pLines * dp2px(mContext, 16f)
                mPencil!!.layoutParams = layoutParams
            }
            animP.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {}

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {
                    ++pLines
                }
            })

            mSet!!.playTogether(anim, animP)
            mSet!!.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    if (mIsSucceed) {
                        TransitionManager.beginDelayedTransition(mViewGroup, Fade())
                        mTvTips!!.text = mTips
                        mPencil!!.visibility = View.GONE
                        Thread(Runnable {
                            try {
                                Thread.sleep(333)
                                val message = Message()
                                message.arg1 = 3222222
                                mHandler.sendMessage(message)
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                        }).start()
                    } else {
                        lines = 0
                        pLines = 0
                        for (i in 0..2) {
                            when (i) {
                                0 -> changeWidth(0, mLine_0!!)
                                1 -> changeWidth(0, mLine_1!!)
                                2 -> changeWidth(0, mLine_2!!)
                            }
                        }
                        val layoutParams = mPencil!!.layoutParams as RelativeLayout.LayoutParams
                        layoutParams.leftMargin = dp2px(mContext, 30f)
                        layoutParams.topMargin = dp2px(mContext, 12f)
                        mPencil!!.layoutParams = layoutParams
                        val message = Message()
                        message.arg1 = 2333333
                        mHandler.sendMessage(message)
                    }
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }

                private fun changeWidth(value: Int, view: View) {
                    val layoutParams = view.layoutParams as RelativeLayout.LayoutParams
                    layoutParams.width = value
                    view.layoutParams = layoutParams
                }
            })
        }
    }

    private fun dp2px(context: Context, dpValue: Float): Int {
        if (mScale == 0f) {
            mScale = context.resources.displayMetrics.density
        }
        return (dpValue * mScale + 0.5f).toInt()
    }

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.arg1) {
                2333333 -> startAnim()
                3222222 -> mPopupWindow!!.dismiss()
            }
        }
    }

}
