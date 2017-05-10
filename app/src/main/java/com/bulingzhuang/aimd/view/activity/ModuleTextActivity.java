package com.bulingzhuang.aimd.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bulingzhuang.aimd.R;
import com.bulingzhuang.aimd.base.AimdApplication;
import com.bulingzhuang.aimd.entity.BaseModuleEntity;
import com.bulingzhuang.aimd.entity.ModuleTextEntity;
import com.bulingzhuang.aimd.entity.ModuleTitleEntity;
import com.bulingzhuang.aimd.utils.Tools;
import com.bulingzhuang.aimd.utils.UploadModuleUtil;
import com.google.gson.Gson;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModuleTextActivity extends AppCompatActivity {


    @Bind(R.id.btn_step_1)
    ImageView btnStep1;
    @Bind(R.id.tv_step_1)
    TextView tvStep1;
    @Bind(R.id.btn_step_2)
    ImageView btnStep2;
    @Bind(R.id.tv_step_2)
    TextView tvStep2;
    @Bind(R.id.btn_step_3)
    ImageView btnStep3;
    @Bind(R.id.tv_step_3)
    TextView tvStep3;
    @Bind(R.id.btn_del)
    ImageView btnDel;
    @Bind(R.id.btn_submit)
    ImageView btnSubmit;
    @Bind(R.id.rl_toolbar)
    RelativeLayout rlToolbar;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.tv_textCounter)
    TextView tvTextCounter;
    @Bind(R.id.btn_step_1_next)
    ImageView btnStep1Next;
    @Bind(R.id.rl_step_1)
    RelativeLayout rlStep1;
    @Bind(R.id.rl_step_2)
    RelativeLayout rlStep2;
    @Bind(R.id.ll_activity_module_text)
    RelativeLayout llActivityModuleText;
    @Bind(R.id.tv_step_2_alignment_k)
    TextView tvStep2AlignmentK;
    @Bind(R.id.iv_step_2_alignment_l)
    ImageView ivStep2AlignmentL;
    @Bind(R.id.rl_step_2_alignment_l)
    RelativeLayout rlStep2AlignmentL;
    @Bind(R.id.iv_step_2_alignment_c)
    ImageView ivStep2AlignmentC;
    @Bind(R.id.rl_step_2_alignment_c)
    RelativeLayout rlStep2AlignmentC;
    @Bind(R.id.iv_step_2_alignment_r)
    ImageView ivStep2AlignmentR;
    @Bind(R.id.rl_step_2_alignment_r)
    RelativeLayout rlStep2AlignmentR;
    @Bind(R.id.tv_step_2_size_k)
    TextView tvStep2SizeK;
    @Bind(R.id.iv_step_2_alignment_tips)
    ImageView ivStep2AlignmentTips;
    @Bind(R.id.tv_step_2_alignment_value)
    TextView tvStep2AlignmentValue;
    @Bind(R.id.tv_step_2_alignment_sp)
    TextView tvStep2AlignmentSp;
    @Bind(R.id.tv_step_2_alignment_tips)
    TextView tvStep2AlignmentTips;
    @Bind(R.id.sb_step_2)
    SeekBar sbStep2;
    @Bind(R.id.tv_step_2_other)
    TextView tvStep2Other;
    @Bind(R.id.tv_step_2_typeface_k)
    TextView tvStep2TypefaceK;
    @Bind(R.id.tv_step_2_typeface_1)
    TextView tvStep2Typeface1;
    @Bind(R.id.tv_step_2_typeface_2)
    TextView tvStep2Typeface2;
    @Bind(R.id.tv_step_2_typeface_3)
    TextView tvStep2Typeface3;
    @Bind(R.id.tv_step_2_typeface_4)
    TextView tvStep2Typeface4;
    @Bind(R.id.tv_step_2_typeface_5)
    TextView tvStep2Typeface5;
    @Bind(R.id.tv_step_2_typeface_6)
    TextView tvStep2Typeface6;
    @Bind(R.id.tv_step_2_typeface_7)
    TextView tvStep2Typeface7;
    @Bind(R.id.tv_step_2_typeface_8)
    TextView tvStep2Typeface8;
    @Bind(R.id.tv_step_2_typeface_9)
    TextView tvStep2Typeface9;
    @Bind(R.id.tv_step_2_typeface_example)
    TextView tvStep2TypefaceExample;
    @Bind(R.id.btn_step_2_last)
    ImageView btnStep2Last;
    @Bind(R.id.btn_step_2_next)
    ImageView btnStep2Next;
    @Bind(R.id.tv_step_2_spacing_1)
    TextView tvStep2Spacing1;
    @Bind(R.id.tv_step_2_spacing_2)
    TextView tvStep2Spacing2;
    @Bind(R.id.tv_step_2_spacing_3)
    TextView tvStep2Spacing3;
    @Bind(R.id.ll_step_3_upon_content)
    LinearLayout llStep3UponContent;
    @Bind(R.id.tv_step_3_preview)
    TextView tvStep3Preview;
    @Bind(R.id.rl_step_3)
    RelativeLayout rlStep3;


    private int mCurrentStep;//当前页面进度
    private ModuleTextEntity mModuleTextEntity;//数据实体类

    private final int mStep_1 = 1, mStep_2 = 2, mStep_3 = 3;//页面状态，1、2、3
    private SparseArray<Typeface> mTypefaceArray;//字体格式列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_text);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Tools.changeFont(tvStep1, tvStep2, tvStep3, etContent, tvTextCounter, tvStep2AlignmentK, tvStep2SizeK,
                tvStep2AlignmentValue, tvStep2AlignmentSp, tvStep2AlignmentTips, tvStep2Other, tvStep2TypefaceK,
                tvStep2TypefaceExample, tvStep2Spacing1, tvStep2Spacing2, tvStep2Spacing3, tvStep3Preview);
        init();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    private void init() {
        Intent intent = getIntent();
        ArrayList<String> contentArray = intent.getStringArrayListExtra("contentArray");
        setUponContent(contentArray);
        mModuleTextEntity = new ModuleTextEntity("", ModuleTextEntity.Alignment_l, 14, ModuleTextEntity.LineSpacing_1, ModuleTextEntity.Typeface_1, BaseModuleEntity.ModuleType.TEXT);
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvTextCounter.setText("字数：" + s.length());
            }
        });

        sbStep2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changeTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mTypefaceArray = AimdApplication.getInstance().getTypefaceArray();

        changeStep(mStep_1);
        changeTextSize(6);
        initTypeface();
    }

    @OnClick({R.id.btn_del, R.id.btn_submit, R.id.btn_step_1, R.id.btn_step_2, R.id.btn_step_3, R.id.btn_step_1_next, R.id.rl_step_2_alignment_l, R.id.rl_step_2_alignment_c,
            R.id.rl_step_2_alignment_r, R.id.iv_step_2_alignment_tips, R.id.tv_step_2_typeface_1,
            R.id.tv_step_2_typeface_2, R.id.tv_step_2_typeface_3, R.id.tv_step_2_typeface_4, R.id.tv_step_2_typeface_5, R.id.tv_step_2_typeface_6,
            R.id.tv_step_2_typeface_7, R.id.tv_step_2_typeface_8, R.id.tv_step_2_typeface_9, R.id.btn_step_2_last, R.id.btn_step_2_next,
            R.id.tv_step_2_spacing_1, R.id.tv_step_2_spacing_2, R.id.tv_step_2_spacing_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_del://退出
                onBackPressed();
                break;
            case R.id.btn_submit://提交数据
                submit();
                break;
            case R.id.btn_step_1://第一步
                changeStep(mStep_1);
                break;
            case R.id.btn_step_2://第二步
                changeStep(mStep_2);
                break;
            case R.id.btn_step_3://第三步
                changeStep(mStep_3);
                break;
            case R.id.btn_step_1_next://第一步到第二步
                changeStep(mCurrentStep + 1);
                break;
            case R.id.rl_step_2_alignment_l://对齐方式选择左对齐
                changeTextAlignment(ModuleTextEntity.Alignment_l);
                break;
            case R.id.rl_step_2_alignment_c://对齐方式选择居中
                changeTextAlignment(ModuleTextEntity.Alignment_c);
                break;
            case R.id.rl_step_2_alignment_r://对齐方式选择右对齐
                changeTextAlignment(ModuleTextEntity.Alignment_r);
                break;
            case R.id.iv_step_2_alignment_tips://显示提示内容
                showStep2Tips();
                break;
            case R.id.tv_step_2_spacing_1://行间距 x1
                changeLineSpacing(ModuleTextEntity.LineSpacing_1);
                break;
            case R.id.tv_step_2_spacing_2://行间距 x2
                changeLineSpacing(ModuleTextEntity.LineSpacing_2);
                break;
            case R.id.tv_step_2_spacing_3://行间距 x3
                changeLineSpacing(ModuleTextEntity.LineSpacing_3);
                break;
            case R.id.tv_step_2_typeface_1://选择字体1
                changeTextTypeface(ModuleTextEntity.Typeface_1);
                break;
            case R.id.tv_step_2_typeface_2://选择字体2
                changeTextTypeface(ModuleTextEntity.Typeface_2);
                break;
            case R.id.tv_step_2_typeface_3://选择字体3
                changeTextTypeface(ModuleTextEntity.Typeface_3);
                break;
            case R.id.tv_step_2_typeface_4://选择字体4
                changeTextTypeface(ModuleTextEntity.Typeface_4);
                break;
            case R.id.tv_step_2_typeface_5://选择字体5
                changeTextTypeface(ModuleTextEntity.Typeface_5);
                break;
            case R.id.tv_step_2_typeface_6://选择字体6
                changeTextTypeface(ModuleTextEntity.Typeface_6);
                break;
            case R.id.tv_step_2_typeface_7://选择字体7
                changeTextTypeface(ModuleTextEntity.Typeface_7);
                break;
            case R.id.tv_step_2_typeface_8://选择字体8
                changeTextTypeface(ModuleTextEntity.Typeface_8);
                break;
            case R.id.tv_step_2_typeface_9://选择字体9
                changeTextTypeface(ModuleTextEntity.Typeface_9);
                break;
            case R.id.btn_step_2_last://第二步到第一步
                changeStep(mCurrentStep - 1);
                break;
            case R.id.btn_step_2_next://第二步到第三步
                changeStep(mCurrentStep + 1);
                break;
        }
    }

    /**
     * 处理之前编辑过的模块
     *
     * @param contentArray
     */
    private void setUponContent(ArrayList<String> contentArray) {
        Gson gson = new Gson();
        for (int i = 0; i < contentArray.size(); i++) {
            BaseModuleEntity baseModuleData = gson.fromJson(contentArray.get(i), BaseModuleEntity.class);
            switch (baseModuleData.getModuleType()) {
                case TITLE:
                    ModuleTitleEntity titleData = gson.fromJson(contentArray.get(i), ModuleTitleEntity.class);
                    UploadModuleUtil.uploadTitleModule(this, llStep3UponContent, titleData);
                    break;
                case TEXT:
                    ModuleTextEntity textData = gson.fromJson(contentArray.get(i), ModuleTextEntity.class);
                    UploadModuleUtil.uploadTextModule(this, llStep3UponContent, textData);
                    break;
                case IMAGE:
                    break;
                case MIC:
                    break;
                case LOCATION:
                    break;
            }
        }
    }

    /**
     * 提交数据
     */
    private void submit() {
        Gson gson = new Gson();
        String jsonData = gson.toJson(mModuleTextEntity);
        EventBus.getDefault().post(jsonData, "update_module_text");
        onBackPressed();
    }

    /**
     * 切换行间距
     *
     * @param lineSpacing
     */
    private void changeLineSpacing(float lineSpacing) {
        mModuleTextEntity.setLineSpacing(lineSpacing);
        tvStep2TypefaceExample.setLineSpacing(0, lineSpacing);
        etContent.setLineSpacing(0, lineSpacing);
        tvStep3Preview.setLineSpacing(0, lineSpacing);
        if (lineSpacing == ModuleTextEntity.LineSpacing_1) {
            tvStep2Spacing1.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            tvStep2Spacing1.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
            tvStep2Spacing2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvStep2Spacing2.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
            tvStep2Spacing3.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvStep2Spacing3.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
        } else if (lineSpacing == ModuleTextEntity.LineSpacing_2) {
            tvStep2Spacing1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvStep2Spacing1.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
            tvStep2Spacing2.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            tvStep2Spacing2.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
            tvStep2Spacing3.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvStep2Spacing3.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
        } else if (lineSpacing == ModuleTextEntity.LineSpacing_3) {
            tvStep2Spacing1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvStep2Spacing1.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
            tvStep2Spacing2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvStep2Spacing2.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
            tvStep2Spacing3.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            tvStep2Spacing3.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
        }
    }

    /**
     * 改变文字大小
     *
     * @param progress
     */
    private void changeTextSize(int progress) {
        float density = Tools.dp2px(this, 1);
        int sizeSp = 8 + progress;
        int sizePx = (int) (sizeSp * density);
        tvStep2AlignmentValue.setText("" + sizeSp);
        tvStep2AlignmentTips.setText(String.format(Locale.CHINESE, "（当前设备 Density=%d ，字体长宽为%dpx）", (int) density, sizePx));
        mModuleTextEntity.setTextSize(sizeSp);
        etContent.setTextSize(sizeSp);
        tvStep2TypefaceExample.setTextSize(sizeSp);
        tvStep3Preview.setTextSize(sizeSp);
    }

    /**
     * 初始化第二步九种字体格式
     */
    private void initTypeface() {
        tvStep2Typeface1.setTypeface(mTypefaceArray.get(ModuleTextEntity.Typeface_1, AimdApplication.getInstance().getCustomTypeface()));
        tvStep2Typeface2.setTypeface(mTypefaceArray.get(ModuleTextEntity.Typeface_2, AimdApplication.getInstance().getCustomTypeface()));
        tvStep2Typeface3.setTypeface(mTypefaceArray.get(ModuleTextEntity.Typeface_3, AimdApplication.getInstance().getCustomTypeface()));
        tvStep2Typeface4.setTypeface(mTypefaceArray.get(ModuleTextEntity.Typeface_4, AimdApplication.getInstance().getCustomTypeface()));
        tvStep2Typeface5.setTypeface(mTypefaceArray.get(ModuleTextEntity.Typeface_5, AimdApplication.getInstance().getCustomTypeface()));
        tvStep2Typeface6.setTypeface(mTypefaceArray.get(ModuleTextEntity.Typeface_6, AimdApplication.getInstance().getCustomTypeface()));
        tvStep2Typeface7.setTypeface(mTypefaceArray.get(ModuleTextEntity.Typeface_7, AimdApplication.getInstance().getCustomTypeface()));
        tvStep2Typeface8.setTypeface(mTypefaceArray.get(ModuleTextEntity.Typeface_8, AimdApplication.getInstance().getCustomTypeface()));
        tvStep2Typeface9.setTypeface(mTypefaceArray.get(ModuleTextEntity.Typeface_9, AimdApplication.getInstance().getCustomTypeface()));
    }

    /**
     * 切换字体格式
     *
     * @param typeface
     */
    private void changeTextTypeface(int typeface) {
        changeTextTypefaceBTN(mModuleTextEntity.getTextTypeface(), false);
        changeTextTypefaceBTN(typeface, true);
        mModuleTextEntity.setTextTypeface(typeface);
        etContent.setTypeface(mTypefaceArray.get(typeface));
        tvStep2TypefaceExample.setTypeface(mTypefaceArray.get(typeface));
        tvStep3Preview.setTypeface(mTypefaceArray.get(typeface));
    }

    /**
     * 切换字体格式对应按钮的状态
     *
     * @param typeface
     * @param isCurrent
     */
    private void changeTextTypefaceBTN(int typeface, boolean isCurrent) {
        switch (typeface) {
            case ModuleTextEntity.Typeface_1:
                if (isCurrent) {
                    tvStep2Typeface1.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    tvStep2Typeface1.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                } else {
                    tvStep2Typeface1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvStep2Typeface1.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                }
                break;
            case ModuleTextEntity.Typeface_2:
                if (isCurrent) {
                    tvStep2Typeface2.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    tvStep2Typeface2.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                } else {
                    tvStep2Typeface2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvStep2Typeface2.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                }
                break;
            case ModuleTextEntity.Typeface_3:
                if (isCurrent) {
                    tvStep2Typeface3.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    tvStep2Typeface3.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                } else {
                    tvStep2Typeface3.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvStep2Typeface3.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                }
                break;
            case ModuleTextEntity.Typeface_4:
                if (isCurrent) {
                    tvStep2Typeface4.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    tvStep2Typeface4.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                } else {
                    tvStep2Typeface4.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvStep2Typeface4.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                }
                break;
            case ModuleTextEntity.Typeface_5:
                if (isCurrent) {
                    tvStep2Typeface5.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    tvStep2Typeface5.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                } else {
                    tvStep2Typeface5.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvStep2Typeface5.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                }
                break;
            case ModuleTextEntity.Typeface_6:
                if (isCurrent) {
                    tvStep2Typeface6.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    tvStep2Typeface6.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                } else {
                    tvStep2Typeface6.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvStep2Typeface6.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                }
                break;
            case ModuleTextEntity.Typeface_7:
                if (isCurrent) {
                    tvStep2Typeface7.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    tvStep2Typeface7.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                } else {
                    tvStep2Typeface7.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvStep2Typeface7.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                }
                break;
            case ModuleTextEntity.Typeface_8:
                if (isCurrent) {
                    tvStep2Typeface8.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    tvStep2Typeface8.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                } else {
                    tvStep2Typeface8.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvStep2Typeface8.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                }
                break;
            case ModuleTextEntity.Typeface_9:
                if (isCurrent) {
                    tvStep2Typeface9.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    tvStep2Typeface9.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                } else {
                    tvStep2Typeface9.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    tvStep2Typeface9.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                }
                break;
        }
    }


    /**
     * 显示提示信息
     */
    private void showStep2Tips() {
        TransitionManager.beginDelayedTransition(llActivityModuleText, new Fade());
        ivStep2AlignmentTips.setVisibility(View.INVISIBLE);
        tvStep2AlignmentTips.setVisibility(View.VISIBLE);
    }

    /**
     * 切换对齐方式
     *
     * @param alignment
     */
    private void changeTextAlignment(int alignment) {
        mModuleTextEntity.setAlignment(alignment);
        switch (alignment) {
            case ModuleTextEntity.Alignment_l:
                ivStep2AlignmentL.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_left_c));
                rlStep2AlignmentL.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                ivStep2AlignmentC.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_center));
                rlStep2AlignmentC.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                ivStep2AlignmentR.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_right));
                rlStep2AlignmentR.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                tvStep2TypefaceExample.setGravity(Gravity.START);
                etContent.setGravity(Gravity.START);
                tvStep3Preview.setGravity(Gravity.START);
                break;
            case ModuleTextEntity.Alignment_c:
                ivStep2AlignmentL.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_left));
                rlStep2AlignmentL.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                ivStep2AlignmentC.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_center_c));
                rlStep2AlignmentC.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                ivStep2AlignmentR.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_right));
                rlStep2AlignmentR.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                tvStep2TypefaceExample.setGravity(Gravity.CENTER_HORIZONTAL);
                etContent.setGravity(Gravity.CENTER_HORIZONTAL);
                tvStep3Preview.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
            case ModuleTextEntity.Alignment_r:
                ivStep2AlignmentL.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_left));
                rlStep2AlignmentL.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                ivStep2AlignmentC.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_center));
                rlStep2AlignmentC.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t));
                ivStep2AlignmentR.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_t_right_c));
                rlStep2AlignmentR.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_module_t_c));
                tvStep2TypefaceExample.setGravity(Gravity.END);
                etContent.setGravity(Gravity.END);
                tvStep3Preview.setGravity(Gravity.END);
                break;
        }
    }

    /**
     * 切换页面进度
     *
     * @param step
     */
    private void changeStep(int step) {
        if (mCurrentStep != step) {
            String contentStr = etContent.getText().toString();
            if (!TextUtils.isEmpty(contentStr)) {
                mModuleTextEntity.setContent(contentStr);
                tvStep3Preview.setText(contentStr);
            }
            mCurrentStep = step;
            hideSoftInput();
            TransitionManager.beginDelayedTransition(llActivityModuleText, new Fade());
            switch (step) {
                case mStep_1:
                    btnStep1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_f));
                    tvStep1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    btnStep2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s));
                    tvStep2.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    btnStep3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s));
                    tvStep3.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    if (rlStep1.getVisibility() != View.VISIBLE) {
                        rlStep1.setVisibility(View.VISIBLE);
                    }
                    if (rlStep2.getVisibility() != View.GONE) {
                        rlStep2.setVisibility(View.GONE);
                    }
                    if (rlStep3.getVisibility() != View.GONE) {
                        rlStep3.setVisibility(View.GONE);
                    }
                    btnDel.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.GONE);
                    break;
                case mStep_2:
                    btnStep1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s));
                    tvStep1.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    btnStep2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_f));
                    tvStep2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    btnStep3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s));
                    tvStep3.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    if (rlStep1.getVisibility() != View.GONE) {
                        rlStep1.setVisibility(View.GONE);
                    }
                    if (rlStep2.getVisibility() != View.VISIBLE) {
                        rlStep2.setVisibility(View.VISIBLE);
                    }
                    if (rlStep3.getVisibility() != View.GONE) {
                        rlStep3.setVisibility(View.GONE);
                    }
                    btnSubmit.setVisibility(View.GONE);
                    btnDel.setVisibility(View.VISIBLE);
                    break;
                case mStep_3:
                    btnStep1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s));
                    tvStep1.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    btnStep2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s));
                    tvStep2.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    btnStep3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_f));
                    tvStep3.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    if (rlStep1.getVisibility() != View.GONE) {
                        rlStep1.setVisibility(View.GONE);
                    }
                    if (rlStep2.getVisibility() != View.GONE) {
                        rlStep2.setVisibility(View.GONE);
                    }
                    if (rlStep3.getVisibility() != View.VISIBLE) {
                        rlStep3.setVisibility(View.VISIBLE);
                    }
                    btnDel.setVisibility(View.GONE);
                    if (TextUtils.isEmpty(tvStep3Preview.getText().toString())) {
                        btnSubmit.setVisibility(View.GONE);
                    } else {
                        btnSubmit.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

    /**
     * 点击后退判断是否回到上一步
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
