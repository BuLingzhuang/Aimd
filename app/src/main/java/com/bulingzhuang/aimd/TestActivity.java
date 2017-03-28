package com.bulingzhuang.aimd;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bulingzhuang.aimd.utils.Tools;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @Bind(R.id.pencil)
    ImageView pencil;
    @Bind(R.id.line_0)
    View line0;
    @Bind(R.id.line_1)
    View line1;
    @Bind(R.id.line_2)
    View line2;
    @Bind(R.id.activity_test)
    RelativeLayout activity;

    private int currentNum;
    private int currentNumP;
    private int pLines;
    private int lines;
    private float mScale = 0f;
    private boolean mIsSucceed;
    private int num;
    private AnimatorSet mSet;

    public void setCurrentNumP(int currentNumP) {
        this.currentNumP = currentNumP;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
//        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_test);
//        List<View> viewList = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            View inflate = LayoutInflater.from(this).inflate(R.layout.item_main_card, null);
//            TextView tvTextView = (TextView) inflate.findViewById(R.id.tv_content);
//            tvTextView.setText("Page-" + i);
//            viewList.add(inflate);
//        }
//
//        viewPager.setPageTransformer(true, new ScrollOffsetTransformer());
//        viewPager.setOffscreenPageLimit(2);
//        viewPager.setAdapter(new AZPagerAdapter(viewList));
    }

    public void btn(View view) {
        getAnim();
        ++num;
        if (num ==3) {
            mIsSucceed = true;
            num = 0;
        }
        mSet.start();
    }

    private void getAnim() {
        if (mSet == null) {
            mSet = new AnimatorSet();
            ObjectAnimator anim = ObjectAnimator.ofInt(this, "currentNum", dp2px(this, 50));
            anim.setDuration(1000);
            anim.setRepeatCount(2);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int value = (int) valueAnimator.getAnimatedValue("currentNum");
                    switch (lines) {
                        case 0:
                            changeWidth(value, line0);
                            break;
                        case 1:
                            changeWidth(value, line1);
                            break;
                        case 2:
                            changeWidth(value, line2);
                            break;
                    }

                }

                private void changeWidth(int value, View view) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.width = value;
                    view.setLayoutParams(layoutParams);
                }
            });
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    ++lines;
                    Tools.showLogE("当前文字行数：" + lines);
                }
            });

            ObjectAnimator animP = ObjectAnimator.ofInt(this, "currentNumP", dp2px(this, 50));
            animP.setDuration(1000);
            animP.setRepeatCount(2);
            animP.setInterpolator(new AccelerateDecelerateInterpolator());
            animP.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int value = (int) valueAnimator.getAnimatedValue("currentNumP");
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pencil.getLayoutParams();
                    layoutParams.leftMargin = dp2px(TestActivity.this, 30) + value;
                    layoutParams.topMargin = dp2px(TestActivity.this, 12) + pLines * dp2px(TestActivity.this, 16);
                    pencil.setLayoutParams(layoutParams);
                }
            });
            animP.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    ++pLines;
                    Tools.showLogE("当前铅笔行数：" + pLines);
                }
            });

            mSet.playTogether(anim, animP);
            mSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Tools.showLogE("最后结束了，mIsSucceed："+mIsSucceed);
                    if (mIsSucceed) {
                        TransitionManager.beginDelayedTransition(activity, new Fade());
                        pencil.setVisibility(View.GONE);
                    } else {
                        lines = 0;
                        pLines = 0;
                        for (int i = 0; i < 3; i++) {
                            switch (i) {
                                case 0:
                                    changeWidth(0, line0);
                                    break;
                                case 1:
                                    changeWidth(0, line1);
                                    break;
                                case 2:
                                    changeWidth(0, line2);
                                    break;
                            }
                        }
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pencil.getLayoutParams();
                        layoutParams.leftMargin = dp2px(TestActivity.this, 30);
                        layoutParams.topMargin = dp2px(TestActivity.this, 12);
                        pencil.setLayoutParams(layoutParams);
                        Message message = new Message();
                        message.arg1 = 23333333;
                        mHandler.sendMessage(message);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

                private void changeWidth(int value, View view) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.width = value;
                    view.setLayoutParams(layoutParams);
                }
            });
        }
    }

    public int dp2px(Context context, float dpValue) {
        if (mScale == 0f) {
            mScale = context.getResources().getDisplayMetrics().density;
        }
        return (int) (dpValue * mScale + 0.5f);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 ==23333333) {
                btn(null);
            }
        }
    };
}
