package com.bulingzhuang.aimd.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bulingzhuang.aimd.R
import com.bulingzhuang.aimd.utils.Tools

import kotlinx.android.synthetic.main.activity_module_mic.*
import org.simple.eventbus.EventBus

class ModuleMicActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_mic)
        EventBus.getDefault().register(this)
        setViewsOnClickListener(btn_step_1, btn_submit, btn_del, btn_step_1_start)
        Tools.changeFont(tv_step_1, tv_step_1_length, tv_step_1_tips)
        init()
    }

    private fun init() {
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private fun setViewsOnClickListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_submit -> {

            }
            R.id.btn_del -> {

            }
            R.id.btn_step_1_start -> {

            }
        }
    }
}
