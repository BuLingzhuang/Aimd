package com.bulingzhuang.aimd.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.SaveCallback;
import com.bulingzhuang.aimd.R;
import com.bulingzhuang.aimd.entity.BaseModuleEntity;
import com.bulingzhuang.aimd.entity.ModuleTitleEntity;
import com.bulingzhuang.aimd.utils.Constants;
import com.bulingzhuang.aimd.utils.SelectPopUtil;
import com.bulingzhuang.aimd.utils.Tools;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.simple.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ModuleTitleActivity extends AppCompatActivity {

    @Bind(R.id.btn_step_1)
    ImageView btnStep1;
    @Bind(R.id.tv_step_1)
    TextView tvStep1;
    @Bind(R.id.btn_step_2)
    ImageView btnStep2;
    @Bind(R.id.tv_step_2)
    TextView tvStep2;
    @Bind(R.id.btn_submit)
    ImageView btnSubmit;
    @Bind(R.id.btn_del)
    ImageView btnDel;
    @Bind(R.id.rl_toolbar)
    RelativeLayout rlToolbar;
    @Bind(R.id.iv_title_bg)
    ImageView ivTitleBg;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.tv_step_1_alignment)
    TextView tvStep1Alignment;
    @Bind(R.id.iv_step_1_alignment_l)
    ImageView ivStep1AlignmentL;
    @Bind(R.id.rl_step_1_alignment_l)
    RelativeLayout rlStep1AlignmentL;
    @Bind(R.id.iv_step_1_alignment_c)
    ImageView ivStep1AlignmentC;
    @Bind(R.id.rl_step_1_alignment_c)
    RelativeLayout rlStep1AlignmentC;
    @Bind(R.id.iv_step_1_alignment_r)
    ImageView ivStep1AlignmentR;
    @Bind(R.id.rl_step_1_alignment_r)
    RelativeLayout rlStep1AlignmentR;
    @Bind(R.id.tv_step_1_mask)
    TextView tvStep1Mask;
    @Bind(R.id.tv_step_1_show_mask)
    TextView tvStep1ShowMask;
    @Bind(R.id.tv_step_1_hidden_mask)
    TextView tvStep1HiddenMask;
    @Bind(R.id.tv_step_1_scale)
    TextView tvStep1Scale;
    @Bind(R.id.tv_step_1_center_crop)
    TextView tvStep1CenterCrop;
    @Bind(R.id.tv_step_1_center_inside)
    TextView tvStep1CenterInside;
    @Bind(R.id.tv_add_image)
    TextView tvAddImage;
    @Bind(R.id.ll_step_2)
    LinearLayout llStep2;
    @Bind(R.id.ll_activity_module_title)
    RelativeLayout llActivityModuleTitle;
    @Bind(R.id.btn_step_1_next)
    ImageView btnStep1Next;
    @Bind(R.id.ll_content)
    LinearLayout llContent;

    private ModuleTitleEntity mModuleTitleEntity;//数据实体类
    private final int mStep_1 = 1, mStep_2 = 2;//页面状态，1、2
    private int mCurrentStep;//当前页面进度
    private Uri mUploadImageUri;//待上传图片本地Uri
    private String mNetImageUri;//网络图片Uri

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_title);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Tools.changeFont(tvStep1, tvStep2, tvTitle, etTitle, tvStep1Alignment, tvStep1Mask, tvStep1ShowMask,
                tvStep1HiddenMask, tvStep1Scale, tvStep1CenterCrop, tvStep1CenterInside, tvAddImage);
        mModuleTitleEntity = new ModuleTitleEntity("", "", ModuleTitleEntity.Alignment_l, ModuleTitleEntity.ScaleType_Crop, true, BaseModuleEntity.ModuleType.TITLE);
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvTitle.setText(s.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @OnClick({R.id.btn_step_1, R.id.btn_step_2, R.id.btn_submit, R.id.btn_del, R.id.rl_step_1_alignment_l, R.id.rl_step_1_alignment_c, R.id.rl_step_1_alignment_r, R.id.tv_step_1_show_mask, R.id.tv_step_1_hidden_mask, R.id.tv_step_1_center_crop, R.id.tv_step_1_center_inside, R.id.tv_add_image, R.id.btn_step_1_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_step_1://第一步
                changeStep(mStep_1);
                break;
            case R.id.btn_step_2://第二步
                changeStep(mStep_2);
                break;
            case R.id.btn_submit://提交
                submit();
                break;
            case R.id.btn_del://后退
                onBackPressed();
                break;
            case R.id.rl_step_1_alignment_l://对齐方式选择左对齐
                changeTextAlignment(ModuleTitleEntity.Alignment_l);
                break;
            case R.id.rl_step_1_alignment_c://对齐方式选择居中
                changeTextAlignment(ModuleTitleEntity.Alignment_c);
                break;
            case R.id.rl_step_1_alignment_r://对齐方式选择右对齐
                changeTextAlignment(ModuleTitleEntity.Alignment_r);
                break;
            case R.id.tv_step_1_show_mask://显示遮罩
                showMask(true);
                break;
            case R.id.tv_step_1_hidden_mask://不显示遮罩
                showMask(false);
                break;
            case R.id.tv_step_1_center_crop://图片格式适应屏幕
                changeScaleType(ModuleTitleEntity.ScaleType_Crop);
                break;
            case R.id.tv_step_1_center_inside://图片格式居中
                changeScaleType(ModuleTitleEntity.ScaleType_Inside);
                break;
            case R.id.tv_add_image://添加照片
                chooseTitleIcon();
                break;
            case R.id.btn_step_1_next://第二步
                changeStep(mStep_2);
                break;
        }
    }

    /**
     * 提交数据
     */
    private void submit() {
        Gson gson = new Gson();
        String jsonData = gson.toJson(mModuleTitleEntity);
        EventBus.getDefault().post(jsonData, "update_module_title");
        onBackPressed();
    }

    /**
     * 选择上传头像所用图片
     */
    private void chooseTitleIcon() {
        SelectPopUtil popWindow = new SelectPopUtil(this);
        popWindow.showPopupWindow("拍照", "相册选择");
        popWindow.setListener(flag -> {
            if (flag.equals("0")) { //拍照
                /* 打开相机拍照 */
                mUploadImageUri = Uri.fromFile(new File(getCacheDir(), "Aimd_" + System.currentTimeMillis() + ".png"));
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
            ivTitleBg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            switch (requestCode) {
                case Constants.IMAGE_REQUEST_CODE:
                    /* 相册里的相片 */
                    saveImage(data.getData());
                    break;

                case Constants.CAMERA_REQUEST_CODE:
                    /* 相机拍摄的相片 */
                    if (Tools.hasSdcard()) {
                        saveImage(mUploadImageUri);
                    } else {
                        Tools.showSnackBarDark(this, "未找到存储卡，无法存储照片", llActivityModuleTitle);
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
    public void saveImage(Uri uri) {
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            String imagePath = Tools.saveBitmap(bitmap, this.getCacheDir() + "/moduleTitleImage.jpeg");
            if (!TextUtils.isEmpty(imagePath)) {
                uploadIcon(imagePath, bitmap);
            } else {
                Tools.showSnackBarDark(this, "保存图片失败", llActivityModuleTitle);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传图片
     *
     * @param filePath
     * @param bitmap
     */
    private void uploadIcon(String filePath, Bitmap bitmap) {
        if (filePath != null) {
            int lastIndexOf = filePath.lastIndexOf(".");
            if (lastIndexOf > 0) {
                String suffix = filePath.substring(lastIndexOf, filePath.length());
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    final byte[] bitmapBytes = stream.toByteArray();
                    final AVFile avFile = AVFile.withAbsoluteLocalPath("CACHE_" + Tools.getAndroidIMEI(this) + suffix, filePath);
                    avFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null && !TextUtils.isEmpty(avFile.getUrl())) {
                                mModuleTitleEntity.setImageUrl(avFile.getUrl());
                                LogUtil.log.e("图片地址：" + avFile.getUrl());
                                disposeImage(bitmapBytes);
                            } else {
                                Tools.showSnackBarDark(ModuleTitleActivity.this, "图片上传失败", llActivityModuleTitle);
                            }
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Tools.showSnackBarDark(this, "所选图片地址异常", llActivityModuleTitle);
            }
        } else {
            Tools.showSnackBarDark(this, "获取图片地址失败", llActivityModuleTitle);
        }
    }

    /**
     * 上传图片成功后状态切换
     *
     * @param bitmapBytes
     */
    private void disposeImage(byte[] bitmapBytes) {
        Glide.with(ModuleTitleActivity.this).load(bitmapBytes).error(R.mipmap.icon_origin)
                .placeholder(R.mipmap.icon_origin).crossFade().into(ivTitleBg);
        switch (mModuleTitleEntity.getScaleType()) {
            case ModuleTitleEntity.ScaleType_Crop:
                ivTitleBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case ModuleTitleEntity.ScaleType_Inside:
                ivTitleBg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
        }
        tvAddImage.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
        tvAddImage.setTextColor(ContextCompat.getColor(this, android.R.color.white));
    }

    /**
     * 切换背景图片格式
     *
     * @param scaleType
     */
    private void changeScaleType(int scaleType) {
        mModuleTitleEntity.setScaleType(scaleType);
        switch (scaleType) {
            case ModuleTitleEntity.ScaleType_Crop:
                ivTitleBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                tvStep1CenterCrop.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                tvStep1CenterCrop.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                tvStep1CenterInside.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                tvStep1CenterInside.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                break;
            case ModuleTitleEntity.ScaleType_Inside:
                ivTitleBg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                tvStep1CenterCrop.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                tvStep1CenterCrop.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                tvStep1CenterInside.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                tvStep1CenterInside.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                break;
        }
    }

    /**
     * 是否显示遮罩
     *
     * @param showMask
     */
    private void showMask(boolean showMask) {
        mModuleTitleEntity.setShowMask(showMask);
        if (showMask) {//显示
            tvStep1ShowMask.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            tvStep1ShowMask.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
            tvStep1HiddenMask.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvStep1HiddenMask.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
            tvTitle.setBackground(ContextCompat.getDrawable(this, R.drawable.module_title_shadow));
        } else {
            tvStep1ShowMask.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvStep1ShowMask.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
            tvStep1HiddenMask.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            tvStep1HiddenMask.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
            tvTitle.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }

    /**
     * 切换对齐方式
     *
     * @param alignment
     */
    private void changeTextAlignment(int alignment) {
        mModuleTitleEntity.setAlignment(alignment);
        switch (alignment) {
            case ModuleTitleEntity.Alignment_l:
                ivStep1AlignmentL.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_left_c));
                rlStep1AlignmentL.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                ivStep1AlignmentC.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_center));
                rlStep1AlignmentC.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                ivStep1AlignmentR.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_right));
                rlStep1AlignmentR.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                tvTitle.setGravity(Gravity.START | Gravity.BOTTOM);
                break;
            case ModuleTitleEntity.Alignment_c:
                ivStep1AlignmentL.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_left));
                rlStep1AlignmentL.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                ivStep1AlignmentC.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_center_c));
                rlStep1AlignmentC.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                ivStep1AlignmentR.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_right));
                rlStep1AlignmentR.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                tvTitle.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                break;
            case ModuleTitleEntity.Alignment_r:
                ivStep1AlignmentL.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_left));
                rlStep1AlignmentL.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                ivStep1AlignmentC.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_center));
                rlStep1AlignmentC.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                ivStep1AlignmentR.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_right_c));
                rlStep1AlignmentR.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                tvTitle.setGravity(Gravity.END | Gravity.BOTTOM);
                break;
        }
    }

    /**
     * 切换页面
     *
     * @param step
     */
    private void changeStep(int step) {
        if (mCurrentStep != step) {
            String titleStr = etTitle.getText().toString();
            if (!TextUtils.isEmpty(titleStr)) {
                mModuleTitleEntity.setTitle(titleStr);
                tvTitle.setText(titleStr);
            }
            mCurrentStep = step;
            hideSoftInput();
            TransitionManager.beginDelayedTransition(llContent, new Fade());
            switch (step) {
                case mStep_1:
                    btnStep1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_f));
                    tvStep1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    btnStep2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s));
                    tvStep2.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    llStep2.setVisibility(View.VISIBLE);
                    btnDel.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.GONE);
                    break;
                case mStep_2:
                    btnStep1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s));
                    tvStep1.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    btnStep2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_f));
                    tvStep2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    llStep2.setVisibility(View.INVISIBLE);
                    btnDel.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    /**
     * 点击后退判断是否回到上一步
     *
     * @param paramInt
     * @param paramKeyEvent
     * @return
     */
    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (KeyEvent.KEYCODE_BACK == paramInt) {
            if (mCurrentStep > mStep_1) {
                changeStep(mCurrentStep - 1);
                return true;
            }
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
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
}
