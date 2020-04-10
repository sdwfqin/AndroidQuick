package com.sdwfqin.quicklib.base;

import androidx.viewbinding.ViewBinding;

/**
 * 描述：Mvp Activity基类
 *
 * @author 张钦
 */
public abstract class BaseMvpActivity<V extends ViewBinding, T extends BasePresenter> extends BaseActivity<V> {

    protected T mPresenter;

    @Override
    protected void initPresenter() {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void removePresenter() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     * 创建Presenter
     *
     * @return
     */
    protected abstract T createPresenter();

}