package com.bulingzhuang.aimd;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bulingzhuang.aimd.base.AimdApplication;


public class WelcomeActivity extends AppCompatActivity {

    private ImageView mIvT;
    private ImageView mIvBg0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        mIvBg0 = (ImageView) findViewById(R.id.iv_bg_0);
        mIvT = (ImageView) findViewById(R.id.iv_t);
        startAnim();
    }

    private void startAnim() {
        ((Animatable) mIvT.getDrawable()).start();
        mIvT.setVisibility(View.VISIBLE);
        ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.bg_anim);
        objectAnimator.setTarget(mIvBg0);
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
        mIvBg0.setVisibility(View.VISIBLE);

        new Thread(() -> {
            SparseArray<Typeface> typefaceArray = new SparseArray<>();
            typefaceArray.append(2, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFJinHei_Noncommercial_Regular.ttf"));
            typefaceArray.append(3, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFKeKe_Noncommercial_Regular.ttf"));
            typefaceArray.append(4, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFKeSong_Noncommercial_Regular.ttf"));
            typefaceArray.append(5, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFLangQian_Noncommercial_Regular.ttf"));
            typefaceArray.append(6, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFLangSong_Noncommercial_Regular.ttf"));
            typefaceArray.append(7, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFManYu_Noncommercial_Regular.ttf"));
            typefaceArray.append(8, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFShangHei_Noncommercial_Regular.ttf"));
            typefaceArray.append(9, Typeface.createFromAsset(getApplicationContext().getAssets(), "MFYueYuan_Noncommercial_Regular.ttf"));
            AimdApplication.getInstance().setTypefaceArray(typefaceArray);
        }).run();

    }
}
