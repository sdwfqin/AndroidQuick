package com.sdwfqin.widget;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 可以点击的ViewPager
 *
 * @author 张钦
 * @date 2017/8/22
 */
public class ClickViewPager extends ViewPager {

    private GestureDetector mGestureDetector;
    private Context mContext;
    private OnItemClick mOnItemClick;

    public ClickViewPager(Context context) {
        super(context);
        init(context);
    }

    public ClickViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mGestureDetector = new GestureDetector(mContext, onGestureListener);
        mGestureDetector.setOnDoubleTapListener(onDoubleTapListener);
        //解决长按屏幕无法拖动,但是会造成无法识别长按事件
        //mGestureDetector.setIsLongpressEnabled(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };

    GestureDetector.OnDoubleTapListener onDoubleTapListener = new GestureDetector.OnDoubleTapListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            mOnItemClick.OnClick(getChildAt(getCurrentItem()), getCurrentItem());
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;
        }
    };

    public interface OnItemClick {
        void OnClick(View view, int currentItem);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }
}
