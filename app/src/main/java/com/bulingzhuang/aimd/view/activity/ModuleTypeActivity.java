package com.bulingzhuang.aimd.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bulingzhuang.aimd.R;
import com.bulingzhuang.aimd.utils.Tools;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModuleTypeActivity extends AppCompatActivity {

    @Bind(R.id.iv_del)
    ImageView ivDel;
    @Bind(R.id.ll_parent)
    LinearLayout llParent;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_m_text)
    TextView tvMText;
    @Bind(R.id.cv_m_text)
    CardView cvMText;
    @Bind(R.id.tv_m_image)
    TextView tvMImage;
    @Bind(R.id.cv_m_image)
    CardView cvMImage;
    @Bind(R.id.tv_m_mic)
    TextView tvMMic;
    @Bind(R.id.cv_m_mic)
    CardView cvMMic;
    @Bind(R.id.tv_m_location)
    TextView tvMLocation;
    @Bind(R.id.cv_m_location)
    CardView cvMLocation;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_type);
        ButterKnife.bind(this);
        Tools.changeFont(tvTitle, tvMText, tvMImage, tvMMic, tvMLocation);
        startEnterAnim();
    }

    private int animCounter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 2333) {
//                Tools.showLogE("计数器：" + animCounter);
                switch (animCounter) {
                    case 0:
                        Animation animation0 = AnimationUtils.loadAnimation(ModuleTypeActivity.this, R.anim.module_type_item_translate);
                        cvMText.startAnimation(animation0);
                        cvMText.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        Animation animation1 = AnimationUtils.loadAnimation(ModuleTypeActivity.this, R.anim.module_type_item_translate);
                        cvMImage.startAnimation(animation1);
                        cvMImage.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        Animation animation2 = AnimationUtils.loadAnimation(ModuleTypeActivity.this, R.anim.module_type_item_translate);
                        cvMMic.startAnimation(animation2);
                        cvMMic.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        Animation animation3 = AnimationUtils.loadAnimation(ModuleTypeActivity.this, R.anim.module_type_item_translate);
                        cvMLocation.startAnimation(animation3);
                        cvMLocation.setVisibility(View.VISIBLE);
                        mTimer.cancel();
                        break;
                }
                ++animCounter;
            }
        }
    };

    private void startEnterAnim() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.arg1 = 2333;
                mHandler.sendMessage(message);
            }
        };
        mTimer = new Timer();
        mTimer.schedule(timerTask, 233, 233);
    }

    @OnClick({R.id.cv_m_text, R.id.cv_m_image, R.id.cv_m_mic, R.id.cv_m_location, R.id.iv_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_m_text:
                startActivity(new Intent(this, ModuleTextActivity.class));
                finish();
                break;
            case R.id.cv_m_image:
                break;
            case R.id.cv_m_mic:
                break;
            case R.id.cv_m_location:
                break;
            case R.id.iv_del:
                onBackPressed();
                break;
        }
    }
}
