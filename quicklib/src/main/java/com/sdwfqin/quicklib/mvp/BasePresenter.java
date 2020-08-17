package com.sdwfqin.quicklib.mvp;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.sdwfqin.quicklib.utils.rx.RxJavaLifecycleManager;

import static androidx.lifecycle.Lifecycle.State.DESTROYED;

/**
 * 描述：SamplePresenter
 *
 * @author zhangqin
 */
public abstract class BasePresenter<T extends IBaseView> implements IBasePresenter<T>, LifecycleObserver {

    protected T mView;
    private RxJavaLifecycleManager mRxJavaLifecycleManager;

    public BasePresenter(LifecycleOwner owner) {
        if (owner.getLifecycle().getCurrentState() == DESTROYED) {
            // ignore
            return;
        }
        owner.getLifecycle().addObserver(this);
        mRxJavaLifecycleManager = new RxJavaLifecycleManager(owner);
    }

    @Override
    public void attachView(T view) {
        mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        detachView();
    }
}
