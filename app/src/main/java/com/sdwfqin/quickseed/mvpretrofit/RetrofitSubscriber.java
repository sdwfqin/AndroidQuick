package com.sdwfqin.quickseed.mvpretrofit;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.sdwfqin.quicklib.mvp.IBaseView;

import java.lang.ref.WeakReference;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 描述：Retrofit Subscriber 封装
 * <p>
 * Activity
 *
 * @author zhangqin
 * @date 2018/4/4
 */
public abstract class RetrofitSubscriber<T> implements Observer<T> {

    private final WeakReference<IBaseView> mView;

    public RetrofitSubscriber(IBaseView view) {
        super();
        mView = new WeakReference<>(view);
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (!NetworkUtils.isConnected()) {
            mView.get().showMsg("网络未连接，请检查网络");
            d.dispose();
        } else {
            mView.get().showProgress();
            mView.get().addSubscribe(d);
        }
    }

    @Override
    public void onComplete() {
        if (mView != null && mView.get() != null) {
            mView.get().hideProgress();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mView != null && mView.get() != null) {
            mView.get().hideProgress();
        }
        onNetError(e);
    }

    @Override
    public void onNext(T response) {
        if (response instanceof BaseResponse) {
            if (((BaseResponse) response).isOk(mView.get())) {
                onSuccess(response);
            } else {
                onServiceError(response);
            }
        } else {
            onOtherSuccess(response);
        }
    }

    /**
     * 请求成功并且服务器未下发异常
     *
     * @param response
     */
    protected abstract void onSuccess(T response);

    /**
     * 请求成功, 返回非JavaBean
     *
     * @param response
     */
    protected void onOtherSuccess(T response) {

    }

    /**
     * 请求成功，服务器下发异常
     *
     * @param response
     */
    protected void onServiceError(T response) {

    }

    /**
     * 网络异常
     *
     * @param e
     */
    protected void onNetError(Throwable e) {
        LogUtils.e(e);
        if (mView != null && mView.get() != null) {
            NetworkError.error(mView.get(), e);
        }
    }
}
