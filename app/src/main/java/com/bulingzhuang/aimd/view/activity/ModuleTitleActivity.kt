package com.bulingzhuang.aimd.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.transition.Fade
import android.transition.TransitionManager
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView

import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVFile
import com.avos.avoscloud.LogUtil
import com.avos.avoscloud.SaveCallback
import com.bulingzhuang.aimd.R
import com.bulingzhuang.aimd.entity.BaseModuleEntity
import com.bulingzhuang.aimd.entity.ModuleTitleEntity
import com.bulingzhuang.aimd.utils.Constants
import com.bulingzhuang.aimd.utils.SelectPopUtil
import com.bulingzhuang.aimd.utils.Tools
import com.bumptech.glide.Glide
import com.google.gson.Gson

import org.simple.eventbus.EventBus

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException

import kotlinx.android.synthetic.main.activity_module_title.*

class ModuleTitleActivity : AppCompatActivity(), View.OnClickListener {

    private var mModuleTitleEntity: ModuleTitleEntity? = null//数据实体类
    private val mStep_1 = 1
    private val mStep_2 = 2//页面状态，1、2
    private var mCurrentStep: Int = 0//当前页面进度
    private var mUploadImageUri: Uri? = null//待上传图片本地Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_title)
        EventBus.getDefault().register(this)
        setViewsOnClickListener(btn_step_1, btn_step_2, btn_submit, btn_del, rl_step_1_alignment_l, rl_step_1_alignment_c, rl_step_1_alignment_r, 
                tv_step_1_show_mask, tv_step_1_hidden_mask, tv_step_1_center_crop, tv_step_1_center_inside, tv_add_image, btn_step_1_next)
        Tools.changeFont(tv_step_1, tv_step_2, tv_title, et_title, tv_step_1_alignment, tv_step_1_mask, tv_step_1_show_mask,
                tv_step_1_hidden_mask, tv_step_1_scale, tv_step_1_center_crop, tv_step_1_center_inside, tv_add_image)
        mModuleTitleEntity = ModuleTitleEntity("", "", ModuleTitleEntity.Alignment_l, ModuleTitleEntity.ScaleType_Crop, true, BaseModuleEntity.ModuleType.TITLE)
        et_title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                tv_title.text = s.toString()
            }
        })
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
            R.id.btn_step_1//第一步
            -> changeStep(mStep_1)
            R.id.btn_step_2//第二步
            -> changeStep(mStep_2)
            R.id.btn_submit//提交
            -> submit()
            R.id.btn_del//后退
            -> onBackPressed()
            R.id.rl_step_1_alignment_l//对齐方式选择左对齐
            -> changeTextAlignment(ModuleTitleEntity.Alignment_l)
            R.id.rl_step_1_alignment_c//对齐方式选择居中
            -> changeTextAlignment(ModuleTitleEntity.Alignment_c)
            R.id.rl_step_1_alignment_r//对齐方式选择右对齐
            -> changeTextAlignment(ModuleTitleEntity.Alignment_r)
            R.id.tv_step_1_show_mask//显示遮罩
            -> showMask(true)
            R.id.tv_step_1_hidden_mask//不显示遮罩
            -> showMask(false)
            R.id.tv_step_1_center_crop//图片格式适应屏幕
            -> changeScaleType(ModuleTitleEntity.ScaleType_Crop)
            R.id.tv_step_1_center_inside//图片格式居中
            -> changeScaleType(ModuleTitleEntity.ScaleType_Inside)
            R.id.tv_add_image//添加照片
            -> chooseTitleIcon()
            R.id.btn_step_1_next//第二步
            -> changeStep(mStep_2)
        }
    }

    /**
     * 提交数据
     */
    private fun submit() {
        val gson = Gson()
        val jsonData = gson.toJson(mModuleTitleEntity)
        EventBus.getDefault().post(jsonData, "update_module_title")
        onBackPressed()
    }

    /**
     * 选择上传头像所用图片
     */
    private fun chooseTitleIcon() {
        val popWindow = SelectPopUtil(this)
        popWindow.showPopupWindow("拍照", "相册选择")
        popWindow.setListener(object : SelectPopUtil.SelectPopListener {
            override fun send(flag: String) {
                if (flag == "0") { //拍照
                    /* 打开相机拍照 */
                    mUploadImageUri = Uri.fromFile(File(cacheDir, "Aimd_" + System.currentTimeMillis() + ".png"))
                    val intent = Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUploadImageUri)
                    startActivityForResult(intent, Constants.CAMERA_REQUEST_CODE)
                }
                if (flag == "1") { //本地
                    /* 打开相册选择图片 */
                    startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), Constants.IMAGE_REQUEST_CODE)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            Tools.showLogE("requestCode：$requestCode，resultCode：$resultCode")
            iv_title_bg.scaleType = ImageView.ScaleType.CENTER_INSIDE
            when (requestCode) {
                Constants.IMAGE_REQUEST_CODE ->
                    /* 相册里的相片 */
                    saveImage(data.data)

                Constants.CAMERA_REQUEST_CODE ->
                    /* 相机拍摄的相片 */
                    if (Tools.hasSdcard()) {
                        saveImage(mUploadImageUri!!)
                    } else {
                        Tools.showSnackBarDark(this, "未找到存储卡，无法存储照片", ll_activity_module_title)
                    }
            }
        }
    }

    /**
     * 裁剪图片方法实现

     * @param uri
     */
    fun saveImage(uri: Uri) {
        try {
            val `is` = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(`is`)
            val imagePath = Tools.saveBitmap(bitmap, this.cacheDir.toString() + "/moduleTitleImage.jpeg")
            if (!TextUtils.isEmpty(imagePath)) {
                uploadIcon(imagePath, bitmap)
            } else {
                Tools.showSnackBarDark(this, "保存图片失败", ll_activity_module_title)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

    }

    /**
     * 上传图片

     * @param filePath
     * *
     * @param bitmap
     */
    private fun uploadIcon(filePath: String?, bitmap: Bitmap) {
        if (filePath != null) {
            val lastIndexOf = filePath.lastIndexOf(".")
            if (lastIndexOf > 0) {
                val suffix = filePath.substring(lastIndexOf, filePath.length)
                try {
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val bitmapBytes = stream.toByteArray()
                    val avFile = AVFile.withAbsoluteLocalPath("CACHE_" + Tools.getAndroidIMEI(this) + suffix, filePath)
                    avFile.saveInBackground(object : SaveCallback() {
                        override fun done(e: AVException?) {
                            if (e == null && !TextUtils.isEmpty(avFile.url)) {
                                mModuleTitleEntity!!.imageUrl = avFile.url
                                LogUtil.log.e("图片地址：" + avFile.url)
                                disposeImage(bitmapBytes)
                            } else {
                                Tools.showSnackBarDark(this@ModuleTitleActivity, "图片上传失败", ll_activity_module_title)
                            }
                        }
                    })
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

            } else {
                Tools.showSnackBarDark(this, "所选图片地址异常", ll_activity_module_title)
            }
        } else {
            Tools.showSnackBarDark(this, "获取图片地址失败", ll_activity_module_title)
        }
    }

    /**
     * 上传图片成功后状态切换

     * @param bitmapBytes
     */
    private fun disposeImage(bitmapBytes: ByteArray) {
        Glide.with(this@ModuleTitleActivity).load(bitmapBytes).error(R.mipmap.icon_origin)
                .placeholder(R.mipmap.icon_origin).crossFade().into(iv_title_bg)
        when (mModuleTitleEntity!!.scaleType) {
            ModuleTitleEntity.ScaleType_Crop -> iv_title_bg.scaleType = ImageView.ScaleType.CENTER_CROP
            ModuleTitleEntity.ScaleType_Inside -> iv_title_bg.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
        tv_add_image.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
        tv_add_image.setTextColor(ContextCompat.getColor(this, android.R.color.white))
    }

    /**
     * 切换背景图片格式

     * @param scaleType
     */
    private fun changeScaleType(scaleType: Int) {
        mModuleTitleEntity!!.scaleType = scaleType
        when (scaleType) {
            ModuleTitleEntity.ScaleType_Crop -> {
                iv_title_bg.scaleType = ImageView.ScaleType.CENTER_CROP
                tv_step_1_center_crop.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
                tv_step_1_center_crop.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                tv_step_1_center_inside.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
                tv_step_1_center_inside.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
            ModuleTitleEntity.ScaleType_Inside -> {
                iv_title_bg.scaleType = ImageView.ScaleType.CENTER_INSIDE
                tv_step_1_center_crop.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
                tv_step_1_center_crop.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                tv_step_1_center_inside.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
                tv_step_1_center_inside.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            }
        }
    }

    /**
     * 是否显示遮罩

     * @param showMask
     */
    private fun showMask(showMask: Boolean) {
        mModuleTitleEntity!!.isShowMask = showMask
        if (showMask) {//显示
            tv_step_1_show_mask.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            tv_step_1_show_mask.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
            tv_step_1_hidden_mask.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            tv_step_1_hidden_mask.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            tv_title.background = ContextCompat.getDrawable(this, R.drawable.module_title_shadow)
        } else {
            tv_step_1_show_mask.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            tv_step_1_show_mask.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
            tv_step_1_hidden_mask.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            tv_step_1_hidden_mask.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
            tv_title.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        }
    }

    /**
     * 切换对齐方式

     * @param alignment
     */
    private fun changeTextAlignment(alignment: Int) {
        mModuleTitleEntity!!.alignment = alignment
        when (alignment) {
            ModuleTitleEntity.Alignment_l -> {
                iv_step_1_alignment_l.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_left_c))
                rl_step_1_alignment_l.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
                iv_step_1_alignment_c.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_center))
                rl_step_1_alignment_c.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
                iv_step_1_alignment_r.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_right))
                rl_step_1_alignment_r.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
                tv_title.gravity = Gravity.START or Gravity.BOTTOM
            }
            ModuleTitleEntity.Alignment_c -> {
                iv_step_1_alignment_l.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_left))
                rl_step_1_alignment_l.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
                iv_step_1_alignment_c.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_center_c))
                rl_step_1_alignment_c.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
                iv_step_1_alignment_r.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_right))
                rl_step_1_alignment_r.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
                tv_title.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
            }
            ModuleTitleEntity.Alignment_r -> {
                iv_step_1_alignment_l.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_left))
                rl_step_1_alignment_l.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
                iv_step_1_alignment_c.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_center))
                rl_step_1_alignment_c.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t)
                iv_step_1_alignment_r.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_right_c))
                rl_step_1_alignment_r.background = ContextCompat.getDrawable(this, R.drawable.bg_module_t_c)
                tv_title.gravity = Gravity.END or Gravity.BOTTOM
            }
        }
    }

    /**
     * 切换页面

     * @param step
     */
    private fun changeStep(step: Int) {
        if (mCurrentStep != step) {
            val titleStr = et_title.text.toString()
            if (!TextUtils.isEmpty(titleStr)) {
                mModuleTitleEntity!!.title = titleStr
                tv_title.text = titleStr
            }
            mCurrentStep = step
            hideSoftInput()
            TransitionManager.beginDelayedTransition(ll_content, Fade())
            when (step) {
                mStep_1 -> {
                    btn_step_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_f))
                    tv_step_1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    btn_step_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s))
                    tv_step_2.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                    ll_step_2.visibility = View.VISIBLE
                    btn_del.visibility = View.VISIBLE
                    btn_submit.visibility = View.GONE
                }
                mStep_2 -> {
                    btn_step_1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s))
                    tv_step_1.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                    btn_step_2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_f))
                    tv_step_2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    ll_step_2.visibility = View.INVISIBLE
                    btn_del.visibility = View.GONE
                    btn_submit.visibility = View.VISIBLE
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
