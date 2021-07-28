package io.github.sdwfqin.quicklib.base;

import android.app.Activity;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * 描述：View的基类
 *
 * @author zhangqin
 */
public interface IBaseActivity {

    /**
     * 获取Activity
     */
    Activity getActivity();

    /**
     * 显示吐司
     *
     * @param msg 提示消息
     */
    void showMsg(String msg);

    /**
     * 显示加载动画
     */
    void showProgress();

    /**
     * 显示提示
     */
    void showTip(@QMUITipDialog.Builder.IconType int iconType, CharSequence tipWord);

    /**
     * 关闭加载动画
     */
    void hideProgress();

    /**
     * 关闭提示
     */
    void hideTip();

    /**
     * 跳转页面
     */
    void startActivitySample(Class<?> cls);
}

