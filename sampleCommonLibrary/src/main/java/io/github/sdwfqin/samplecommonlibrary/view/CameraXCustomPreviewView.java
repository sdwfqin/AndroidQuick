package io.github.sdwfqin.samplecommonlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.view.PreviewView;

/**
 * 自定义CameraX预览页面
 * <p>
 *
 * @author 张钦
 * @date 2020/4/2
 */
public class CameraXCustomPreviewView extends PreviewView {

    private GestureDetector mGestureDetector;

    /**
     * 缩放相关
     */
    private float currentDistance = 0;
    private float lastDistance = 0;
    private ScaleGestureDetector mScaleGestureDetector;

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

    public CameraXCustomPreviewView(@NonNull Context context) {
        this(context, null);
    }

    public CameraXCustomPreviewView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraXCustomPreviewView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CameraXCustomPreviewView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mGestureDetector = new GestureDetector(context, onGestureListener);
        mScaleGestureDetector = new ScaleGestureDetector(context, onScaleGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        if (!mScaleGestureDetector.isInProgress()) {
            mGestureDetector.onTouchEvent(event);
        }
        return true;
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
            currentDistance = 0;
            lastDistance = 0;
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
