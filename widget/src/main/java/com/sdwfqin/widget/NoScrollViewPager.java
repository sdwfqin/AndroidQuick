package com.sdwfqin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 描述：可以禁止滑动的Viewpager
 * <p>
 * ViewPager2可以使用setUserInputEnabled(false)实现禁止滑动
 *
 * @author 张钦
 * @date 2017/9/25
 */
public class NoScrollViewPager extends ViewPager {

    /**
     * true 禁止滑动
     */
    private boolean noScroll = true;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    /**
     * 设置是否允许滑动
     * <p>
     * true 禁止滑动
     *
     * @param noScroll
     */
    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    public boolean getNoScroll() {
        return noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return !noScroll && super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return !noScroll && super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        //false 去除滚动效果
        super.setCurrentItem(item, false);
    }
}
