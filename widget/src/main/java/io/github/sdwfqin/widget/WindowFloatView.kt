package io.github.sdwfqin.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 悬浮窗View
 * <p>
 *
 * @author 张钦
 * @date 2020/4/10
 */
public abstract class WindowFloatView {

    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;

    private boolean isShowing = false;
    private boolean mCreate = false;
    private View mDecor;
    private boolean isCanMove = false;

    public WindowFloatView(@NonNull Context context) {
        mContext = context;
        //窗口管理器
        mWindowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        //布局参数
        mParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mParams.format = PixelFormat.RGBA_8888;

        mParams.flags = getWindowFlags();

        mParams.gravity = getWindowGravity();
        mParams.width = getWidth();
        mParams.height = getHeight();
        if (getCreateAnimator() != 0) {
            mParams.windowAnimations = getCreateAnimator();
        }
    }

    public void show() {
        if (isShowing) {
            if (mDecor != null) {
                mDecor.setVisibility(View.VISIBLE);
            }
            return;
        }
        if (!mCreate) {
            dispathOnCreate();
        }
        mWindowManager.addView(mDecor, mParams);
        isShowing = true;
        onStart();
    }

    private void dispathOnCreate() {
        if (!mCreate) {
            create();
            mCreate = true;
        }
    }

    private void create() {
        mDecor = LayoutInflater.from(getContext()).inflate(getLayoutView(), null);

        onCreate(mDecor, mParams);
        if (isCanMove) {
            mDecor.setOnTouchListener(new FloatOnTouchListener());
        }

        //如果集成的有ButterKnife，可以在这里声明ButterKnife.bind(this, mDecor);

    }

    //留给子类修改布局参数使用。
    protected void onCreate(View decor, WindowManager.LayoutParams layoutParams) {

    }

    public void dismiss() {
        if (mDecor == null || !isShowing) {
            return;
        }
        try {
            onStop();
            mWindowManager.removeViewImmediate(mDecor);
            //这个地方可以注销ButterKnife
        } finally {
            mDecor = null;
            isShowing = false;
            mCreate = false;
            //这里可以还原参数
        }
    }

    public void hide() {
        if (mDecor != null) {
            mDecor.setVisibility(View.GONE);
        }
    }

    //获取当前悬浮窗是否展示
    public boolean isShowing() {
        return isShowing;
    }

    @Nullable
    protected <T extends View> T findViewById(@IdRes int id) {
        return mDecor.findViewById(id);
    }

    public void setCanMove(boolean isCan) {
        if (isCan) {
            isCanMove = true;
        } else {
            isCanMove = false;
        }
    }

    private class FloatOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    mParams.x = mParams.x + movedX;
                    mParams.y = mParams.y + movedY;

                    // 更新悬浮窗控件布局
                    mWindowManager.updateViewLayout(v, mParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    public WindowManager.LayoutParams getParams() {
        return mParams;
    }

    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    @NonNull
    protected Context getContext() {
        return mContext;
    }

    /**
     * 悬浮窗布局
     */
    protected abstract int getLayoutView();

    /**
     * 高度
     */
    protected abstract int getHeight();

    /**
     * 宽度
     */
    protected abstract int getWidth();

    protected int getWindowFlags() {
        return WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    protected int getWindowGravity() {
        return Gravity.LEFT | Gravity.TOP;
    }

    protected int getCreateAnimator() {
        return -1;
    }

    /**
     * 界面加载完成
     */
    protected void onStart() {

    }

    /**
     * 界面销毁
     */
    protected void onStop() {

    }

}
