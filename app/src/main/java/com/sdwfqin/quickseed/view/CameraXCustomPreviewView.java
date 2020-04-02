package com.sdwfqin.quickseed.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.view.PreviewView;

import com.blankj.utilcode.util.LogUtils;

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
//    private ScaleGestureDetector mScaleGestureDetector;

    /**
     * 缩放监听
     */
    public interface CustomTouchListener {
        /**
         * 放大
         */
        void zoom();

        /**
         * 缩小
         */
        void ZoomOut();

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
        mGestureDetector.setOnDoubleTapListener(onDoubleTapListener);

//        mScaleGestureDetector = new ScaleGestureDetector(context, onScaleGestureListener);
        // 解决长按屏幕无法拖动,但是会造成无法识别长按事件
        // mGestureDetector.setIsLongpressEnabled(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 接管onTouchEvent
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 缩放监听
     */
//    ScaleGestureDetector.OnScaleGestureListener onScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//            LogUtils.e(detector.getScaleFactor());
//            return true;
//        }
//
//        @Override
//        public boolean onScaleBegin(ScaleGestureDetector detector) {
//            return false;
//        }
//
//        @Override
//        public void onScaleEnd(ScaleGestureDetector detector) {
//
//        }
//    };

    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            LogUtils.i("onDown: 按下");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            LogUtils.i("onShowPress: 刚碰上还没松开");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            LogUtils.i("onSingleTapUp: 轻轻一碰后马上松开");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            LogUtils.i("onScroll: 按下后拖动");
            // 大于两个触摸点
            if (e2.getPointerCount() >= 2) {

                //event中封存了所有屏幕被触摸的点的信息，第一个触摸的位置可以通过event.getX(0)/getY(0)得到
                float offSetX = e2.getX(0) - e2.getX(1);
                float offSetY = e2.getY(0) - e2.getY(1);
                //运用三角函数的公式，通过计算X,Y坐标的差值，计算两点间的距离
                currentDistance = (float) Math.sqrt(offSetX * offSetX + offSetY * offSetY);
                if (lastDistance == 0) {//如果是第一次进行判断
                    lastDistance = currentDistance;
                } else {
                    if (currentDistance - lastDistance > 10) {
                        // 放大
                        if (mCustomTouchListener != null) {
                            mCustomTouchListener.zoom();
                        }
                    } else if (lastDistance - currentDistance > 10) {
                        // 缩小
                        if (mCustomTouchListener != null) {
                            mCustomTouchListener.ZoomOut();
                        }
                    }
                }
                //在一次缩放操作完成后，将本次的距离赋值给lastDistance，以便下一次判断
                //但这种方法写在move动作中，意味着手指一直没有抬起，监控两手指之间的变化距离超过10
                //就执行缩放操作，不是在两次点击之间的距离变化来判断缩放操作
                //故这种将本次距离留待下一次判断的方法，不能在两次点击之间使用
                lastDistance = currentDistance;
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            LogUtils.i("onLongPress: 长按屏幕");
            if (mCustomTouchListener != null) {
                mCustomTouchListener.longClick(e.getX(), e.getY());
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            LogUtils.i("onFling: 滑动后松开");
            currentDistance = 0;
            lastDistance = 0;
            return true;
        }
    };

    GestureDetector.OnDoubleTapListener onDoubleTapListener = new GestureDetector.OnDoubleTapListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            LogUtils.i("onSingleTapConfirmed: 严格的单击");
            if (mCustomTouchListener != null) {
                mCustomTouchListener.click(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            LogUtils.i("onDoubleTap: 双击");
            if (mCustomTouchListener != null) {
                mCustomTouchListener.doubleClick(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            LogUtils.i("onDoubleTapEvent: 表示发生双击行为");
            return true;
        }
    };
}
