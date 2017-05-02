package com.bulingzhuang.aimd.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bulingzhuang.aimd.R;
import com.bulingzhuang.aimd.utils.Tools;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditorActivity extends AppCompatActivity {

    @Bind(R.id.iv_del)
    ImageView ivDel;
    @Bind(R.id.tv_add)
    TextView tvAdd;
    @Bind(R.id.ll_add)
    LinearLayout llAdd;
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    @Bind(R.id.ll_parent)
    LinearLayout llParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);
        Tools.changeFont(tvAdd);
    }

    @Override
    public void onBackPressed() {
        TransitionManager.beginDelayedTransition(llAdd, new Fade());
        llAdd.setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }

    @OnClick({R.id.iv_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_del:
                onBackPressed();
                break;
        }
    }
}
