package com.bulingzhuang.aimd;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WelcomeActivity extends AppCompatActivity {

    @Bind(R.id.iv_bg_0)
    ImageView ivBg0;
    @Bind(R.id.iv_t)
    ImageView ivT;
    @Bind(R.id.activity_welcome)
    RelativeLayout activityWelcome;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        startAnim();
    }

    private void startAnim() {
        ImageView ivT = (ImageView) findViewById(R.id.iv_t);
        ((Animatable) ivT.getDrawable()).start();
        ivT.setVisibility(View.VISIBLE);
        ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.bg_anim);
        objectAnimator.setTarget(ivBg0);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
        ivBg0.setVisibility(View.VISIBLE);
    }
}
