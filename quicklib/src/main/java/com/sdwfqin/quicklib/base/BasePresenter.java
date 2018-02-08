package com.sdwfqin.quicklib.base;

/**
 * 描述：Presenter基类
 *
 * @author zhangqin
 */
public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void detachView();
}
