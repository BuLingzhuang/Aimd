package com.bulingzhuang.aimd.view.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.LogUtil;
import com.bulingzhuang.aimd.R;
import com.bulingzhuang.aimd.entity.BaseModuleEntity;
import com.bulingzhuang.aimd.entity.ModuleImageEntity;
import com.bulingzhuang.aimd.entity.ModuleTextEntity;
import com.bulingzhuang.aimd.entity.ModuleTitleEntity;
import com.bulingzhuang.aimd.utils.Tools;
import com.bulingzhuang.aimd.utils.UploadModuleUtil;
import com.bulingzhuang.aimd.view.adapter.ModuleImagePicAdapter;
import com.bulingzhuang.aimd.view.adapter.ModuleImageTypeAdapter;
import com.google.gson.Gson;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModuleImageActivity extends AppCompatActivity {

    @Bind(R.id.rl_add_image)
    RelativeLayout rlAddImage;
    @Bind(R.id.rv_image)
    RecyclerView rvImage;
    @Bind(R.id.ll_bottom_tips)
    LinearLayout llBottomTips;
    @Bind(R.id.ll_activity_module_image)
    RelativeLayout llActivityModuleImage;
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
    @Bind(R.id.btn_next)
    ImageView btnNext;
    @Bind(R.id.btn_last)
    ImageView btnLast;
    @Bind(R.id.rv_content)
    RecyclerView rvContent;
    @Bind(R.id.ll_step_1)
    LinearLayout llStep1;
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    @Bind(R.id.ll_image)
    LinearLayout llImage;
    @Bind(R.id.rl_step_2)
    RelativeLayout rlStep2;

    private PictureConfig.OnSelectResultCallback mPicResultCallback;
    private ModuleImagePicAdapter mPicAdapter;
    private FunctionOptions mFunctionOptions;
    private final int mStep_1 = 1, mStep_2 = 2;//页面状态，1、2
    private int mCurrentStep;//当前页面进度
    private ModuleImageTypeAdapter mTypeAdapter;
    private ModuleImageEntity mModuleImageEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_image);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
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
        mModuleImageEntity = new ModuleImageEntity(BaseModuleEntity.ModuleType.IMAGE);

        //初始化图片选择器
        mFunctionOptions = new FunctionOptions.Builder()
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
                .create();
        mPicResultCallback = new PictureConfig.OnSelectResultCallback() {
            @Override
            public void onSelectSuccess(List<LocalMedia> list) {
                mPicAdapter.addAll(list);
            }

            @Override
            public void onSelectSuccess(LocalMedia localMedia) {

            }
        };

        //设置RecyclerView上下左右滑动事件
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int movePosition = viewHolder.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();
                mPicAdapter.moveItem(movePosition, targetPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mPicAdapter.removeItem(position);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                switch (actionState) {
                    case ItemTouchHelper.ACTION_STATE_IDLE://闲置状态
                        break;
                    case ItemTouchHelper.ACTION_STATE_DRAG://拖动状态
                        break;
                    case ItemTouchHelper.ACTION_STATE_SWIPE://滑动状态
                        //滑动时改变Item的透明度
                        float alpha = Math.abs(dX) / (float) viewHolder.itemView.getWidth() * 2f;
                        if (alpha > 1) {
                            alpha = 1;
                        }
                        View v = viewHolder.itemView.findViewById(R.id.v);
                        v.setAlpha(alpha);
                        break;
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(rvImage);

        //初始化两个Adapter
        mPicAdapter = new ModuleImagePicAdapter(this, itemCount -> {
            TransitionManager.beginDelayedTransition(llActivityModuleImage, new Fade());
            if (itemCount > 0) {
                llBottomTips.setVisibility(View.GONE);
            } else {
                llBottomTips.setVisibility(View.VISIBLE);
            }
            btnNext.setVisibility(View.GONE);
            btnLast.setVisibility(View.GONE);
            mModuleImageEntity.setType(-1);
            mTypeAdapter.changeList(itemCount);
        });
        rvImage.setAdapter(mPicAdapter);

        mTypeAdapter = new ModuleImageTypeAdapter(this, type -> {
            TransitionManager.beginDelayedTransition(llActivityModuleImage, new Fade());
            btnNext.setVisibility(View.VISIBLE);
            mModuleImageEntity.setType(type);
        });
        rvContent.setAdapter(mTypeAdapter);
    }

    @OnClick({R.id.rl_add_image, R.id.btn_step_1, R.id.btn_step_2, R.id.btn_submit, R.id.btn_del, R.id.btn_next, R.id.btn_last})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_add_image://添加图片
                int picMaxSize = 9 - mPicAdapter.getItemCount();
                if (picMaxSize > 0) {
                    mFunctionOptions.setMaxSelectNum(picMaxSize);
                    PictureConfig.getInstance().init(mFunctionOptions).openPhoto(this, mPicResultCallback);
                } else {
                    Tools.showSnackBarDark(this, "最多选择9张图片", llActivityModuleImage);
                }
                break;
            case R.id.btn_step_1://第一步
                changeStep(mStep_1);
                break;
            case R.id.btn_step_2://第二步
                changeStep(mStep_2);
                break;
            case R.id.btn_submit://提交
                break;
            case R.id.btn_del://关闭
                onBackPressed();
                break;
            case R.id.btn_next://下一步
                changeStep(mStep_2);
                break;
            case R.id.btn_last://上一步
                changeStep(mStep_1);
        }
    }

    /**
     * 切换页面
     *
     * @param step
     */
    private void changeStep(int step) {
        if (mCurrentStep != step) {
            mCurrentStep = step;
            TransitionManager.beginDelayedTransition(llActivityModuleImage, new Fade());
            switch (step) {
                case mStep_1:
                    btnStep1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_f));
                    tvStep1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    btnStep2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s));
                    tvStep2.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    llStep1.setVisibility(View.VISIBLE);
                    rlStep2.setVisibility(View.GONE);
                    btnDel.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.GONE);
                    btnLast.setVisibility(View.GONE);
                    if (mModuleImageEntity.getType() != -1) {
                        btnNext.setVisibility(View.VISIBLE);
                    }
                    break;
                case mStep_2:
                    saveLocalPicList(mPicAdapter.getDataList());
                    btnStep1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_s));
                    tvStep1.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    btnStep2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_module_indicator_f));
                    tvStep2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    llStep1.setVisibility(View.GONE);
                    rlStep2.setVisibility(View.VISIBLE);
                    btnLast.setVisibility(View.VISIBLE);
                    if (btnNext.getVisibility() == View.VISIBLE) {
                        btnNext.setVisibility(View.GONE);
                        btnDel.setVisibility(View.GONE);
                        btnSubmit.setVisibility(View.VISIBLE);
                        showPreview();
                    }
                    break;
            }
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
                    UploadModuleUtil.uploadTitleModule(this, llContent, titleData);
                    break;
                case TEXT:
                    ModuleTextEntity textData = gson.fromJson(contentArray.get(i), ModuleTextEntity.class);
                    UploadModuleUtil.uploadTextModule(this, llContent, textData);
                    break;
                case IMAGE:
                    ModuleImageEntity imageData = gson.fromJson(contentArray.get(i), ModuleImageEntity.class);
                    UploadModuleUtil.uploadImageModule(this, llContent, imageData);
                    break;
                case MIC:
                    break;
                case LOCATION:
                    break;
            }
        }
    }

    /**
     * 展示预览样式
     */
    public void showPreview() {
        llImage.removeAllViews();
        UploadModuleUtil.uploadImageModule(this, llImage, mModuleImageEntity);
    }

    /**
     * 保存图片列表（地址为本地硬盘地址）
     *
     * @param dataList
     */
    public void saveLocalPicList(List<LocalMedia> dataList) {
        if (dataList != null && dataList.size() > 0) {
            List<ModuleImageEntity.PicEntity> localPicList = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                String filePath = dataList.get(i).getPath();
                int lastIndexOf = filePath.lastIndexOf(".");
                String suffix = filePath.substring(lastIndexOf, filePath.length());
                localPicList.add(new ModuleImageEntity.PicEntity("IMAGE_" + i + suffix, filePath));
            }
            mModuleImageEntity.setPicList(localPicList);
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
}
