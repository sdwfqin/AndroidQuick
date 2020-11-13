package io.github.sdwfqin.samplecommonlibrary.view;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * 自定义CameraX点击事件
 * <p>
 *
 * @author 张钦
 * @date 2020/4/2
 */
public class CameraXPreviewViewTouchListener implements View.OnTouchListener {

    private GestureDetector mGestureDetector;
    /**
     * 缩放相关
     */
    private ScaleGestureDetector mScaleGestureDetector;

    public CameraXPreviewViewTouchListener(Context context) {
        mGestureDetector = new GestureDetector(context, onGestureListener);
        mScaleGestureDetector = new ScaleGestureDetector(context, onScaleGestureListener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        if (!mScaleGestureDetector.isInProgress()) {
            mGestureDetector.onTouchEvent(event);
        }
        return true;
    }

    /**
     * 缩放监听
     */
    public interface CustomTouchListener {
        /**
         * 放大
         */
        void zoom(float delta);

        /**
         * 点击
         */
        void click(float x, float y);

        /**
         * 双击
         */
        void doubleClick(float x, float y);

        /**
         * 长按
         */
        void longClick(float x, float y);
    }

    private CustomTouchListener mCustomTouchListener;

    public void setCustomTouchListener(CustomTouchListener customTouchListener) {
        mCustomTouchListener = customTouchListener;
    }

    /**
     * 缩放监听
     */
    ScaleGestureDetector.OnScaleGestureListener onScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float delta = detector.getScaleFactor();
            if (mCustomTouchListener != null) {
                mCustomTouchListener.zoom(delta);
            }
            return true;
        }
    };


    GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public void onLongPress(MotionEvent e) {
            if (mCustomTouchListener != null) {
                mCustomTouchListener.longClick(e.getX(), e.getY());
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mCustomTouchListener != null) {
                mCustomTouchListener.click(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mCustomTouchListener != null) {
                mCustomTouchListener.doubleClick(e.getX(), e.getY());
            }
            return true;
        }
    };
}
