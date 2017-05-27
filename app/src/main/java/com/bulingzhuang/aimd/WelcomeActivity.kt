package com.bulingzhuang.aimd

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import android.view.View
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_welcome.*;

import com.bulingzhuang.aimd.base.AimdApplication


class WelcomeActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_welcome)
        startAnim()
    }

    private fun startAnim() {
        (iv_t.drawable as Animatable).start()
        iv_t.visibility = View.VISIBLE
        val objectAnimator = AnimatorInflater.loadAnimator(this, R.animator.bg_anim) as ObjectAnimator
        objectAnimator.target = iv_bg_0
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                finish()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        objectAnimator.start()
        iv_bg_0.visibility = View.VISIBLE

        Thread {
            val typefaceArray = SparseArray<Typeface>()
            typefaceArray.append(2, Typeface.createFromAsset(applicationContext.assets, "MFJinHei_Noncommercial_Regular.ttf"))
            typefaceArray.append(3, Typeface.createFromAsset(applicationContext.assets, "MFKeKe_Noncommercial_Regular.ttf"))
            typefaceArray.append(4, Typeface.createFromAsset(applicationContext.assets, "MFKeSong_Noncommercial_Regular.ttf"))
            typefaceArray.append(5, Typeface.createFromAsset(applicationContext.assets, "MFLangQian_Noncommercial_Regular.ttf"))
            typefaceArray.append(6, Typeface.createFromAsset(applicationContext.assets, "MFLangSong_Noncommercial_Regular.ttf"))
            typefaceArray.append(7, Typeface.createFromAsset(applicationContext.assets, "MFManYu_Noncommercial_Regular.ttf"))
            typefaceArray.append(8, Typeface.createFromAsset(applicationContext.assets, "MFShangHei_Noncommercial_Regular.ttf"))
            typefaceArray.append(9, Typeface.createFromAsset(applicationContext.assets, "MFYueYuan_Noncommercial_Regular.ttf"))
            (application as AimdApplication).typefaceArray = typefaceArray
        }.run()

    }
}
