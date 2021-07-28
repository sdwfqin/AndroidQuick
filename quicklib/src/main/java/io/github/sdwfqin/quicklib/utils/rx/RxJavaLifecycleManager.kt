package io.github.sdwfqin.quicklib.utils.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * RxJava生命周期管理
 *
 * @author 张钦
 * @date 2020/8/11
 */
class RxJavaLifecycleManager(owner: LifecycleOwner) : LifecycleObserver {

    init {
        if (owner.lifecycle.currentState != Lifecycle.State.DESTROYED) {
            owner.lifecycle.addObserver(this)
        }
    }

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun dispose() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        dispose()
    }
}