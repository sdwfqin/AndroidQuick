package com.sdwfqin.quickseed.retrofit;

import com.blankj.utilcode.util.LogUtils;
import com.sdwfqin.quicklib.base.BaseActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.ref.WeakReference;

/**
 * 描述：Retrofit Subscriber 封装
 * <p>
 * Activity
 *
 * @author zhangqin
 * @date 2018/4/4
 */
public abstract class RetrofitSubscriberA<T> implements Subscriber<T> {

    private final WeakReference<BaseActivity> mContent;

    public RetrofitSubscriberA(BaseActivity context) {
        super();
        mContent = new WeakReference<>(context);
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onComplete() {
        if (mContent != null && mContent.get() != null) {
            mContent.get().hideProgress();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mContent != null && mContent.get() != null) {
            mContent.get().hideProgress();
        }
        onNetError(e);
    }

    @Override
    public void onNext(T response) {
        if (response instanceof BaseResponse) {
            if (((BaseResponse) response).isOk(mContent.get())) {
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
        if (mContent != null && mContent.get() != null) {
            NetworkError.error(mContent.get(), e);
        }
    }
}
