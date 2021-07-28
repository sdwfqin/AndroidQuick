package io.github.sdwfqin.quicklib.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import io.github.sdwfqin.quicklib.utils.eventbus.Event
import io.github.sdwfqin.quicklib.utils.eventbus.EventBusUtils
import io.github.sdwfqin.quicklib.utils.rx.RxJavaLifecycleManager
import io.reactivex.rxjava3.disposables.Disposable
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 描述：Fragment基类
 *
 * @author 张钦
 * @date 2017/8/3
 */
abstract class BaseFragment<V : ViewBinding> : Fragment() {

    protected lateinit var mBinding: V
    protected lateinit var mBaseActivity: IBaseActivity
    protected lateinit var mContext: Context
    protected lateinit var mInflater: LayoutInflater
    private lateinit var mRxJavaLifecycleManager: RxJavaLifecycleManager

    override fun onAttach(context: Context) {
        mBaseActivity = if (context is IBaseActivity) {
            context
        } else {
            throw ClassCastException("BaseFragment下属Fragment应依附与BaseActivity")
        }
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = getViewBinding(inflater, container, savedInstanceState)
        //指出fragment愿意添加item到选项菜单
        setHasOptionsMenu(true)
        mRxJavaLifecycleManager = RxJavaLifecycleManager(this)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mInflater = onGetLayoutInflater(savedInstanceState)
        initViewModel()
        initEventAndData()
        initClickListener()
    }

    override fun onStart() {
        super.onStart()
        if (isRegisterEventBus) {
            EventBusUtils.register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        if (isRegisterEventBus) {
            EventBusUtils.unregister(this)
        }
    }

    protected fun addSubscribe(disposable: Disposable) {
        mRxJavaLifecycleManager.addDisposable(disposable)
    }

    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected open val isRegisterEventBus: Boolean
        get() = false

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventBusCome(event: Event<Any?>?) {
        event?.let { receiveEvent(it) }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onStickyEventBusCome(event: Event<Any?>?) {
        event?.let { receiveStickyEvent(it) }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected open fun receiveEvent(event: Event<Any?>) {}

    /**
     * 接收到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected open fun receiveStickyEvent(event: Event<Any?>) {}

    protected open fun initViewModel() {}

    /**
     * 加载布局
     */
    protected abstract fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): V

    /**
     * 加载数据
     */
    protected abstract fun initEventAndData()

    /**
     * 点击事件
     */
    protected open fun initClickListener() {}
}