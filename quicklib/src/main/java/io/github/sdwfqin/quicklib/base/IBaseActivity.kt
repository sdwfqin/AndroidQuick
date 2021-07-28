package io.github.sdwfqin.quicklib.base

import android.app.Activity
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

/**
 * 描述：View的基类
 *
 * @author zhangqin
 */
interface IBaseActivity {
    /**
     * 获取Activity
     */
    fun getActivity(): Activity

    /**
     * 显示吐司
     *
     * @param msg 提示消息
     */
    fun showMsg(msg: String)

    /**
     * 显示加载动画
     */
    fun showProgress()

    /**
     * 显示提示
     */
    fun showTip(@QMUITipDialog.Builder.IconType iconType: Int, tipWord: CharSequence)

    /**
     * 关闭加载动画
     */
    fun hideProgress()

    /**
     * 关闭提示
     */
    fun hideTip()

    /**
     * 跳转页面
     */
    fun startActivitySample(cls: Class<*>)
}