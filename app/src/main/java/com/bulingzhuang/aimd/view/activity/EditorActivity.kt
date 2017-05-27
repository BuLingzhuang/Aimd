package com.bulingzhuang.aimd.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Fade
import android.transition.TransitionManager
import android.view.View

import com.bulingzhuang.aimd.R
import com.bulingzhuang.aimd.entity.ModuleTextEntity
import com.bulingzhuang.aimd.entity.ModuleTitleEntity
import com.bulingzhuang.aimd.utils.Tools
import com.bulingzhuang.aimd.utils.UploadModuleUtil
import com.google.gson.Gson

import org.simple.eventbus.EventBus
import org.simple.eventbus.Subscriber

import java.util.ArrayList

import kotlinx.android.synthetic.main.activity_editor.*

class EditorActivity : AppCompatActivity(), View.OnClickListener {

    private var gson: Gson? = null
    private var mContentArray: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        EventBus.getDefault().register(this)
        setViewsOnClickListener(iv_del,ll_add,iv_finish)
        Tools.changeFont(tv_add)
        gson = Gson()
        mContentArray = ArrayList<String>()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onBackPressed() {
        TransitionManager.beginDelayedTransition(ll_add, Fade())
        ll_add.visibility = View.INVISIBLE
        super.onBackPressed()
    }

    private fun setViewsOnClickListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_del -> onBackPressed()
            R.id.ll_add -> {
                val intent = Intent(this, ModuleTypeActivity::class.java)
                intent.putStringArrayListExtra("contentArray", mContentArray)
                intent.putExtra("showTitleModule", ll_content.childCount == 0)
                startActivity(intent)
            }
            R.id.iv_finish -> Tools.showSnackBar(this, "完成", ll_parent)
        }
    }

    @Subscriber(tag = "update_module_text")
    private fun updateModuleText(jsonData: String) {
        Tools.showLogE("Text编辑完成的内容：" + jsonData)
        val moduleTextData = gson!!.fromJson(jsonData, ModuleTextEntity::class.java)
        UploadModuleUtil.uploadTextModule(this, ll_content, moduleTextData)
        mContentArray!!.add(jsonData)
    }

    @Subscriber(tag = "update_module_title")
    private fun updateModuleTitle(jsonData: String) {
        Tools.showLogE("Title编辑完成的内容：" + jsonData)
        val moduleTitleData = gson!!.fromJson(jsonData, ModuleTitleEntity::class.java)
        UploadModuleUtil.uploadTitleModule(this, ll_content, moduleTitleData)
        mContentArray!!.add(jsonData)
    }
}
