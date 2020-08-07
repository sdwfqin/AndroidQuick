package io.github.sdwfqin.samplecommonlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatImageView;

import io.github.sdwfqin.samplecommonlibrary.R;

/**
 * 对焦View
 * <p>
 *
 * @author 张钦
 * @date 2020/4/2
 */
public class FocusImageView extends AppCompatImageView {

    private static final int NO_ID = -1;
    private int mFocusImg = NO_ID;
    private int mFocusSucceedImg = NO_ID;
    private int mFocusFailedImg = NO_ID;
    private Animation mAnimation;
    private Handler mHandler;

    public FocusImageView(Context context) {
        super(context);
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.focusview_show);
        setVisibility(View.GONE);
        mHandler = new Handler();
    }

    public FocusImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.focusview_show);
        mHandler = new Handler();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FocusImageView);
        mFocusImg = typedArray.getResourceId(R.styleable.FocusImageView_focus_focusing_id, NO_ID);
        mFocusSucceedImg = typedArray.getResourceId(R.styleable.FocusImageView_focus_success_id, NO_ID);
        mFocusFailedImg = typedArray.getResourceId(R.styleable.FocusImageView_focus_fail_id, NO_ID);
        typedArray.recycle();

        //聚焦图片不能为空
        if (mFocusImg == NO_ID || mFocusSucceedImg == NO_ID || mFocusFailedImg == NO_ID) {
            throw new RuntimeException("mFocusImg,mFocusSucceedImg,mFocusFailedImg is null");
        }
    }

    /**
     * 显示对焦图案
     */
    public void startFocus(Point point) {
        if (mFocusImg == NO_ID || mFocusSucceedImg == NO_ID || mFocusFailedImg == NO_ID) {
            throw new RuntimeException("focus image is null");
        }
        //根据触摸的坐标设置聚焦图案的位置
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        params.topMargin = point.y - getMeasuredHeight() / 2;
        params.leftMargin = point.x - getMeasuredWidth() / 2;
        setLayoutParams(params);
        //设置控件可见，并开始动画
        setVisibility(View.VISIBLE);
        setImageResource(mFocusImg);
        startAnimation(mAnimation);
    }

    /**
     * 聚焦成功回调
     */
    public void onFocusSuccess() {
        setImageResource(mFocusSucceedImg);
        //移除在startFocus中设置的callback，1秒后隐藏该控件
        mHandler.removeCallbacks(null, null);
        mHandler.postDelayed(() -> setVisibility(View.GONE), 1000);

    }

    /**
     * 聚焦失败回调
     */
    public void onFocusFailed() {
        setImageResource(mFocusFailedImg);
        //移除在startFocus中设置的callback，1秒后隐藏该控件
        mHandler.removeCallbacks(null, null);
        mHandler.postDelayed(() -> setVisibility(View.GONE), 1000);
    }

    /**
     * 设置开始聚焦时的图片
     *
     * @param focus
     */
    public void setFocusImg(int focus) {
        this.mFocusImg = focus;
    }

    /**
     * 设置聚焦成功显示的图片
     *
     * @param focusSucceed
     */
    public void setFocusSucceedImg(int focusSucceed) {
        this.mFocusSucceedImg = focusSucceed;
    }
}