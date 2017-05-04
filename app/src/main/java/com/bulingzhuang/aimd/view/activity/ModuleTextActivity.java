package com.bulingzhuang.aimd.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bulingzhuang.aimd.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModuleTextActivity extends AppCompatActivity {

    @Bind(R.id.iv_del)
    ImageView ivDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_text);
        ButterKnife.bind(this);
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
