package com.bulingzhuang.aimd.view.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.transition.Fade
import android.transition.TransitionManager
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVFile
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.LogInCallback
import com.avos.avoscloud.LogUtil
import com.avos.avoscloud.SaveCallback
import com.avos.avoscloud.SignUpCallback
import com.bulingzhuang.aimd.R
import com.bulingzhuang.aimd.utils.Constants
import com.bulingzhuang.aimd.utils.SelectPopUtil
import com.bulingzhuang.aimd.utils.SharePreferenceUtil
import com.bulingzhuang.aimd.utils.Tools
import com.bulingzhuang.aimd.utils.UserRegisterWindowUtil
import com.bumptech.glide.Glide

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException

import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity(), View.OnClickListener {

    private var mIsRegister: Boolean = false//页面状态值(true注册/false登录)
    private var mHasUser: Boolean = false
    private var mIsInitial: Boolean = false

    private var mLoginEmailLength: Int = 0//登录邮箱长度
    private var mLoginPasswordLength: Int = 0//登录密码长度
    private var mRegisterEmailLength: Int = 0//注册邮箱长度
    private var mRegisterPasswordLength: Int = 0//注册密码长度
    private var mRegisterCheckPwdLength: Int = 0//注册检查密码长度

    private var mUploadImageUri: Uri? = null//待上传图片本地Uri
    private var mNetImageUri: String? = null//已上传图片网络Uri
    private var mException: AVException? = null
    private var mRegisterPopupWindow: UserRegisterWindowUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setViewsOnClickListener(civ_portrait, btn_submit, btn_pageType, btn_login_weibo, btn_back, btn_user_logout, btn_tips)
        Tools.changeFonts(this)
        init()
        initData()
    }

    private fun init() {
        mIsInitial = true
        civ_portrait.isFocusable = true
        civ_portrait.isFocusableInTouchMode = true
        civ_portrait.requestFocus()
        civ_portrait.requestFocusFromTouch()
        btn_submit.isEnabled = false
        changePageType(SharePreferenceUtil.getBooleanDefaultTrue(this, Constants.USER_INIT_TYPE))
        initListeners(et_login_email, et_login_password, et_register_nickname, et_register_email, et_register_password, et_register_checkPassword)
    }

    private fun initData() {
        checkCurrentStatus(true)
    }

    private fun setViewsOnClickListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.civ_portrait//头像(注册时可上传，登录时不可上传隐藏右上角图标)
            -> if (mIsRegister && !mHasUser) {
                chooseUserIcon()
            }
            R.id.btn_submit//提交表单(登录或注册)
            -> submit()
            R.id.btn_pageType//跳转注册or登录
            -> {
                mIsRegister = !mIsRegister
                Tools.showLogE("isRegister="+mIsRegister)
                changePageType(mIsRegister)
            }
            R.id.btn_login_weibo//第三方登陆
            -> Tools.showSnackBarDark(this, "并没写微博登陆", activity_user)
            R.id.btn_back//回退
            -> finish()
            R.id.btn_user_logout//退出登录
            -> {
                AVUser.logOut()
                if (AVUser.getCurrentUser() == null) {
                    onBackPressed()
                } else {
                    Tools.showSnackBarDark(this, "登出失败(可能是服务器炸了吧)", activity_user)
                }
            }
            R.id.btn_tips//显示隐藏tips
            -> {
                TransitionManager.beginDelayedTransition(activity_user, Fade())
                if (tv_user_tips.visibility == View.VISIBLE) {
                    tv_user_tips.visibility = View.GONE
                } else {
                    tv_user_tips.visibility = View.VISIBLE
                }
            }
        }
    }

    /**
     * 根据页面状态(true注册/false登录)，执行相关操作
     */
    private fun submit() {
        if (mIsRegister) {//注册
            val nickname = et_register_nickname.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(nickname)) {
                showError("昵称为空")
                return
            }
            val email = et_register_email.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(email)) {
                showError("账号(邮箱)为空")
                return
            }
            val password = et_register_password.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(password)) {
                showError("密码为空")
                return
            }
            val checkPassword = et_register_checkPassword.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(checkPassword)) {
                showError("确认密码为空")
                return
            }
            if (password != checkPassword) {
                showError("两次密码不一致")
                return
            }
            mRegisterPopupWindow = UserRegisterWindowUtil(this, activity_user)
            class AnimEndListener : UserRegisterWindowUtil.AnimEndListener {
                override fun end() {
                    if (mException == null) {//注册成功
                        SharePreferenceUtil.setValue(this@UserActivity, Constants.USER_INIT_TYPE, false)
                        Tools.showSnackBarDark(this@UserActivity, "注册成功", activity_user)
                        checkCurrentStatus(false)
                    } else {//注册失败，根据code弹对应提示
                        Tools.leanCloudExceptionHadling(this@UserActivity, mException!!, activity_user)
                    }
                    mException = null
                }
            }
            mRegisterPopupWindow!!.execute(AnimEndListener())
            val avUser = AVUser()
            avUser.username = email
            avUser.setPassword(password)
            avUser.put("nickname", nickname)
            avUser.put("portrait", mNetImageUri)

            hideSoftInput()
            avUser.signUpInBackground(object : SignUpCallback() {
                override fun done(e: AVException) {
                    mException = e
                    mRegisterPopupWindow!!.finishPopupWindow(mException)
                }
            })
        } else {//登录
            hideSoftInput()
            AVUser.logInInBackground(et_login_email.text.toString(), et_login_password.text.toString(), object : LogInCallback<AVUser>() {
                override fun done(avUser: AVUser, e: AVException?) {
                    if (e == null) {
                        SharePreferenceUtil.setValue(this@UserActivity, Constants.USER_INIT_TYPE, false)
                        checkCurrentStatus(false)
                    } else {
                        Tools.leanCloudExceptionHadling(applicationContext, e, activity_user)
                    }
                }
            })
        }
    }

    /**
     * 选择上传头像所用图片
     */
    private fun chooseUserIcon() {
        val popWindow = SelectPopUtil(this)
        popWindow.showPopupWindow("拍照", "相册选择")
        class SelectPopListener : SelectPopUtil.SelectPopListener {
            override fun send(flag: String) {
                if (flag == "0") { //拍照
                    /* 打开相机拍照 */
                    mUploadImageUri = Uri.fromFile(File(applicationContext.filesDir, "Aimd_" + System.currentTimeMillis() + ".jpg"))
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
        }
        popWindow.setListener(SelectPopListener())

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            Tools.showLogE("requestCode：$requestCode，resultCode：$resultCode")
            when (requestCode) {
                Constants.IMAGE_REQUEST_CODE ->
                    /* 相册里的相片 */
                    startPhotoZoom(data!!.data)

                Constants.CAMERA_REQUEST_CODE ->
                    /* 相机拍摄的相片 */
                    if (Tools.hasSdcard()) {
                        startPhotoZoom(mUploadImageUri!!)
                    } else {
                        Tools.showSnackBarDark(this, "未找到存储卡，无法存储照片", activity_user)
                    }
                Constants.RESULT_REQUEST_CODE ->
                    /* 保存并设置头像 */
                    if (data != null) {
                        saveClippedImage(data)
                    }
            }
        }
    }

    /**
     * 裁剪图片方法实现

     * @param uri
     */
    fun startPhotoZoom(uri: Uri) {

        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        // 设置裁剪
        intent.putExtra("crop", "true")
        intent.putExtra("circleCrop", true)
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        intent.putExtra("outputX", 300)
        intent.putExtra("outputY", 300)
        intent.putExtra("return-data", true)
        startActivityForResult(intent, Constants.RESULT_REQUEST_CODE)
    }

    /**
     * 保存裁剪图片

     * @param intent
     */
    private fun saveClippedImage(intent: Intent) {
        val extras = intent.extras

        if (extras != null) {
            val photo = extras.getParcelable<Bitmap>("data")
            val imagePath = Tools.saveBitmap(photo, this.filesDir.toString() + "clippedImage.jpeg")
            if (!TextUtils.isEmpty(imagePath)) {
                uploadIcon(imagePath, photo)
            } else {
                Tools.showSnackBarDark(this, "保存剪裁图片失败", activity_user)
            }
        }
    }

    /**
     * 上传图片

     * @param filePath
     */
    private fun uploadIcon(filePath: String?, bitmap: Bitmap) {
        if (filePath != null) {
            val lastIndexOf = filePath.lastIndexOf(".")
            if (lastIndexOf > 0) {
                val suffix = filePath.substring(lastIndexOf, filePath.length)
                try {
                    iv_portrait_type.visibility = View.INVISIBLE
                    tv_loading.visibility = View.VISIBLE
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val bitmapBytes = stream.toByteArray()
                    val avFile = AVFile.withAbsoluteLocalPath("ICON_" + Tools.getAndroidIMEI(this) + suffix, filePath)
                    avFile.saveInBackground(object : SaveCallback() {
                        override fun done(e: AVException?) {
                            if (e == null && !TextUtils.isEmpty(avFile.url)) {
                                iv_portrait_type.setImageResource(R.drawable.ic_portrait_true)
                                mNetImageUri = avFile.url
                                LogUtil.log.e("图片地址：" + mNetImageUri!!)
                                Glide.with(this@UserActivity).load(bitmapBytes).error(R.mipmap.icon_origin).bitmapTransform(CropCircleTransformation(this@UserActivity))
                                        .placeholder(R.mipmap.icon_origin).crossFade().into(civ_portrait)
                            } else {
                                iv_portrait_type.setImageResource(R.drawable.ic_portrait_false)
                                Tools.showSnackBarDark(this@UserActivity, "头像上传失败", activity_user)
                            }
                            tv_loading.visibility = View.INVISIBLE
                            iv_portrait_type.visibility = View.VISIBLE
                        }
                    })
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

            } else {
                Tools.showSnackBarDark(this, "所选图片地址异常", activity_user)
            }

        } else {
            Tools.showSnackBarDark(this, "获取图片地址失败", activity_user)
        }
    }

    /**
     * 检查当前用户状态(如果有用户缓存，进入已登录页面)

     * @param isCreate
     */
    @SuppressLint("SetTextI18n")
    private fun checkCurrentStatus(isCreate: Boolean) {
        val currentUser = AVUser.getCurrentUser()
        if (currentUser != null) {//代表当前有账号登录中
            if (!isCreate) {
                TransitionManager.beginDelayedTransition(activity_user, Fade())
            }
            mHasUser = true
            iv_portrait_type.visibility = View.GONE
            iv_portrait_type_bg.visibility = View.GONE
            ll_login.visibility = View.GONE
            ll_register.visibility = View.GONE
            btn_submit.visibility = View.GONE
            btn_pageType.visibility = View.GONE
            btn_login_weibo.visibility = View.GONE
            ll_login_otherTitle.visibility = View.GONE
            rl_user_nickname.visibility = View.VISIBLE
            btn_user_logout.visibility = View.VISIBLE

            et_login_email.setText("")
            et_login_password.setText("")
            val nickname = currentUser.get("nickname") as String
            if (!TextUtils.isEmpty(nickname)) {
                tv_user_nickname.text = nickname
            } else {
                tv_user_nickname.text = "null"
            }
            val userIcon = currentUser.get("portrait") as String
            Glide.with(this).load(userIcon).error(R.mipmap.icon_origin).bitmapTransform(CropCircleTransformation(this)).placeholder(R.mipmap.icon_origin).crossFade().into(civ_portrait)
        }
    }

    private fun showError(str: String) {
        Tools.showSnackBarDark(this, str, activity_user, Snackbar.LENGTH_LONG)
        hideSoftInput()
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

    /**
     * 改变页面类型(true注册/false登录)

     * @param isRegister
     */
    private fun changePageType(isRegister: Boolean) {
//        TransitionManager.beginDelayedTransition(activity_user, Fade())
        et_login_email.setText("")
        et_login_password.setText("")
        et_register_nickname.setText("")
        et_register_email.setText("")
        et_register_password.setText("")
        et_register_checkPassword.setText("")
        val margin = Tools.dp2px(this, 16f)
        mIsRegister = isRegister
        if (isRegister) {//注册页面
            if (!mIsInitial) {
                ObjectAnimator.ofFloat(ll_submit, "translationY", -(3f * margin), -(1.5f * margin)).setDuration(233).start()
            }
            iv_portrait_type.visibility = View.VISIBLE
            iv_portrait_type_bg.visibility = View.GONE
            btn_submit.text = "注册"
            ll_register.visibility = View.VISIBLE
            ll_login.visibility = View.GONE
            tv_pageType.text = resources.getString(R.string.user_page_type_register)
        } else {//登录页面
            if (!mIsInitial) {
                ObjectAnimator.ofFloat(ll_submit, "translationY", -(1.5f * margin), -(3f * margin)).setDuration(233).start()
            }
            iv_portrait_type.visibility = View.GONE
            iv_portrait_type_bg.visibility = View.VISIBLE
            btn_submit.text = "登录"
            ll_register.visibility = View.GONE
            ll_login.visibility = View.VISIBLE
            tv_pageType.text = resources.getString(R.string.user_page_type_login)
        }
        checkCanSubmit()
    }

    /**
     * 检查提交按钮是否应该可以点击
     */
    private fun checkCanSubmit() {
        if (mIsRegister) {
            btn_submit.isEnabled = mRegisterEmailLength > 0 && mRegisterCheckPwdLength > 5 && mRegisterPasswordLength > 5
        } else {
            btn_submit.isEnabled = mLoginEmailLength > 0 && mLoginPasswordLength > 5
        }
    }

    /**
     * 初始化各种监听器
     */
    private fun initListeners(vararg views: EditText) {
        val myFocusChangeListener = MyFocusChangeListener()
        val myTextWatcher = MyTextWatcher()
        val myTouchListener = MyTouchListener()
        for (view in views) {
            view.onFocusChangeListener = myFocusChangeListener
            view.addTextChangedListener(myTextWatcher)
            view.setOnTouchListener(myTouchListener)
        }
    }

    /**
     * 移动动画
     */
    private fun startMoveAnim() {
        val rlPortraitScreen = IntArray(2)
        rl_portrait.getLocationOnScreen(rlPortraitScreen)
        //        Tools.showLogE("x="+rlPortraitScreen[0]+",y="+rlPortraitScreen[1]);
        val margin = Tools.dp2px(this, 16f).toFloat()
        val rightValue = rlPortraitScreen[0] - margin * 3 / 4
        val topValue = rlPortraitScreen[1] - margin
        val aPortraitTranslationX = ObjectAnimator.ofFloat(rl_portrait, "translationX", 0f, rightValue).setDuration(233)
        val aPortraitScaleX = ObjectAnimator.ofFloat(rl_portrait, "scaleX", 1f, 0.666f).setDuration(233)
        val aPortraitScaleY = ObjectAnimator.ofFloat(rl_portrait, "scaleY", 1f, 0.666f).setDuration(233)
        val aContentTranslationY = ObjectAnimator.ofFloat(ll_content, "translationY", 0f, -topValue).setDuration(233)
        val aEditTextTranslationY: ObjectAnimator
        val aSubmitTranslationY: ObjectAnimator
        if (mIsRegister) {
            aEditTextTranslationY = ObjectAnimator.ofFloat(ll_register, "translationY", 0f, -margin).setDuration(233)
            aSubmitTranslationY = ObjectAnimator.ofFloat(ll_submit, "translationY", 0f, -(1.5f * margin)).setDuration(233)
        } else {
            aEditTextTranslationY = ObjectAnimator.ofFloat(ll_login, "translationY", 0f, -margin).setDuration(233)
            aSubmitTranslationY = ObjectAnimator.ofFloat(ll_submit, "translationY", 0f, -(3 * margin)).setDuration(233)
        }
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(aPortraitTranslationX, aPortraitScaleX, aPortraitScaleY, aContentTranslationY, aEditTextTranslationY, aSubmitTranslationY)
        animatorSet.start()
        mIsInitial = false
    }


    /**
     * 焦点变化监听
     */
    private inner class MyFocusChangeListener : View.OnFocusChangeListener {

        override fun onFocusChange(v: View, hasFocus: Boolean) {
            var lineView: View? = null
            when (v.id) {
                R.id.et_login_email -> lineView = v_login_email
                R.id.et_login_password -> lineView = v_login_password
                R.id.et_register_email -> lineView = v_register_email
                R.id.et_register_checkPassword -> lineView = v_register_checkPassword
                R.id.et_register_password -> lineView = v_register_password
                R.id.et_register_nickname -> lineView = v_register_nickname
            }
            if (lineView != null) {
                if (hasFocus) {//获得焦点
                    lineView.setBackgroundResource(R.color.colorPrimary)
                } else {//失去焦点
                    lineView.setBackgroundResource(R.color.colorAccent)
                }
            }
        }
    }

    /**
     * 文字输入监听
     */
    private inner class MyTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            val length = s.length
            if (currentFocus != null) {
                when (currentFocus!!.id) {
                    R.id.et_login_email -> mLoginEmailLength = length
                    R.id.et_login_password -> mLoginPasswordLength = length
                    R.id.et_register_email -> mRegisterEmailLength = length
                    R.id.et_register_checkPassword -> mRegisterCheckPwdLength = length
                    R.id.et_register_password -> mRegisterPasswordLength = length
                }
            }
            checkCanSubmit()
        }
    }

    /**
     * 触摸监听
     */
    private inner class MyTouchListener : View.OnTouchListener {

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            val action = event.action
            when (action) {
                MotionEvent.ACTION_UP -> if (mIsInitial) {
                    Tools.showLogE("开始上移动化")
                    startMoveAnim()
                }
            }
            return false
        }
    }
}
