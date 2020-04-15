package com.sdwfqin.quicklib.mvp;

/**
 * 描述：SamplePresenter
 *
 * @author zhangqin
 */
public abstract class SamplePresenter<T extends BaseView> implements BasePresenter<T> {

    protected T mView;

    @Override
    public void attachView(T view) {
        mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }
}
