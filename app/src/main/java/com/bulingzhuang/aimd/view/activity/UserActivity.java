package com.bulingzhuang.aimd.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.bulingzhuang.aimd.R;
import com.bulingzhuang.aimd.utils.Constants;
import com.bulingzhuang.aimd.utils.SelectPopUtil;
import com.bulingzhuang.aimd.utils.SharePreferenceUtil;
import com.bulingzhuang.aimd.utils.Tools;
import com.bulingzhuang.aimd.utils.UserRegisterWindowUtil;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class UserActivity extends AppCompatActivity {

    @Bind(R.id.ll_content)
    LinearLayout llContent;
    @Bind(R.id.rl_portrait)
    RelativeLayout rlPortrait;
    @Bind(R.id.civ_portrait)
    ImageView civPortrait;
    @Bind(R.id.iv_portrait_type_bg)
    ImageView ivPortraitTypeBg;
    @Bind(R.id.tv_loading)
    TextView tvLoading;
    @Bind(R.id.iv_portrait_type)
    ImageView ivPortraitType;
    @Bind(R.id.et_login_email)
    EditText etLoginEmail;
    @Bind(R.id.v_login_email)
    View vLoginEmail;
    @Bind(R.id.et_login_password)
    EditText etLoginPassword;
    @Bind(R.id.v_login_password)
    View vLoginPassword;
    @Bind(R.id.ll_login)
    LinearLayout llLogin;
    @Bind(R.id.et_register_nickname)
    EditText etRegisterNickname;
    @Bind(R.id.v_register_nickname)
    View vRegisterNickname;
    @Bind(R.id.et_register_email)
    EditText etRegisterEmail;
    @Bind(R.id.v_register_email)
    View vRegisterEmail;
    @Bind(R.id.et_register_password)
    EditText etRegisterPassword;
    @Bind(R.id.v_register_password)
    View vRegisterPassword;
    @Bind(R.id.et_register_checkPassword)
    EditText etRegisterCheckPassword;
    @Bind(R.id.v_register_checkPassword)
    View vRegisterCheckPassword;
    @Bind(R.id.ll_register)
    LinearLayout llRegister;
    @Bind(R.id.btn_submit)
    TextView btnSubmit;
    @Bind(R.id.tv_pageType)
    TextView tvPageType;
    @Bind(R.id.btn_pageType)
    RelativeLayout btnPageType;
    @Bind(R.id.ll_submit)
    LinearLayout llSubmit;
    @Bind(R.id.tv_user_nickname)
    TextView tvUserNickname;
    @Bind(R.id.btn_tips)
    RelativeLayout btnTips;
    @Bind(R.id.rl_user_nickname)
    RelativeLayout rlUserNickname;
    @Bind(R.id.tv_user_tips)
    TextView tvUserTips;
    @Bind(R.id.btn_user_logout)
    TextView btnUserLogout;
    @Bind(R.id.btn_login_weibo)
    ImageView btnLoginWeibo;
    @Bind(R.id.ll_login_otherTitle)
    LinearLayout llLoginOtherTitle;
    @Bind(R.id.btn_back)
    RelativeLayout btnBack;
    @Bind(R.id.activity_user)
    RelativeLayout activity;

    private boolean mIsRegister;//页面状态值(true注册/false登录)
    private boolean mHasUser;
    private boolean mIsInitial;

    private int mLoginEmailLength;//登录邮箱长度
    private int mLoginPasswordLength;//登录密码长度
    private int mRegisterEmailLength;//注册邮箱长度
    private int mRegisterPasswordLength;//注册密码长度
    private int mRegisterCheckPwdLength;//注册检查密码长度

    private Uri mUploadImageUri;//待上传图片本地Uri
    private String mNetImageUri;//已上传图片网络Uri
    private AVException mException;
    private UserRegisterWindowUtil mRegisterPopupwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        Tools.changeFonts(this);
        init();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void init() {
        mIsInitial = true;
        civPortrait.setFocusable(true);
        civPortrait.setFocusableInTouchMode(true);
        civPortrait.requestFocus();
        civPortrait.requestFocusFromTouch();
        btnSubmit.setEnabled(false);
        changePageType(SharePreferenceUtil.getBooleanDefaultTrue(this, Constants.USER_INIT_TYPE));
        initListeners(etLoginEmail,etLoginPassword,etRegisterNickname,etRegisterEmail,etRegisterPassword,etRegisterCheckPassword);
    }

    private void initData() {
        checkCurrentStatus(true);
    }

    @OnClick({R.id.civ_portrait, R.id.btn_submit, R.id.btn_pageType, R.id.btn_login_weibo, R.id.btn_back, R.id.btn_user_logout, R.id.btn_tips})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.civ_portrait://头像(注册时可上传，登录时不可上传隐藏右上角图标)
                if (mIsRegister && !mHasUser) {
                    chooseUserIcon();
                }
                break;
            case R.id.btn_submit://提交表单(登录或注册)
                submit();
                break;
            case R.id.btn_pageType://跳转注册or登录
                changePageType(!mIsRegister);
                break;
            case R.id.btn_login_weibo://第三方登陆
                Tools.showSnackBarDark(this, "并没写微博登陆", activity);
                break;
            case R.id.btn_back://回退
                finish();
                break;
            case R.id.btn_user_logout://退出登录
                AVUser.logOut();
                if (AVUser.getCurrentUser() == null) {
                    onBackPressed();
                } else {
                    Tools.showSnackBarDark(this, "登出失败(可能是服务器炸了吧)", activity);
                }
                break;
            case R.id.btn_tips://显示隐藏tips
                TransitionManager.beginDelayedTransition(activity, new Fade());
                if (tvUserTips.getVisibility() == View.VISIBLE) {
                    tvUserTips.setVisibility(View.GONE);
                } else {
                    tvUserTips.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /**
     * 根据页面状态(true注册/false登录)，执行相关操作
     */
    private void submit() {
        if (mIsRegister) {//注册
            String nickname = etRegisterNickname.getText().toString().trim();
            if (TextUtils.isEmpty(nickname)) {
                showError("昵称为空");
                return;
            }
            String email = etRegisterEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                showError("账号(邮箱)为空");
                return;
            }
            String password = etRegisterPassword.getText().toString().trim();
            if (TextUtils.isEmpty(password)) {
                showError("密码为空");
                return;
            }
            String checkPassword = etRegisterCheckPassword.getText().toString().trim();
            if (TextUtils.isEmpty(checkPassword)) {
                showError("确认密码为空");
                return;
            }
            if (!password.equals(checkPassword)) {
                showError("两次密码不一致");
                return;
            }
            mRegisterPopupwindow = new UserRegisterWindowUtil(this, activity);
            mRegisterPopupwindow.execute(() -> {
                if (mException == null) {//注册成功
                    SharePreferenceUtil.setValue(UserActivity.this, Constants.USER_INIT_TYPE, false);
                    Tools.showSnackBarDark(UserActivity.this, "注册成功", activity);
                    checkCurrentStatus(false);
                } else {//注册失败，根据code弹对应提示
                    Tools.leanCloudExceptionHadling(UserActivity.this, mException, activity);
                }
                mException = null;
            });
            AVUser avUser = new AVUser();
            avUser.setUsername(email);
            avUser.setPassword(password);
            avUser.put("nickname", nickname);
            avUser.put("portrait", mNetImageUri);

            hideSoftInput();
            avUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(AVException e) {
                    mException = e;
                    mRegisterPopupwindow.finishPopupWindow(mException);
                }
            });
        } else {//登录
            hideSoftInput();
            AVUser.logInInBackground(etLoginEmail.getText().toString(), etLoginPassword.getText().toString(), new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null) {
                        SharePreferenceUtil.setValue(UserActivity.this, Constants.USER_INIT_TYPE, false);
                        checkCurrentStatus(false);
                    } else {
                        Tools.leanCloudExceptionHadling(getApplicationContext(), e, activity);
                    }
                }
            });
        }
    }

    /**
     * 选择上传头像所用图片
     */
    private void chooseUserIcon() {
        SelectPopUtil popWindow = new SelectPopUtil(this);
        popWindow.showPopupWindow("拍照", "相册选择");
        popWindow.setListener(flag -> {
            if (flag.equals("0")) { //拍照
                /* 打开相机拍照 */
                mUploadImageUri = Uri.fromFile(new File(getApplicationContext().getFilesDir(), "Aimd_" + System.currentTimeMillis() + ".jpg"));
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUploadImageUri);
                startActivityForResult(intent, Constants.CAMERA_REQUEST_CODE);
            }
            if (flag.equals("1")) { //本地
                /* 打开相册选择图片 */
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), Constants.IMAGE_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            Tools.showLogE("requestCode：" + requestCode + "，resultCode：" + resultCode);
            switch (requestCode) {
                case Constants.IMAGE_REQUEST_CODE:
                    /* 相册里的相片 */
                    startPhotoZoom(data.getData());
                    break;

                case Constants.CAMERA_REQUEST_CODE:
                    /* 相机拍摄的相片 */
                    if (Tools.hasSdcard()) {
                        startPhotoZoom(mUploadImageUri);
                    } else {
                        Tools.showSnackBarDark(this, "未找到存储卡，无法存储照片", activity);
                    }
                    break;
                case Constants.RESULT_REQUEST_CODE:
                    /* 保存并设置头像 */
                    if (data != null) {
                        saveClippedImage(data);
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("circleCrop", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Constants.RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪图片
     *
     * @param intent
     */
    private void saveClippedImage(Intent intent) {
        Bundle extras = intent.getExtras();

        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            String imagePath = Tools.saveBitmap(photo, this.getFilesDir() + "clippedImage.jpeg");
            if (!TextUtils.isEmpty(imagePath)) {
                uploadIcon(imagePath, photo);
            } else {
                Tools.showSnackBarDark(this, "保存剪裁图片失败", activity);
            }
        }
    }

    /**
     * 上传图片
     *
     * @param filePath
     */
    private void uploadIcon(String filePath, Bitmap bitmap) {
        if (filePath != null) {
            int lastIndexOf = filePath.lastIndexOf(".");
            if (lastIndexOf > 0) {
                String suffix = filePath.substring(lastIndexOf, filePath.length());
                try {
                    ivPortraitType.setVisibility(View.INVISIBLE);
                    tvLoading.setVisibility(View.VISIBLE);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    final byte[] bitmapBytes = stream.toByteArray();
                    final AVFile avFile = AVFile.withAbsoluteLocalPath("ICON_" + Tools.getAndroidIMEI(this) + suffix, filePath);
                    avFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null && !TextUtils.isEmpty(avFile.getUrl())) {
                                ivPortraitType.setImageResource(R.drawable.ic_portrait_true);
                                mNetImageUri = avFile.getUrl();
                                LogUtil.log.e("图片地址：" + mNetImageUri);
                                Glide.with(UserActivity.this).load(bitmapBytes).error(R.mipmap.icon_origin).bitmapTransform(new CropCircleTransformation(UserActivity.this))
                                        .placeholder(R.mipmap.icon_origin).crossFade().into(civPortrait);
                            } else {
                                ivPortraitType.setImageResource(R.drawable.ic_portrait_false);
                                Tools.showSnackBarDark(UserActivity.this, "头像上传失败", activity);
                            }
                            tvLoading.setVisibility(View.INVISIBLE);
                            ivPortraitType.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Tools.showSnackBarDark(this, "所选图片地址异常", activity);
            }

        } else {
            Tools.showSnackBarDark(this, "获取图片地址失败", activity);
        }
    }

    /**
     * 检查当前用户状态(如果有用户缓存，进入已登录页面)
     *
     * @param isCreate
     */
    private void checkCurrentStatus(boolean isCreate) {
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {//代表当前有账号登录中
            if (!isCreate) {
                TransitionManager.beginDelayedTransition(activity, new Fade());
            }
            mHasUser = true;
            ivPortraitType.setVisibility(View.GONE);
            ivPortraitTypeBg.setVisibility(View.GONE);
            llLogin.setVisibility(View.GONE);
            llRegister.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
            btnPageType.setVisibility(View.GONE);
            btnLoginWeibo.setVisibility(View.GONE);
            llLoginOtherTitle.setVisibility(View.GONE);
            rlUserNickname.setVisibility(View.VISIBLE);
            btnUserLogout.setVisibility(View.VISIBLE);

            etLoginEmail.setText("");
            etLoginPassword.setText("");
            String nickname = (String) currentUser.get("nickname");
            if (!TextUtils.isEmpty(nickname)) {
                tvUserNickname.setText(nickname);
            } else {
                tvUserNickname.setText("null");
            }
            String userIcon = (String) currentUser.get("portrait");
            Glide.with(this).load(userIcon).error(R.mipmap.icon_origin).bitmapTransform(new CropCircleTransformation(this)).placeholder(R.mipmap.icon_origin).crossFade().into(civPortrait);
        }
    }

    private void showError(String str) {
        Tools.showSnackBarDark(this, str, activity, Snackbar.LENGTH_LONG);
        hideSoftInput();
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            IBinder token = currentFocus.getWindowToken();
            if (token != null) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(token,
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 改变页面类型(true注册/false登录)
     *
     * @param isRegister
     */
    private void changePageType(boolean isRegister) {
        TransitionManager.beginDelayedTransition(activity, new Fade());
        etLoginEmail.setText("");
        etLoginPassword.setText("");
        etRegisterNickname.setText("");
        etRegisterEmail.setText("");
        etRegisterPassword.setText("");
        etRegisterCheckPassword.setText("");
        int margin = Tools.dp2px(this, 16);
        if (mIsRegister = isRegister) {//注册页面
            if (!mIsInitial) {
                ObjectAnimator.ofFloat(llSubmit, "translationY", -(3 * margin), -(1.5f * margin)).setDuration(233).start();
            }
            ivPortraitType.setVisibility(View.VISIBLE);
            ivPortraitTypeBg.setVisibility(View.GONE);
            btnSubmit.setText("注册");
            llRegister.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);
            tvPageType.setText(getResources().getString(R.string.user_page_type_register));
        } else {//登录页面
            if (!mIsInitial) {
                ObjectAnimator.ofFloat(llSubmit, "translationY", -(1.5f * margin), -(3 * margin)).setDuration(233).start();
            }
            ivPortraitType.setVisibility(View.GONE);
            ivPortraitTypeBg.setVisibility(View.VISIBLE);
            btnSubmit.setText("登录");
            llRegister.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);
            tvPageType.setText(getResources().getString(R.string.user_page_type_login));
        }
        checkCanSubmit();
    }

    /**
     * 检查提交按钮是否应该可以点击
     */
    private void checkCanSubmit() {
        if (mIsRegister) {
            if (mRegisterEmailLength > 0 && mRegisterCheckPwdLength > 5 && mRegisterPasswordLength > 5) {
                btnSubmit.setEnabled(true);
            } else {
                btnSubmit.setEnabled(false);
            }
        } else {
            if (mLoginEmailLength > 0 && mLoginPasswordLength > 5) {
                btnSubmit.setEnabled(true);
            } else {
                btnSubmit.setEnabled(false);
            }
        }
    }

    /**
     * 初始化各种监听器
     */
    private void initListeners(EditText... views) {
        MyFocusChangeListener myFocusChangeListener = new MyFocusChangeListener();
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        MyTouchListener myTouchListener = new MyTouchListener();
        for (EditText view : views) {
            view.setOnFocusChangeListener(myFocusChangeListener);
            view.addTextChangedListener(myTextWatcher);
            view.setOnTouchListener(myTouchListener);
        }
    }

    /**
     * 移动动画
     */
    private void startMoveAnim() {
        int[] rlPortraitScreen = new int[2];
        rlPortrait.getLocationOnScreen(rlPortraitScreen);
//        Tools.showLogE("x="+rlPortraitScreen[0]+",y="+rlPortraitScreen[1]);
        int margin = Tools.dp2px(this, 16);
        int rightValue = rlPortraitScreen[0] - margin * 3 / 4;
        int topValue = rlPortraitScreen[1] - margin;
        ObjectAnimator aPortraitTranslationX = ObjectAnimator.ofFloat(rlPortrait, "translationX", 0f, rightValue).setDuration(233);
        ObjectAnimator aPortraitScaleX = ObjectAnimator.ofFloat(rlPortrait, "scaleX", 1f, 0.666f).setDuration(233);
        ObjectAnimator aPortraitScaleY = ObjectAnimator.ofFloat(rlPortrait, "scaleY", 1f, 0.666f).setDuration(233);
        ObjectAnimator aContentTranslationY = ObjectAnimator.ofFloat(llContent, "translationY", 0f, -topValue).setDuration(233);
        ObjectAnimator aEditTextTranslationY;
        ObjectAnimator aSubmitTranslationY;
        if (mIsRegister) {
            aEditTextTranslationY = ObjectAnimator.ofFloat(llRegister, "translationY", 0f, -margin).setDuration(233);
            aSubmitTranslationY = ObjectAnimator.ofFloat(llSubmit, "translationY", 0f, -(1.5f * margin)).setDuration(233);
        } else {
            aEditTextTranslationY = ObjectAnimator.ofFloat(llLogin, "translationY", 0f, -margin).setDuration(233);
            aSubmitTranslationY = ObjectAnimator.ofFloat(llSubmit, "translationY", 0f, -(3 * margin)).setDuration(233);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(aPortraitTranslationX, aPortraitScaleX, aPortraitScaleY, aContentTranslationY, aEditTextTranslationY, aSubmitTranslationY);
        animatorSet.start();
        mIsInitial = false;
    }


    /**
     * 焦点变化监听
     */
    private class MyFocusChangeListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            View lineView = null;
            switch (v.getId()) {
                case R.id.et_login_email:
                    lineView = vLoginEmail;
                    break;
                case R.id.et_login_password:
                    lineView = vLoginPassword;
                    break;
                case R.id.et_register_email:
                    lineView = vRegisterEmail;
                    break;
                case R.id.et_register_checkPassword:
                    lineView = vRegisterCheckPassword;
                    break;
                case R.id.et_register_password:
                    lineView = vRegisterPassword;
                    break;
                case R.id.et_register_nickname:
                    lineView = vRegisterNickname;
                    break;
            }
            if (lineView != null) {
                if (hasFocus) {//获得焦点
                    lineView.setBackgroundResource(R.color.colorPrimary);
                } else {//失去焦点
                    lineView.setBackgroundResource(R.color.colorAccent);
                }
            }
        }
    }

    /**
     * 文字输入监听
     */
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int length = s.length();
            if (getCurrentFocus() != null) {
                switch (getCurrentFocus().getId()) {
                    case R.id.et_login_email:
                        mLoginEmailLength = length;
                        break;
                    case R.id.et_login_password:
                        mLoginPasswordLength = length;
                        break;
                    case R.id.et_register_email:
                        mRegisterEmailLength = length;
                        break;
                    case R.id.et_register_checkPassword:
                        mRegisterCheckPwdLength = length;
                        break;
                    case R.id.et_register_password:
                        mRegisterPasswordLength = length;
                        break;
                }
            }
            checkCanSubmit();
        }
    }

    /**
     * 触摸监听
     */
    private class MyTouchListener implements View.OnTouchListener{

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_UP:
                    if (mIsInitial) {
                        Tools.showLogE("开始上移动化");
                        startMoveAnim();
                    }
                    break;
            }
            return false;
        }
    }
}
