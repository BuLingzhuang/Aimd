package com.bulingzhuang.aimd.view.activity

import android.graphics.Canvas
import android.os.Bundle
import android.support.transition.Fade
import android.support.transition.TransitionManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.KeyEvent
import android.view.View

import com.bulingzhuang.aimd.R
import com.bulingzhuang.aimd.entity.BaseModuleEntity
import com.bulingzhuang.aimd.entity.ModuleImageEntity
import com.bulingzhuang.aimd.entity.ModuleTextEntity
import com.bulingzhuang.aimd.entity.ModuleTitleEntity
import com.bulingzhuang.aimd.utils.Tools
import com.bulingzhuang.aimd.utils.UploadModuleUtil
import com.bulingzhuang.aimd.view.adapter.ModuleImagePicAdapter
import com.bulingzhuang.aimd.view.adapter.ModuleImageTypeAdapter
import com.google.gson.Gson
import com.luck.picture.lib.model.FunctionConfig
import com.luck.picture.lib.model.FunctionOptions
import com.luck.picture.lib.model.PictureConfig
import com.yalantis.ucrop.entity.LocalMedia

import org.simple.eventbus.EventBus

import java.util.ArrayList

import kotlinx.android.synthetic.main.activity_module_image.*

class ModuleImageActivity : AppCompatActivity(), View.OnClickListener {

    private var mPicResultCallback: PictureConfig.OnSelectResultCallback? = null
    private var mPicAdapter: ModuleImagePicAdapter? = null
    private var mFunctionOptions: FunctionOptions? = null
    private val mStep_1 = 1
    private val mStep_2 = 2//页面状态，1、2
    private var mCurrentStep: Int = 0//当前页面进度
    private var mTypeAdapter: ModuleImageTypeAdapter? = null
    private var mModuleImageEntity: ModuleImageEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_image)
        EventBus.getDefault().register(this)
        setViewsOnClickListener(rl_add_image, btn_step_1, btn_step_2, btn_submit, btn_del, btn_next, btn_last)
        init()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private fun init() {

        val intent = intent
        val contentArray = intent.getStringArrayListExtra("contentArray")
        setUponContent(contentArray)
        mModuleImageEntity = ModuleImageEntity(BaseModuleEntity.ModuleType.IMAGE)

        //初始化图片选择器
        mFunctionOptions = FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE)
                .setCompress(false)
                .setMaxSelectNum(9)
                .setSelectMode(FunctionConfig.MODE_MULTIPLE)
                .setCustomQQ_theme(R.drawable.checkbox_num_selector_default)
                .setCheckNumMode(true)
                .setShowCamera(true)
                .setEnableCrop(false)
                .setCheckNumMode(true)
                .setPreviewColor(R.color.colorPrimaryDark)
                .setCompleteColor(R.color.colorPrimaryDark)
                .create()
        mPicResultCallback = object : PictureConfig.OnSelectResultCallback {
            override fun onSelectSuccess(list: List<LocalMedia>) {
                mPicAdapter?.addAll(list)
            }

            override fun onSelectSuccess(localMedia: LocalMedia) {

            }
        }

        //设置RecyclerView上下左右滑动事件
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.START or ItemTouchHelper.END) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val movePosition = viewHolder.adapterPosition
                val targetPosition = target.adapterPosition
                mPicAdapter!!.moveItem(movePosition, targetPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                mPicAdapter!!.removeItem(position)
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                when (actionState) {
                    ItemTouchHelper.ACTION_STATE_IDLE//闲置状态
                    -> {
                    }
                    ItemTouchHelper.ACTION_STATE_DRAG//拖动状态
                    -> {
                    }
                    ItemTouchHelper.ACTION_STATE_SWIPE//滑动状态
                    -> {
                        //滑动时改变Item的透明度
                        var alpha = Math.abs(dX) / viewHolder.itemView.width.toFloat() * 2f
                        if (alpha > 1) {
                            alpha = 1f
                        }
                        val v = viewHolder.itemView.findViewById(R.id.v)
                        v.alpha = alpha
                    }
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(rv_image)

        //初始化两个Adapter
        mPicAdapter = ModuleImagePicAdapter(this, object : ModuleImagePicAdapter.Callback {
            override fun notifyChange(itemCount: Int) {
                TransitionManager.beginDelayedTransition(ll_activity_module_image, Fade())
                if (itemCount > 0) {
                    ll_bottom_tips.visibility = View.GONE
                } else {
                    ll_bottom_tips.visibility = View.VISIBLE
                }
                btn_next.visibility = View.GONE
                btn_last.visibility = View.GONE
                mModuleImageEntity!!.type = -1
                mTypeAdapter!!.changeList(itemCount)
            }
        })
        rv_image.adapter = mPicAdapter

        mTypeAdapter = ModuleImageTypeAdapter(this, object : ModuleImageTypeAdapter.Callback {
            override fun checkedType(type: Int) {
                TransitionManager.beginDelayedTransition(ll_activity_module_image, Fade())
                btn_next.visibility = View.VISIBLE
                mModuleImageEntity!!.type = type
            }
        })
        rv_content.adapter = mTypeAdapter
    }

    private fun setViewsOnClickListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rl_add_image//添加图片
            -> {
                val picMaxSize = 9 - mPicAdapter!!.itemCount
                if (picMaxSize > 0) {
                    mFunctionOptions!!.maxSelectNum = picMaxSize
                    PictureConfig.getInstance().init(mFunctionOptions).openPhoto(this, mPicResultCallback)
                } else {
                    Tools.showSnackBarDark(this, "最多选择9张图片", ll_activity_module_image)
                }
            }
            R.id.btn_step_1//第一步
            -> changeStep(mStep_1)
            R.id.btn_step_2//第二步
            -> changeStep(mStep_2)
            R.id.btn_submit//提交
            -> submit()
            R.id.btn_del//关闭
            -> onBackPressed()
            R.id.btn_next//下一步
            -> changeStep(mStep_2)
            R.id.btn_last//上一步
            -> changeStep(mStep_1)
        }
    }

    /**
     * 提交数据
     */
    private fun submit() {
        //TODO 待处理
        val gson = Gson()
        val jsonData = gson.toJson(mModuleImageEntity)
        EventBus.getDefault().post(jsonData, "update_module_image")
        onBackPressed()
    }

    /**
     * 切换页面

     * @param step
     */
    private fun changeStep(step: Int) {
        if (mCurrentStep != step) {
            mCurrentStep = step
            TransitionManager.beginDelayedTransition(ll_activity_module_image, Fade())
            when (step) {
                mStep_1 -> {
                    btn_step_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_f))
                    tv_step_1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    btn_step_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s))
                    tv_step_2.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                    ll_step_1.visibility = View.VISIBLE
                    rl_step_2.visibility = View.GONE
                    btn_del.visibility = View.VISIBLE
                    btn_submit.visibility = View.GONE
                    btn_last.visibility = View.GONE
                    if (mModuleImageEntity?.type != -1) {
                        btn_next.visibility = View.VISIBLE
                    }
                }
                mStep_2 -> {
                    saveLocalPicList(mPicAdapter!!.dataList)
                    btn_step_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s))
                    tv_step_1.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                    btn_step_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_f))
                    tv_step_2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    ll_step_1.visibility = View.GONE
                    rl_step_2.visibility = View.VISIBLE
                    btn_last.visibility = View.VISIBLE
                    if (btn_next.visibility == View.VISIBLE) {
                        btn_next.visibility = View.GONE
                        btn_del.visibility = View.GONE
                        btn_submit.visibility = View.VISIBLE
                        showPreview()
                    }
                }
            }
        }
    }

    /**
     * 处理之前编辑过的模块

     * @param contentArray
     */
    private fun setUponContent(contentArray: ArrayList<String>) {
        val gson = Gson()
        for (i in contentArray.indices) {
            val baseModuleData = gson.fromJson(contentArray[i], BaseModuleEntity::class.java)
            when (baseModuleData.moduleType) {
                BaseModuleEntity.ModuleType.TITLE -> {
                    val titleData = gson.fromJson(contentArray[i], ModuleTitleEntity::class.java)
                    UploadModuleUtil.uploadTitleModule(this, ll_content, titleData)
                }
                BaseModuleEntity.ModuleType.TEXT -> {
                    val textData = gson.fromJson(contentArray[i], ModuleTextEntity::class.java)
                    UploadModuleUtil.uploadTextModule(this, ll_content, textData)
                }
                BaseModuleEntity.ModuleType.IMAGE -> {
                    val imageData = gson.fromJson(contentArray[i], ModuleImageEntity::class.java)
                    UploadModuleUtil.uploadImageModule(this, ll_content, imageData)
                }
                BaseModuleEntity.ModuleType.MIC -> {
                }
                BaseModuleEntity.ModuleType.LOCATION -> {
                }
            }
        }
    }

    /**
     * 展示预览样式
     */
    fun showPreview() {
        ll_image.removeAllViews()
        UploadModuleUtil.uploadImageModule(this, ll_image, mModuleImageEntity!!)
    }

    /**
     * 保存图片列表（地址为本地硬盘地址）

     * @param dataList
     */
    fun saveLocalPicList(dataList: List<LocalMedia>?) {
        if (dataList!!.isNotEmpty()) {
            val localPicList = ArrayList<ModuleImageEntity.PicEntity>()
            for (i in dataList.indices) {
                val filePath = dataList[i].path
                val lastIndexOf = filePath.lastIndexOf(".")
                val suffix = filePath.substring(lastIndexOf, filePath.length)
                localPicList.add(ModuleImageEntity.PicEntity("IMAGE_" + i + suffix, filePath))
            }
            mModuleImageEntity!!.picList = localPicList
        }
    }

    /**
     * 点击后退判断是否回到上一步

     * @param paramInt
     * *
     * @param paramKeyEvent
     * *
     * @return
     */
    override fun onKeyDown(paramInt: Int, paramKeyEvent: KeyEvent): Boolean {
        if (KeyEvent.KEYCODE_BACK == paramInt) {
            if (mCurrentStep > mStep_1) {
                changeStep(mCurrentStep - 1)
                return true
            }
        }
        return super.onKeyDown(paramInt, paramKeyEvent)
    }
}
