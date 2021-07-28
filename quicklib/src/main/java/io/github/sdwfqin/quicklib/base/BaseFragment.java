package io.github.sdwfqin.quicklib.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import io.github.sdwfqin.quicklib.utils.eventbus.Event;
import io.github.sdwfqin.quicklib.utils.eventbus.EventBusUtils;
import io.github.sdwfqin.quicklib.utils.rx.RxJavaLifecycleManager;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 描述：Fragment基类
 *
 * @author 张钦
 * @date 2017/8/3
 */
public abstract class BaseFragment<V extends ViewBinding> extends Fragment {

    protected V mBinding;
    protected IBaseActivity mBaseActivity;
    protected Context mContext;
    protected LayoutInflater mInflater;
    private RxJavaLifecycleManager mRxJavaLifecycleManager;

    @Override
    public void onAttach(@NotNull Context context) {
        if (context instanceof IBaseActivity) {
            mBaseActivity = (IBaseActivity) context;
        } else {
            throw new ClassCastException("BaseFragment下属Fragment应依附与BaseActivity");
        }
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = getViewBinding(inflater, container, savedInstanceState);
        //指出fragment愿意添加item到选项菜单
        setHasOptionsMenu(true);
        mRxJavaLifecycleManager = new RxJavaLifecycleManager(this);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mInflater = onGetLayoutInflater(savedInstanceState);
        initViewModel();
        initEventAndData();
        initClickListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isRegisterEventBus()) {
            EventBusUtils.register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRegisterEventBus()) {
            EventBusUtils.unregister(this);
        }
    }

    @Override
    public void onDestroyView() {
        mRxJavaLifecycleManager.dispose();
        mBinding = null;
        super.onDestroyView();
    }

    protected void addSubscribe(Disposable disposable) {
        mRxJavaLifecycleManager.addDisposable(disposable);
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
    public void onEventBusCome(Event<Object> event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(Event<Object> event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(Event<Object> event) {

    }

    /**
     * 接收到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected void receiveStickyEvent(Event<Object> event) {

    }

    protected void initViewModel() {

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
    protected void initClickListener() {

    }

}