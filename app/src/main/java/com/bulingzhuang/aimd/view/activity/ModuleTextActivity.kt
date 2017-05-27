package com.bulingzhuang.aimd.view.activity

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.transition.Fade
import android.transition.TransitionManager
import android.util.SparseArray
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar

import com.bulingzhuang.aimd.R
import com.bulingzhuang.aimd.base.AimdApplication
import com.bulingzhuang.aimd.entity.BaseModuleEntity
import com.bulingzhuang.aimd.entity.ModuleTextEntity
import com.bulingzhuang.aimd.entity.ModuleTitleEntity
import com.bulingzhuang.aimd.utils.Tools
import com.bulingzhuang.aimd.utils.UploadModuleUtil
import com.google.gson.Gson

import org.simple.eventbus.EventBus

import java.util.ArrayList
import java.util.Locale

import kotlinx.android.synthetic.main.activity_module_text.*

class ModuleTextActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentStep: Int = 0//当前页面进度
    private var mModuleTextEntity: ModuleTextEntity? = null//数据实体类

    private val mStep_1 = 1
    private val mStep_2 = 2
    private val mStep_3 = 3//页面状态，1、2、3
    private var mTypefaceArray: SparseArray<Typeface>? = null//字体格式列表

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_text)
        EventBus.getDefault().register(this)
        setViewsOnClickListener(btn_del, btn_submit, btn_step_1, btn_step_2, btn_step_3, btn_step_1_next, rl_step_2_alignment_l, 
                rl_step_2_alignment_c, rl_step_2_alignment_r, iv_step_2_alignment_tips, tv_step_2_typeface_1, tv_step_2_typeface_2, 
                tv_step_2_typeface_3, tv_step_2_typeface_4, tv_step_2_typeface_5, tv_step_2_typeface_6, tv_step_2_typeface_7, 
                tv_step_2_typeface_8, tv_step_2_typeface_9, btn_step_2_last, btn_step_2_next, tv_step_2_spacing_1, tv_step_2_spacing_2, tv_step_2_spacing_3)
        
        Tools.changeFont(tv_step_1, tv_step_2, tv_step_3, et_content, tv_textCounter, tv_step_2_alignment_k, tv_step_2_size_k,
                tv_step_2_alignment_value, tv_step_2_alignment_sp, tv_step_2_alignment_tips, tv_step_2_other, tv_step_2_typeface_k,
                tv_step_2_typeface_example, tv_step_2_spacing_1, tv_step_2_spacing_2, tv_step_2_spacing_3, tv_step_3_preview)
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
        mModuleTextEntity = ModuleTextEntity("", ModuleTextEntity.Alignment_l, 14, ModuleTextEntity.LineSpacing_1, ModuleTextEntity.Typeface_1, BaseModuleEntity.ModuleType.TEXT)
        et_content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                tv_textCounter.text = "字数：" + s.length
            }
        })

        sb_step_2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                changeTextSize(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        mTypefaceArray = (application as AimdApplication).typefaceArray

        changeStep(mStep_1)
        changeTextSize(6)
        initTypeface()
    }

    private fun setViewsOnClickListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_del//退出
            -> onBackPressed()
            R.id.btn_submit//提交数据
            -> submit()
            R.id.btn_step_1//第一步
            -> changeStep(mStep_1)
            R.id.btn_step_2//第二步
            -> changeStep(mStep_2)
            R.id.btn_step_3//第三步
            -> changeStep(mStep_3)
            R.id.btn_step_1_next//第一步到第二步
            -> changeStep(mCurrentStep + 1)
            R.id.rl_step_2_alignment_l//对齐方式选择左对齐
            -> changeTextAlignment(ModuleTextEntity.Alignment_l)
            R.id.rl_step_2_alignment_c//对齐方式选择居中
            -> changeTextAlignment(ModuleTextEntity.Alignment_c)
            R.id.rl_step_2_alignment_r//对齐方式选择右对齐
            -> changeTextAlignment(ModuleTextEntity.Alignment_r)
            R.id.iv_step_2_alignment_tips//显示提示内容
            -> showStep2Tips()
            R.id.tv_step_2_spacing_1//行间距 x1
            -> changeLineSpacing(ModuleTextEntity.LineSpacing_1)
            R.id.tv_step_2_spacing_2//行间距 x2
            -> changeLineSpacing(ModuleTextEntity.LineSpacing_2)
            R.id.tv_step_2_spacing_3//行间距 x3
            -> changeLineSpacing(ModuleTextEntity.LineSpacing_3)
            R.id.tv_step_2_typeface_1//选择字体1
            -> changeTextTypeface(ModuleTextEntity.Typeface_1)
            R.id.tv_step_2_typeface_2//选择字体2
            -> changeTextTypeface(ModuleTextEntity.Typeface_2)
            R.id.tv_step_2_typeface_3//选择字体3
            -> changeTextTypeface(ModuleTextEntity.Typeface_3)
            R.id.tv_step_2_typeface_4//选择字体4
            -> changeTextTypeface(ModuleTextEntity.Typeface_4)
            R.id.tv_step_2_typeface_5//选择字体5
            -> changeTextTypeface(ModuleTextEntity.Typeface_5)
            R.id.tv_step_2_typeface_6//选择字体6
            -> changeTextTypeface(ModuleTextEntity.Typeface_6)
            R.id.tv_step_2_typeface_7//选择字体7
            -> changeTextTypeface(ModuleTextEntity.Typeface_7)
            R.id.tv_step_2_typeface_8//选择字体8
            -> changeTextTypeface(ModuleTextEntity.Typeface_8)
            R.id.tv_step_2_typeface_9//选择字体9
            -> changeTextTypeface(ModuleTextEntity.Typeface_9)
            R.id.btn_step_2_last//第二步到第一步
            -> changeStep(mCurrentStep - 1)
            R.id.btn_step_2_next//第二步到第三步
            -> changeStep(mCurrentStep + 1)
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
                    UploadModuleUtil.uploadTitleModule(this, ll_step_3_upon_content, titleData)
                }
                BaseModuleEntity.ModuleType.TEXT -> {
                    val textData = gson.fromJson(contentArray[i], ModuleTextEntity::class.java)
                    UploadModuleUtil.uploadTextModule(this, ll_step_3_upon_content, textData)
                }
                BaseModuleEntity.ModuleType.IMAGE -> {
                }
                BaseModuleEntity.ModuleType.MIC -> {
                }
                BaseModuleEntity.ModuleType.LOCATION -> {
                }
            }
        }
    }

    /**
     * 提交数据
     */
    private fun submit() {
        val gson = Gson()
        val jsonData = gson.toJson(mModuleTextEntity)
        EventBus.getDefault().post(jsonData, "update_module_text")
        onBackPressed()
    }

    /**
     * 切换行间距

     * @param lineSpacing
     */
    private fun changeLineSpacing(lineSpacing: Float) {
        mModuleTextEntity!!.lineSpacing = lineSpacing
        tv_step_2_typeface_example.setLineSpacing(0f, lineSpacing)
        et_content.setLineSpacing(0f, lineSpacing)
        tv_step_3_preview.setLineSpacing(0f, lineSpacing)
        if (lineSpacing == ModuleTextEntity.LineSpacing_1) {
            tv_step_2_spacing_1.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            tv_step_2_spacing_1.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
            tv_step_2_spacing_2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            tv_step_2_spacing_2.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            tv_step_2_spacing_3.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            tv_step_2_spacing_3.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
        } else if (lineSpacing == ModuleTextEntity.LineSpacing_2) {
            tv_step_2_spacing_1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            tv_step_2_spacing_1.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            tv_step_2_spacing_2.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            tv_step_2_spacing_2.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
            tv_step_2_spacing_3.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            tv_step_2_spacing_3.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
        } else if (lineSpacing == ModuleTextEntity.LineSpacing_3) {
            tv_step_2_spacing_1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            tv_step_2_spacing_1.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            tv_step_2_spacing_2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            tv_step_2_spacing_2.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            tv_step_2_spacing_3.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            tv_step_2_spacing_3.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
        }
    }

    /**
     * 改变文字大小

     * @param progress
     */
    private fun changeTextSize(progress: Int) {
        val density = Tools.dp2px(this, 1f).toFloat()
        val sizeSp = 8 + progress
        val sizePx = (sizeSp * density).toInt()
        tv_step_2_alignment_value.text = "" + sizeSp
        tv_step_2_alignment_tips.text = String.format(Locale.CHINESE, "（当前设备 Density=%d ，字体长宽为%dpx）", density.toInt(), sizePx)
        mModuleTextEntity!!.textSize = sizeSp
        et_content.textSize = sizeSp.toFloat()
        tv_step_2_typeface_example.textSize = sizeSp.toFloat()
        tv_step_3_preview.textSize = sizeSp.toFloat()
    }

    /**
     * 初始化第二步九种字体格式
     */
    private fun initTypeface() {
        tv_step_2_typeface_1.typeface = mTypefaceArray!!.get(ModuleTextEntity.Typeface_1, AimdApplication.instance!!.customTypeface)
        tv_step_2_typeface_2.typeface = mTypefaceArray!!.get(ModuleTextEntity.Typeface_2, AimdApplication.instance!!.customTypeface)
        tv_step_2_typeface_3.typeface = mTypefaceArray!!.get(ModuleTextEntity.Typeface_3, AimdApplication.instance!!.customTypeface)
        tv_step_2_typeface_4.typeface = mTypefaceArray!!.get(ModuleTextEntity.Typeface_4, AimdApplication.instance!!.customTypeface)
        tv_step_2_typeface_5.typeface = mTypefaceArray!!.get(ModuleTextEntity.Typeface_5, AimdApplication.instance!!.customTypeface)
        tv_step_2_typeface_6.typeface = mTypefaceArray!!.get(ModuleTextEntity.Typeface_6, AimdApplication.instance!!.customTypeface)
        tv_step_2_typeface_7.typeface = mTypefaceArray!!.get(ModuleTextEntity.Typeface_7, AimdApplication.instance!!.customTypeface)
        tv_step_2_typeface_8.typeface = mTypefaceArray!!.get(ModuleTextEntity.Typeface_8, AimdApplication.instance!!.customTypeface)
        tv_step_2_typeface_9.typeface = mTypefaceArray!!.get(ModuleTextEntity.Typeface_9, AimdApplication.instance!!.customTypeface)
    }

    /**
     * 切换字体格式

     * @param typeface
     */
    private fun changeTextTypeface(typeface: Int) {
        changeTextTypefaceBTN(mModuleTextEntity!!.textTypeface, false)
        changeTextTypefaceBTN(typeface, true)
        mModuleTextEntity!!.textTypeface = typeface
        et_content.typeface = mTypefaceArray!!.get(typeface)
        tv_step_2_typeface_example.typeface = mTypefaceArray!!.get(typeface)
        tv_step_3_preview.typeface = mTypefaceArray!!.get(typeface)
    }

    /**
     * 切换字体格式对应按钮的状态

     * @param typeface
     * *
     * @param isCurrent
     */
    private fun changeTextTypefaceBTN(typeface: Int, isCurrent: Boolean) {
        when (typeface) {
            ModuleTextEntity.Typeface_1 -> if (isCurrent) {
                tv_step_2_typeface_1.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                tv_step_2_typeface_1.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
            } else {
                tv_step_2_typeface_1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                tv_step_2_typeface_1.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            }
            ModuleTextEntity.Typeface_2 -> if (isCurrent) {
                tv_step_2_typeface_2.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                tv_step_2_typeface_2.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
            } else {
                tv_step_2_typeface_2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                tv_step_2_typeface_2.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            }
            ModuleTextEntity.Typeface_3 -> if (isCurrent) {
                tv_step_2_typeface_3.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                tv_step_2_typeface_3.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
            } else {
                tv_step_2_typeface_3.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                tv_step_2_typeface_3.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            }
            ModuleTextEntity.Typeface_4 -> if (isCurrent) {
                tv_step_2_typeface_4.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                tv_step_2_typeface_4.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
            } else {
                tv_step_2_typeface_4.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                tv_step_2_typeface_4.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            }
            ModuleTextEntity.Typeface_5 -> if (isCurrent) {
                tv_step_2_typeface_5.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                tv_step_2_typeface_5.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
            } else {
                tv_step_2_typeface_5.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                tv_step_2_typeface_5.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            }
            ModuleTextEntity.Typeface_6 -> if (isCurrent) {
                tv_step_2_typeface_6.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                tv_step_2_typeface_6.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
            } else {
                tv_step_2_typeface_6.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                tv_step_2_typeface_6.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            }
            ModuleTextEntity.Typeface_7 -> if (isCurrent) {
                tv_step_2_typeface_7.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                tv_step_2_typeface_7.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
            } else {
                tv_step_2_typeface_7.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                tv_step_2_typeface_7.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            }
            ModuleTextEntity.Typeface_8 -> if (isCurrent) {
                tv_step_2_typeface_8.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                tv_step_2_typeface_8.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
            } else {
                tv_step_2_typeface_8.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                tv_step_2_typeface_8.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            }
            ModuleTextEntity.Typeface_9 -> if (isCurrent) {
                tv_step_2_typeface_9.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                tv_step_2_typeface_9.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
            } else {
                tv_step_2_typeface_9.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                tv_step_2_typeface_9.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            }
        }
    }


    /**
     * 显示提示信息
     */
    private fun showStep2Tips() {
        TransitionManager.beginDelayedTransition(ll_activity_module_text, Fade())
        iv_step_2_alignment_tips.visibility = View.INVISIBLE
        tv_step_2_alignment_tips.visibility = View.VISIBLE
    }

    /**
     * 切换对齐方式

     * @param alignment
     */
    private fun changeTextAlignment(alignment: Int) {
        mModuleTextEntity!!.alignment = alignment
        when (alignment) {
            ModuleTextEntity.Alignment_l -> {
                iv_step_2_alignment_l.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_left_c))
                rl_step_2_alignment_l.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
                iv_step_2_alignment_c.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_center))
                rl_step_2_alignment_c.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
                iv_step_2_alignment_r.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_right))
                rl_step_2_alignment_r.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
                tv_step_2_typeface_example.gravity = Gravity.START
                et_content.gravity = Gravity.START
                tv_step_3_preview.gravity = Gravity.START
            }
            ModuleTextEntity.Alignment_c -> {
                iv_step_2_alignment_l.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_left))
                rl_step_2_alignment_l.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
                iv_step_2_alignment_c.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_center_c))
                rl_step_2_alignment_c.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
                iv_step_2_alignment_r.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_right))
                rl_step_2_alignment_r.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
                tv_step_2_typeface_example.gravity = Gravity.CENTER_HORIZONTAL
                et_content.gravity = Gravity.CENTER_HORIZONTAL
                tv_step_3_preview.gravity = Gravity.CENTER_HORIZONTAL
            }
            ModuleTextEntity.Alignment_r -> {
                iv_step_2_alignment_l.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_left))
                rl_step_2_alignment_l.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
                iv_step_2_alignment_c.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_center))
                rl_step_2_alignment_c.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
                iv_step_2_alignment_r.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_right_c))
                rl_step_2_alignment_r.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
                tv_step_2_typeface_example.gravity = Gravity.END
                et_content.gravity = Gravity.END
                tv_step_3_preview.gravity = Gravity.END
            }
        }
    }

    /**
     * 切换页面进度

     * @param step
     */
    private fun changeStep(step: Int) {
        if (mCurrentStep != step) {
            val contentStr = et_content.text.toString()
            if (!TextUtils.isEmpty(contentStr)) {
                mModuleTextEntity!!.content = contentStr
                tv_step_3_preview.text = contentStr
            }
            mCurrentStep = step
            hideSoftInput()
            TransitionManager.beginDelayedTransition(ll_activity_module_text, Fade())
            when (step) {
                mStep_1 -> {
                    btn_step_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_f))
                    tv_step_1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    btn_step_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s))
                    tv_step_2.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                    btn_step_3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s))
                    tv_step_3.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                    if (rl_step_1.visibility != View.VISIBLE) {
                        rl_step_1.visibility = View.VISIBLE
                    }
                    if (rl_step_2.visibility != View.GONE) {
                        rl_step_2.visibility = View.GONE
                    }
                    if (rl_step_3.visibility != View.GONE) {
                        rl_step_3.visibility = View.GONE
                    }
                    btn_del.visibility = View.VISIBLE
                    btn_submit.visibility = View.GONE
                }
                mStep_2 -> {
                    btn_step_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s))
                    tv_step_1.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                    btn_step_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_f))
                    tv_step_2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    btn_step_3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s))
                    tv_step_3.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                    if (rl_step_1.visibility != View.GONE) {
                        rl_step_1.visibility = View.GONE
                    }
                    if (rl_step_2.visibility != View.VISIBLE) {
                        rl_step_2.visibility = View.VISIBLE
                    }
                    if (rl_step_3.visibility != View.GONE) {
                        rl_step_3.visibility = View.GONE
                    }
                    btn_submit.visibility = View.GONE
                    btn_del.visibility = View.VISIBLE
                }
                mStep_3 -> {
                    btn_step_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s))
                    tv_step_1.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                    btn_step_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s))
                    tv_step_2.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                    btn_step_3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_f))
                    tv_step_3.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    if (rl_step_1.visibility != View.GONE) {
                        rl_step_1.visibility = View.GONE
                    }
                    if (rl_step_2.visibility != View.GONE) {
                        rl_step_2.visibility = View.GONE
                    }
                    if (rl_step_3.visibility != View.VISIBLE) {
                        rl_step_3.visibility = View.VISIBLE
                    }
                    btn_del.visibility = View.GONE
                    if (TextUtils.isEmpty(tv_step_3_preview.text.toString())) {
                        btn_submit.visibility = View.GONE
                    } else {
                        btn_submit.visibility = View.VISIBLE
                    }
                }
            }
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

    /**
     * 隐藏软键盘
     */
    private fun hideSoftInput() {
        val currentFocus = currentFocus
        if (currentFocus != null) {
            val token = currentFocus.windowToken
            if (token != null) {
                val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                im.hideSoftInputFromWindow(token,
                        InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }
}
