package io.github.sdwfqin.quicklib.utils

import android.view.View

/**
 * 描述：按钮防抖
 *
 * @author 张钦
 * @date 2018/12/18
 */
abstract class IClickListener : View.OnClickListener {

    private var mLastClickTime: Long = 0

    override fun onClick(view: View) {
        if (System.currentTimeMillis() - mLastClickTime >= TIME_INTERVAL) {
            onIClick(view)
            mLastClickTime = System.currentTimeMillis()
        } else {
            onAgain(view)
        }
    }

    /**
     * 正常点击
     *
     * @param view
     */
    protected abstract fun onIClick(view: View)

    /**
     * 重复点击
     *
     * @param view
     */
    protected open fun onAgain(view: View) {}

    companion object {
        const val TIME_INTERVAL = 1000
    }
}