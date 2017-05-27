package com.bulingzhuang.aimd.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils

import com.bulingzhuang.aimd.R
import com.bulingzhuang.aimd.utils.Tools

import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask

import kotlinx.android.synthetic.main.activity_module_type.*

class ModuleTypeActivity : AppCompatActivity(), View.OnClickListener {

    private var mTimer: Timer? = null
    private var animCounter: Int = 0

    private var mContentArray: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_type)
        setViewsOnClickListener(cv_m_title, cv_m_text, cv_m_image, cv_m_mic, cv_m_location, iv_del)
        Tools.changeFont(tv_title, tv_m_title, tv_m_text, tv_m_image, tv_m_mic, tv_m_location)
        val intent = intent
        val showTitleModule = intent.getBooleanExtra("showTitleModule", false)
        if (showTitleModule) {
            animCounter = 0
            cv_m_title.visibility = View.INVISIBLE
        } else {
            animCounter = 1
            cv_m_title.visibility = View.GONE
        }
        mContentArray = intent.getStringArrayListExtra("contentArray")
        startEnterAnim()
    }

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.arg1 == 2333) {
                //                Tools.showLogE("计数器：" + animCounter);
                when (animCounter) {
                    0 -> {
                        val animation4 = AnimationUtils.loadAnimation(this@ModuleTypeActivity, R.anim.module_type_item_translate)
                        cv_m_title.startAnimation(animation4)
                        cv_m_title.visibility = View.VISIBLE
                    }
                    1 -> {
                        val animation0 = AnimationUtils.loadAnimation(this@ModuleTypeActivity, R.anim.module_type_item_translate)
                        cv_m_text.startAnimation(animation0)
                        cv_m_text.visibility = View.VISIBLE
                    }
                    2 -> {
                        val animation1 = AnimationUtils.loadAnimation(this@ModuleTypeActivity, R.anim.module_type_item_translate)
                        cv_m_image.startAnimation(animation1)
                        cv_m_image.visibility = View.VISIBLE
                    }
                    3 -> {
                        val animation2 = AnimationUtils.loadAnimation(this@ModuleTypeActivity, R.anim.module_type_item_translate)
                        cv_m_mic.startAnimation(animation2)
                        cv_m_mic.visibility = View.VISIBLE
                    }
                    4 -> {
                        val animation3 = AnimationUtils.loadAnimation(this@ModuleTypeActivity, R.anim.module_type_item_translate)
                        cv_m_location.startAnimation(animation3)
                        cv_m_location.visibility = View.VISIBLE
                        mTimer!!.cancel()
                    }
                }
                ++animCounter
            }
        }
    }

    private fun startEnterAnim() {
        val timerTask = object : TimerTask() {
            override fun run() {
                val message = Message()
                message.arg1 = 2333
                mHandler.sendMessage(message)
            }
        }
        mTimer = Timer()
        mTimer!!.schedule(timerTask, 233, 233)
    }

    private fun setViewsOnClickListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        val intent: Intent
        when (v?.id) {
            R.id.cv_m_title -> {
                intent = Intent(this, ModuleTitleActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.cv_m_text -> {
                intent = Intent(this, ModuleTextActivity::class.java)
                intent.putStringArrayListExtra("contentArray", mContentArray)
                startActivity(intent)
                finish()
            }
            R.id.cv_m_image -> {
                intent = Intent(this, ModuleImageActivity::class.java)
                intent.putStringArrayListExtra("contentArray", mContentArray)
                startActivity(intent)
                finish()
            }
            R.id.cv_m_mic -> {
            }
            R.id.cv_m_location -> {
            }
            R.id.iv_del -> onBackPressed()
        }
    }
}
