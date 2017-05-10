package com.bulingzhuang.aimd.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bulingzhuang.aimd.R;
import com.bulingzhuang.aimd.entity.ModuleTextEntity;
import com.bulingzhuang.aimd.entity.ModuleTitleEntity;
import com.bulingzhuang.aimd.utils.Tools;
import com.bulingzhuang.aimd.utils.UploadModuleUtil;
import com.google.gson.Gson;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditorActivity extends AppCompatActivity {


    @Bind(R.id.tv_add)
    TextView tvAdd;
    @Bind(R.id.ll_add)
    LinearLayout llAdd;
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    @Bind(R.id.iv_del)
    ImageView ivDel;
    @Bind(R.id.iv_finish)
    ImageView ivFinish;
    @Bind(R.id.ll_parent)
    LinearLayout llParent;
    private Gson mGson;
    private ArrayList<String> mContentArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Tools.changeFont(tvAdd);
        mGson = new Gson();
        mContentArray = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        TransitionManager.beginDelayedTransition(llAdd, new Fade());
        llAdd.setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }

    @OnClick({R.id.iv_del, R.id.ll_add, R.id.iv_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_del:
                onBackPressed();
                break;
            case R.id.ll_add:
                Intent intent = new Intent(this, ModuleTypeActivity.class);
                intent.putStringArrayListExtra("contentArray", mContentArray);
                intent.putExtra("showTitleModule", llContent.getChildCount() == 0);
                startActivity(intent);
                break;
            case R.id.iv_finish:
                Tools.showSnackBar(this, "完成", llParent);
                break;
        }
    }

    @Subscriber(tag = "update_module_text")
    private void updateModuleText(String jsonData) {
        Tools.showLogE("Text编辑完成的内容：" + jsonData);
        ModuleTextEntity moduleTextData = mGson.fromJson(jsonData, ModuleTextEntity.class);
        UploadModuleUtil.uploadTextModule(this, llContent, moduleTextData);
        mContentArray.add(jsonData);
    }

    @Subscriber(tag = "update_module_title")
    private void updateModuleTitle(String jsonData) {
        Tools.showLogE("Title编辑完成的内容：" + jsonData);
        ModuleTitleEntity moduleTitleData = mGson.fromJson(jsonData, ModuleTitleEntity.class);
        UploadModuleUtil.uploadTitleModule(this, llContent, moduleTitleData);
        mContentArray.add(jsonData);
    }
}
