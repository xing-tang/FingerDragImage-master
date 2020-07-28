package xing.tang.library.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import xing.tang.library.util.ScreenUtils;
import xing.tang.library.widget.photoview.PhotoView;


/**
 * FingerDragLayout
 *
 * @Description: 一个辅助手指下滑拖动关闭图片的LinearLayout
 * @Author: xing.tang
 * @CreateDate: 2020/7/18 8:59 PM
 */
public class FingerDragLayout extends LinearLayout {

    private static final String TAG = FingerDragLayout.class.getSimpleName();
    private final static int MAX_EXIT_Y = 500;
    private final static float MAX_SCALE = 0.25f;
    private final static long DURATION = 200;
    private PhotoView photoView;
    private float mDownY;
    private float mTranslationX;
    private float mTranslationY;
    private float mLastTranslationX;
    private float mLastTranslationY;
    private float scale = 1.0f;
    private boolean isAnimateX = false;
    private boolean isAnimateY = false;
    private int mTouchslop;
    // 是否执行关闭页面的操作
    private boolean isFinsh = false;
    // 手指按下的点为(downX, downY)
    private float downX = 0, downY = 0;
    // 手指离开屏幕的点为(upX, upY)
    private float upX = 0, upY = 0;
    private OnAlphaChangedListener mOnAlphaChangedListener;
    private OnPageFinishListener mOnPageFinishListener;

    public FingerDragLayout(Context context) {
        this(context, null);
    }

    public FingerDragLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FingerDragLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        mTouchslop = ViewConfiguration.getTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        photoView = (PhotoView) getChildAt(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        int action = ev.getAction() & ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (photoView != null && photoView.getVisibility() == View.VISIBLE) {
                    Log.d(TAG, "photoView.getMaxTouchCount()=" + photoView.getMaxTouchCount());
                    Log.d("asd","photoView.getScaleX()="+photoView.getScaleX()+";photoView.getScale()="+photoView.getScale());
                    isIntercept = (photoView.getScale() <= (photoView.getMinimumScale() + 0.001F))
                            && (photoView.getMaxTouchCount() == 0 || photoView.getMaxTouchCount() == 1)
                            && Math.abs(ev.getRawY() - mDownY) > 2 * mTouchslop;
                }
                break;
            default:
                break;
        }
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // ACTION_DOWN在这里是不会触发的，因为down事件首先被其子View消费
                // 后续满足某些条件后触发外部拦截，从而该View获取到move事件
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                downX = 0;
                downY = 0;
                onActionUp();
                if (isFinsh) isFinsh = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (downX == 0 && downY == 0) {
                    downX = event.getX();
                    downY = event.getY();
                }
                upX = event.getX();
                upY = event.getY();
                float moveX = upX - downX;
                float moveY = upY - downY;
                Log.d(TAG, "downX=" + downX + ";downY=" + downY + ";upX=" + upX + ";upY=" + upY + ";moveX=" + moveX + ";moveY=" + moveY);
                if (moveY > moveX) {
                    if (downY - upY > mTouchslop) {
                        Log.d(TAG, "向上滑动");
                    } else if (upY - downY > mTouchslop) {
                        if (photoView.getMaxTouchCount() == 0 || photoView.getMaxTouchCount() == 1) {
                            isFinsh = true;
                            Log.d(TAG, "向下滑动");
                        }
                    }
                }
                if (isFinsh) onOneFingerPanActionMove(event, moveX, moveY);
                Log.d(TAG, "photoImage.getMaxTouchCount()=" + photoView.getMaxTouchCount());
                return false;
        }
        return true;
    }

    private void onOneFingerPanActionMove(MotionEvent event, float deltaX, float deltaY) {
        mTranslationX = deltaX + mLastTranslationX;
        mTranslationY = deltaY + mLastTranslationY;
        scale = Math.min(Math.max(1 - Math.abs(deltaY) / getHeight(), MAX_SCALE), 1);
        Log.d(TAG, "deltaX=" + deltaX + ";mTranslationX=" + mTranslationX + ";deltaY=" + deltaY + ";mTranslationY=" + mTranslationY);
        Log.d(TAG, "mLastTranslationX=" + mLastTranslationX + ";mLastTranslationY=" + mLastTranslationY);
        // 触发回调，根据距离处理其他控件的透明度等等
        changeTransparent(mTranslationY);
        photoView.setScaleX(scale);
        photoView.setScaleY(scale);
        this.setScrollX(-(int) deltaX);
        this.setScrollY(-(int) deltaY);
    }

    private void onActionUp() {
        if (Math.abs(mTranslationY) > MAX_EXIT_Y) {
            exitWithTranslation(mTranslationY);
        } else {
            resetCallBackAnimation();
        }
    }

    public void exitWithTranslation(float currentY) {
        if (currentY > 0) {
            ValueAnimator animDown = ValueAnimator.ofFloat(mTranslationY, getHeight());
            animDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = (float) animation.getAnimatedValue();
                    FingerDragLayout.this.setScrollY(-(int) fraction);
                }
            });
            animDown.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    pageFinish();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animDown.setDuration(DURATION);
            animDown.setInterpolator(new LinearInterpolator());
            animDown.start();
        } else {
            ValueAnimator animUp = ValueAnimator.ofFloat(mTranslationY, -getHeight());
            animUp.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = (float) animation.getAnimatedValue();
                    FingerDragLayout.this.setScrollY(-(int) fraction);
                }
            });
            animUp.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    pageFinish();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animUp.setDuration(DURATION);
            animUp.setInterpolator(new LinearInterpolator());
            animUp.start();
        }
    }

    private void resetCallBackAnimation() {
        ValueAnimator animatorScrollX = ValueAnimator.ofFloat(mTranslationX, 0);
        ValueAnimator animatorScrollY = ValueAnimator.ofFloat(mTranslationY, 0);
        ValueAnimator animatorScaleX = ValueAnimator.ofFloat(scale, 1.0f);
        ValueAnimator animatorScaleY = ValueAnimator.ofFloat(scale, 1.0f);
        animatorScaleX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float scaleX = (float) valueAnimator.getAnimatedValue();
                photoView.setScaleX(scaleX);
            }
        });
        animatorScaleY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float scaleY = (float) valueAnimator.getAnimatedValue();
                photoView.setScaleY(scaleY);
                changeTransparent(mTranslationY);
            }
        });
        animatorScrollX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (isAnimateX) {
                    mTranslationX = (float) valueAnimator.getAnimatedValue();
                    mLastTranslationX = mTranslationX;
                    scale = Math.min(Math.max(1 - Math.abs(mTranslationX) / getHeight(), MAX_SCALE), 1);
                    FingerDragLayout.this.setScrollX(-(int) mTranslationX);
                    photoView.setScaleX(scale);
                    photoView.setScaleY(scale);
                }
            }
        });
        animatorScrollY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (isAnimateY) {
                    mTranslationY = (float) valueAnimator.getAnimatedValue();
                    mLastTranslationY = mTranslationY;
                    FingerDragLayout.this.setScrollY(-(int) mTranslationY);
                }
            }
        });
        animatorScrollY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimateX = true;
                isAnimateY = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isAnimateX && isAnimateY) {
                    mTranslationX = 0;
                    mTranslationY = 0;
                    scale = 1.0f;
                }
                isAnimateX = false;
                isAnimateY = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorScrollX, animatorScrollY, animatorScaleX, animatorScaleY);
        set.setDuration(DURATION);
        set.start();
    }

    private void changeTransparent(float mTranslationY) {
        float percent = Math.abs(mTranslationY) / ScreenUtils.getScreenHeight(this.getContext());
        float alpha = 1.0f - percent;
        Log.d(TAG, "percent=" + percent + "；alpha=" + alpha);
        if (null != mOnAlphaChangedListener) {
            mOnAlphaChangedListener.onAlphaChanged(alpha);
        }
    }

    private void pageFinish() {
        if (mOnPageFinishListener != null) {
            mOnPageFinishListener.onPageFinish();
        }
    }

    /**
     * 提供给外部修改透明度的回调方法
     * 可根据位移距离或者alpha来改变主UI控件的透明度等
     */
    public void setOnAlphaChangeListener(OnAlphaChangedListener alphaChangeListener) {
        mOnAlphaChangedListener = alphaChangeListener;
    }

    public void setOnPageFinishListener(OnPageFinishListener onPageFinishListener) {
        mOnPageFinishListener = onPageFinishListener;
    }

    public interface OnAlphaChangedListener {
        void onAlphaChanged(float alpha);
    }

    public interface OnPageFinishListener {
        void onPageFinish();
    }
}