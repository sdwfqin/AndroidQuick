package com.sdwfqin.quicklib.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.sdwfqin.quicklib.mvp.BaseView;
import com.sdwfqin.quicklib.utils.eventbus.Event;
import com.sdwfqin.quicklib.utils.eventbus.EventBusUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 描述：Fragment基类
 *
 * @author 张钦
 * @date 2017/8/3
 */
public abstract class BaseFragment<V extends ViewBinding> extends Fragment implements BaseView {

    protected V mBinding;
    protected BaseActivity mActivity;
    protected Context mContext;
    protected LayoutInflater mInflater;
    /**
     * 标志位，标志已经初始化完成。
     */
    @Deprecated
    protected boolean mIsPrepared;
    /**
     * 当前界面是否可见
     */
    @Deprecated
    protected boolean mIsVisible;
    /**
     * 是否加载过，用于缓存处理
     * 需要手动在lazyLoad()方法中做判断
     */
    @Deprecated
    protected boolean mIsLoad = false;
    private QMUITipDialog mQmuiTipDialog;
    protected CompositeDisposable mCompositeDisposable;

    /**
     * Fragment的UI是否是可见
     * <p>
     * 在onCreateView方法之前调用
     * <p>
     * ViewPager2已经可以实现原生懒加载
     * <p>
     * 如果还是用的ViewPager或其它方式可以参考以下方式实现懒加载
     * https://juejin.im/post/5cdb7c15f265da036c57ac66
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisible = isVisibleToUser;
        baseLazyLoad();
    }

    @Override
    public void onAttach(Context context) {
        mActivity = (BaseActivity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = getViewBinding(inflater, container, savedInstanceState);
        //指出fragment愿意添加item到选项菜单
        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mInflater = onGetLayoutInflater(savedInstanceState);
        initPresenter();
        initEventAndData();
        initClickListener();
        // 界面加载完成
        mIsPrepared = true;
        baseLazyLoad();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }

    /**
     * 懒加载条件判断
     */
    @Deprecated
    private void baseLazyLoad() {
        if (mIsPrepared) {
            if (isVisible()) {
                lazyLoadShow();
            } else {
                lazyLoadHide();
            }

        }
    }

    @Override
    public void onDestroyView() {
        mIsPrepared = false;
        unSubscribe();
        removePresenter();
        mBinding = null;
        super.onDestroyView();
    }

    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(Event event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(Event event) {

    }

    /**
     * 接收到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected void receiveStickyEvent(Event event) {

    }

    /**
     * Toast
     *
     * @param msg
     */
    @Override
    public void showMsg(String msg) {
        ToastUtils.showShort(msg);
    }

    /**
     * 开启加载动画
     */
    @Override
    public void showProgress() {
        showTip(QMUITipDialog.Builder.ICON_TYPE_LOADING, "正在加载");
    }

    /**
     * 显示QmuiTip
     */
    @Override
    public void showTip(@QMUITipDialog.Builder.IconType int iconType, CharSequence tipWord) {
        if (getActivity() instanceof BaseActivity) {
            mActivity.showTip(iconType, tipWord);
        } else {
            if (mQmuiTipDialog == null) {
                mQmuiTipDialog = new QMUITipDialog.Builder(mContext)
                        .setIconType(iconType)
                        .setTipWord(tipWord)
                        .create();
            }
            if (!mQmuiTipDialog.isShowing()) {
                mQmuiTipDialog.show();
            }
        }
    }

    /**
     * 关闭加载动画
     */
    @Override
    public void hideProgress() {
        hideTip();
    }

    /**
     * 关闭QmuiTip
     */
    @Override
    public void hideTip() {
        if (getActivity() instanceof BaseActivity) {
            mActivity.hideTip();
        } else {
            if (mQmuiTipDialog != null) {
                if (mQmuiTipDialog.isShowing()) {
                    mQmuiTipDialog.dismiss();
                }
            }
        }
    }

    @Override
    public void startActivitySample(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
    }

    /**
     * RxJava 添加订阅者
     */
    @Override
    public void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    /**
     * RxJava 解除所有订阅者
     */
    public void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable.clear();
            mCompositeDisposable = new CompositeDisposable();
        }
    }

    protected void initPresenter() {

    }

    protected void removePresenter() {

    }

    /**
     * 加载布局
     */
    protected abstract V getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 加载数据
     */
    protected abstract void initEventAndData();

    /**
     * 点击事件
     */
    protected abstract void initClickListener();

    /**
     * 页面懒加载
     * <p>
     * ViewPager2已经可以实现原生懒加载
     */
    @Deprecated
    protected abstract void lazyLoadShow();

    /**
     * 页面懒加载
     * <p>
     * ViewPager2已经可以实现原生懒加载
     */
    @Deprecated
    protected void lazyLoadHide() {

    }
}