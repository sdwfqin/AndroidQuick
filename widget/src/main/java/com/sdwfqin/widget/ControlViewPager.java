package com.sdwfqin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.viewpager.widget.ViewPager;

/**
 * 标题：可动态禁止（允许）左滑/右滑的ViewPager
 * 详细描述：
 * <p>
 * 创建者：张钦
 * 创建时间：2019-05-15
 * <p>
 * 修改者：张钦
 * 修改时间：2019-05-15
 * 修改内容：
 */
public class ControlViewPager extends ViewPager {

    /**
     * 是否可以滑出左侧页面
     */
    private boolean isLeftSlide = true;
    /**
     * 是否可以滑出右侧页面
     */
    private boolean isRightSlide = true;
    private float mLastInterceptX;
    private float mLastX;
    /**
     * 1划出左侧界面、2划出右侧界面
     */
    private int mDirection;


    public ControlViewPager(Context context) {
        super(context);
    }

    public ControlViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 获取起始坐标值
                mLastInterceptX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX() - mLastInterceptX) > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 获取起始坐标值（可能获取不到）
                mLastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mLastX == 0) {
                    mLastX = event.getX();
                }
                if (mLastX - event.getX() > 150) {
                    // 从右到左滑
                    mDirection = 2;
                } else if (mLastX - event.getX() < -150) {
                    // 从左到右滑
                    mDirection = 1;
                }
                break;
            case MotionEvent.ACTION_UP:
                mLastX = 0;
                if (mDirection == 1) {
                    if (isLeftSlide) {
                        // 划出左侧界面
                        try {
                            setCurrentItem(getCurrentItem() - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (isRightSlide) {
                        // 划出右侧界面
                        try {
                            setCurrentItem(getCurrentItem() + 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }

        return true;
    }

    public boolean isLeftSlide() {
        return isLeftSlide;
    }

    public void setLeftSlide(boolean leftSlide) {
        isLeftSlide = leftSlide;
    }

    public boolean isRightSlide() {
        return isRightSlide;
    }

    public void setRightSlide(boolean rightSlide) {
        isRightSlide = rightSlide;
    }
}
