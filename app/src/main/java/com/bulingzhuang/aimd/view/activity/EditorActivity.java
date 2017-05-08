package com.bulingzhuang.aimd.view.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bulingzhuang.aimd.R;
import com.bulingzhuang.aimd.base.AimdApplication;
import com.bulingzhuang.aimd.entity.ModuleTextEntity;
import com.bulingzhuang.aimd.utils.Tools;
import com.google.gson.Gson;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Tools.changeFont(tvAdd);
        mGson = new Gson();
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
                startActivity(new Intent(this, ModuleTypeActivity.class));
                break;
            case R.id.iv_finish:
                Tools.showSnackBar(this, "完成", llParent);
                break;
        }
    }

    @Subscriber(tag = "update_module_text")
    private void updateModuleText(String jsonData) {
        Tools.showLogE("编辑完成的内容：" + jsonData);
        ModuleTextEntity moduleTextData = mGson.fromJson(jsonData, ModuleTextEntity.class);
        TextView tv = new TextView(this);
        int childCount = llContent.getChildCount();
        int px = Tools.dp2px(this, 16);
        if (childCount == 0) {
            tv.setPadding(px, px, px, px / 2);
        } else {
            tv.setPadding(px, px / 2, px, px / 2);
        }
        tv.setText(moduleTextData.getContent());
        switch (moduleTextData.getAlignment()) {
            case ModuleTextEntity.Alignment_l:
                tv.setGravity(Gravity.START);
                break;
            case ModuleTextEntity.Alignment_c:
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
            case ModuleTextEntity.Alignment_r:
                tv.setGravity(Gravity.END);
                break;
        }
        tv.setTextSize(moduleTextData.getTextSize());
        SparseArray<Typeface> typefaceArray = AimdApplication.getInstance().getTypefaceArray();
        tv.setTypeface(typefaceArray.get(moduleTextData.getTextTypeface()));
        tv.setLineSpacing(0, moduleTextData.getLineSpacing());
        tv.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        llContent.addView(tv);
    }
}
