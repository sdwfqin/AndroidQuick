package com.sdwfqin.quicklib.mvp;

/**
 * 描述：Presenter基类
 *
 * @author zhangqin
 */
public interface IBasePresenter<T extends IBaseView> {

    void attachView(T view);

    void detachView();
}
