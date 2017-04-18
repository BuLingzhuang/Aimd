package com.bulingzhuang.aimd.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.bulingzhuang.aimd.R;

/**
 * Created by bulingzhuang
 * on 2017/3/28
 * E-mail:bulingzhuang@foxmail.com
 */

public class UserRegisterWindowUtil {

    private View mLine_0;
    private View mLine_1;
    private View mLine_2;
    private ImageView mPencil;
    private PopupWindow mPopupWindow;
    private TextView mTvTips;

    private Context mContext;
    private ViewGroup mViewGroup;
    private AnimEndListener mListener;
    private String mTips = "注册成功";

    public interface AnimEndListener {
        void end();
    }

    public UserRegisterWindowUtil(Context context, ViewGroup viewGroup) {
        mContext = context;
        mViewGroup = viewGroup;
    }

    private int currentNum;
    private int currentNumP;
    private int pLines;
    private int lines;
    private float mScale = 0f;
    private boolean mIsSucceed;
    private AnimatorSet mSet;

    public void setCurrentNumP(int currentNumP) {
        this.currentNumP = currentNumP;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public void execute(AnimEndListener listener) {
        mListener = listener;
        showPopupWindow();
    }

    public void finishPopupWindow(AVException e){
        mIsSucceed = true;
        if (e == null) {
            mTips = "注册成功";
        }else {
            mTips = "注册失败";
        }
    }

    /**
     * 显示上传popupWindow
     */
    private void showPopupWindow() {

        @SuppressLint("InflateParams") View inflate = LayoutInflater.from(mContext).inflate(R.layout.popup_user_register, null);
        mLine_0 = inflate.findViewById(R.id.line_0);
        mLine_1 = inflate.findViewById(R.id.line_1);
        mLine_2 = inflate.findViewById(R.id.line_2);
        mTvTips = (TextView) inflate.findViewById(R.id.tv_tips);
        mPencil = (ImageView) inflate.findViewById(R.id.pencil);
        Tools.changeFont(mTvTips);

        mPopupWindow = new PopupWindow(inflate, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setAnimationStyle(R.style.popupWindow_sh_anim_style);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopupWindow = null;
                if (mListener != null) {
                    mListener.end();
                }
            }
        });
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE && !mPopupWindow.isFocusable()) {
                    return true;
                }
                return false;
            }
        });

        mPopupWindow.showAtLocation(mViewGroup, Gravity.NO_GRAVITY, 0, 0);
        startAnim();
    }

    private void startAnim() {
        getAnim();
        mSet.start();
    }

    private void getAnim() {
        if (mSet == null) {
            mSet = new AnimatorSet();
            ObjectAnimator anim = ObjectAnimator.ofInt(this, "currentNum", dp2px(mContext, 50));
            anim.setDuration(1000);
            anim.setRepeatCount(2);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int value = (int) valueAnimator.getAnimatedValue("currentNum");
                    switch (lines) {
                        case 0:
                            changeWidth(value, mLine_0);
                            break;
                        case 1:
                            changeWidth(value, mLine_1);
                            break;
                        case 2:
                            changeWidth(value, mLine_2);
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
                }
            });

            ObjectAnimator animP = ObjectAnimator.ofInt(this, "currentNumP", dp2px(mContext, 50));
            animP.setDuration(1000);
            animP.setRepeatCount(2);
            animP.setInterpolator(new AccelerateDecelerateInterpolator());
            animP.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int value = (int) valueAnimator.getAnimatedValue("currentNumP");
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mPencil.getLayoutParams();
                    layoutParams.leftMargin = dp2px(mContext, 30) + value;
                    layoutParams.topMargin = dp2px(mContext, 12) + pLines * dp2px(mContext, 16);
                    mPencil.setLayoutParams(layoutParams);
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
                }
            });

            mSet.playTogether(anim, animP);
            mSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mIsSucceed) {
                        TransitionManager.beginDelayedTransition(mViewGroup, new Fade());
                        mTvTips.setText(mTips);
                        mPencil.setVisibility(View.GONE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(333);
                                    Message message = new Message();
                                    message.arg1 = 3222222;
                                    mHandler.sendMessage(message);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        lines = 0;
                        pLines = 0;
                        for (int i = 0; i < 3; i++) {
                            switch (i) {
                                case 0:
                                    changeWidth(0, mLine_0);
                                    break;
                                case 1:
                                    changeWidth(0, mLine_1);
                                    break;
                                case 2:
                                    changeWidth(0, mLine_2);
                                    break;
                            }
                        }
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mPencil.getLayoutParams();
                        layoutParams.leftMargin = dp2px(mContext, 30);
                        layoutParams.topMargin = dp2px(mContext, 12);
                        mPencil.setLayoutParams(layoutParams);
                        Message message = new Message();
                        message.arg1 = 2333333;
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

    private int dp2px(Context context, float dpValue) {
        if (mScale == 0f) {
            mScale = context.getResources().getDisplayMetrics().density;
        }
        return (int) (dpValue * mScale + 0.5f);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 2333333:
                    startAnim();
                    break;
                case 3222222:
                    mPopupWindow.dismiss();
                    break;
            }
        }
    };

}
