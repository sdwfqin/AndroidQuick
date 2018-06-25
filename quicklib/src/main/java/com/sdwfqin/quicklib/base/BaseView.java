package com.sdwfqin.quicklib.base;

/**
 * 描述：View的基类
 *
 * @author zhangqin
 */
public interface BaseView {

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
     * 关闭加载动画
     */
    void hideProgress();
}
