package io.github.sdwfqin.quicklib.utils;

import android.view.View;

/**
 * 描述：按钮防抖
 *
 * @author 张钦
 * @date 2018/12/18
 */
public abstract class IClickListener implements View.OnClickListener {

    private long mLastClickTime = 0;
    public static final int TIME_INTERVAL = 1000;

    @Override
    public final void onClick(View view) {
        if (System.currentTimeMillis() - mLastClickTime >= TIME_INTERVAL) {
            onIClick(view);
            mLastClickTime = System.currentTimeMillis();
        } else {
            onAgain(view);
        }
    }

    /**
     * 正常点击
     *
     * @param view
     */
    protected abstract void onIClick(View view);

    /**
     * 重复点击
     *
     * @param view
     */
    protected void onAgain(View view) {

    }
}
