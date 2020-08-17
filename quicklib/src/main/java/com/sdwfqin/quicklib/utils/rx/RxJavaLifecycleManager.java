package com.sdwfqin.quicklib.utils.rx;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

import static androidx.lifecycle.Lifecycle.State.DESTROYED;

/**
 * RxJava生命周期管理
 * <p>
 *
 * @author 张钦
 * @date 2020/8/11
 */
public class RxJavaLifecycleManager implements LifecycleObserver {

    private CompositeDisposable compositeDisposable;

    public RxJavaLifecycleManager(LifecycleOwner owner) {
        if (owner.getLifecycle().getCurrentState() == DESTROYED) {
            // ignore
            return;
        }
        owner.getLifecycle().addObserver(this);
    }

    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        if (disposable != null) {
            compositeDisposable.add(disposable);
        }

    }

    public void dispose() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        dispose();
    }
}
