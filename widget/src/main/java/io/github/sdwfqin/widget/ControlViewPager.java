package io.github.sdwfqin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ConvertUtils;

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
    private boolean isGoLeft = true;
    /**
     * 是否可以滑出右侧页面
     */
    private boolean isGoRight = true;
    private float lastIX = 0;
    private float lastIY = 0;
    private int direction;
    /**
     * 允许触摸
     */
    private boolean enableTouch = true;


    public ControlViewPager(Context context) {
        this(context, null);
    }

    public ControlViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isGoLeft() {
        return isGoLeft;
    }

    public void setGoLeft(boolean goLeft) {
        isGoLeft = goLeft;
    }

    public boolean isGoRight() {
        return isGoRight;
    }

    public void setGoRight(boolean goRight) {
        isGoRight = goRight;
    }

    public boolean isEnableTouch() {
        return enableTouch;
    }

    public void setEnableTouch(boolean enableTouch) {
        this.enableTouch = enableTouch;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!enableTouch) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取起始坐标值
                lastIX = event.getRawX();
                lastIY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                if (Math.abs(event.getRawY() - lastIY) > Math.abs(event.getRawX() - lastIX)) {
                    return false;
                }

                if (Math.abs(event.getRawX() - lastIX) > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    return true;
                }
                break;

        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                if (lastIX - event.getRawX() > ConvertUtils.dp2px(80)) {
                    //从右到左滑
                    direction = 2;
                } else if (lastIX - event.getRawX() < -ConvertUtils.dp2px(80)) {
                    //从左到右滑
                    direction = 1;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (direction == 1) {
                    if (isGoLeft) {
                        // 划出左侧界面
                        try {
                            setCurrentItem(getCurrentItem() - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (direction == 2) {
                    if (isGoRight) {
                        // 划出右侧界面
                        try {
                            setCurrentItem(getCurrentItem() + 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                direction = -1;
                break;
        }
        return true;
    }
}
