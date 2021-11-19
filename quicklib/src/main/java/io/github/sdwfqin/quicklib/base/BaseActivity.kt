package io.github.sdwfqin.quicklib.base

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.request.ExplainScope
import com.permissionx.guolindev.request.ForwardScope
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import io.github.sdwfqin.quicklib.R
import io.github.sdwfqin.quicklib.utils.eventbus.Event
import io.github.sdwfqin.quicklib.utils.eventbus.EventBusUtils
import io.github.sdwfqin.quicklib.utils.rx.RxJavaLifecycleManager
import io.github.sdwfqin.widget.StatusBarView
import io.reactivex.rxjava3.disposables.Disposable
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 描述：Activity基类
 *
 * @author 张钦
 */
abstract class BaseActivity<V : ViewBinding> : AppCompatActivity(), IBaseActivity {

    protected lateinit var mContext: Activity
    protected lateinit var mBinding: V
    private lateinit var mQuickBaseView: LinearLayoutCompat

    /**
     * 顶部状态栏
     */
    protected lateinit var mStatusBar: StatusBarView

    /**
     * 顶部标题栏
     */
    protected lateinit var mNavBar: QMUITopBarLayout

    /**
     * TipDialog
     */
    private var mQmuiTipDialog: QMUITipDialog? = null

    /**
     * Rxjava 生命周期管理
     */
    private lateinit var mRxJavaLifecycleManager: RxJavaLifecycleManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimaryDark))
        mBinding = getViewBinding()
        initContentView()
        mStatusBar = findViewById(R.id.quick_base_status_bar)
        mNavBar = findViewById(R.id.quick_base_nav_bar)
        mContext = this
        mRxJavaLifecycleManager = RxJavaLifecycleManager(this)
        initViewModel()
        initEventAndData()
        initListener()
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        when (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                // 夜间模式未启用，使用浅色主题
                reloadActivity()
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                // 夜间模式启用，使用深色主题
                reloadActivity()
            }
        }
    }

    private fun initContentView() {
        val quickBaseViewGroup = this.layoutInflater.inflate(R.layout.quick_activity_base, null)
        mQuickBaseView = quickBaseViewGroup.findViewById(R.id.quick_base_view)
        mBinding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mQuickBaseView.addView(mBinding.root)
        setContentView(quickBaseViewGroup)
    }

    protected fun addSubscribe(disposable: Disposable) {
        mRxJavaLifecycleManager.addDisposable(disposable)
    }

    override fun getActivity(): Activity {
        return this
    }

    /**
     * 重启activity
     */
    open fun reloadActivity() {
        val intent:Intent = intent;
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    // ==================== EventBus事件 ====================

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

    // ==================== Toast ====================

    /**
     * Toast
     *
     * @param msg
     */
    override fun showMsg(msg: String) {
        ToastUtils.showShort(msg)
    }

    // ==================== QmuiTip(加载动画) ====================
    /**
     * 开启加载动画
     */
    override fun showProgress() {
        showTip(QMUITipDialog.Builder.ICON_TYPE_LOADING, "正在加载")
    }

    /**
     * 显示QmuiTip
     */
    override fun showTip(@QMUITipDialog.Builder.IconType iconType: Int, tipWord: CharSequence) {
        mQmuiTipDialog ?: let {
            mQmuiTipDialog = QMUITipDialog.Builder(mContext)
                .setIconType(iconType)
                .setTipWord(tipWord)
                .create()
        }

        mQmuiTipDialog?.let {
            if (!it.isShowing) {
                it.show()
            }
        }
    }

    /**
     * 关闭加载动画
     */
    override fun hideProgress() {
        hideTip()
    }

    /**
     * 关闭QmuiTip
     */
    override fun hideTip() {
        mQmuiTipDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        mQmuiTipDialog = null
    }

    override fun startActivitySample(cls: Class<*>) {
        val intent = Intent(mContext, cls)
        startActivity(intent)
    }

    // ==================== 权限管理 ====================

    interface OnPermissionCallback {
        fun onSuccess()
        fun onError()
    }

    /**
     * 初始化权限检查
     *
     * @param perms                权限列表
     * @param onPermissionCallback 权限回掉接口
     */
    protected open fun initCheckPermissions(
        perms: Array<String>,
        onPermissionCallback: OnPermissionCallback
    ) {
        PermissionX.init(this)
            .permissions(*perms)
            .onExplainRequestReason { scope: ExplainScope, deniedList: List<String>, beforeRequest: Boolean ->
                scope.showRequestReasonDialog(
                    deniedList, getString(
                        R.string.quick_permissions_title, AppUtils.getAppName()
                    ), getString(R.string.quick_permissions_dialog_submit), getString(
                        R.string.quick_permissions_dialog_cancel
                    )
                )
            }
            .onForwardToSettings { scope: ForwardScope, deniedList: List<String> ->
                scope.showForwardToSettingsDialog(
                    deniedList, getString(
                        R.string.quick_permissions_forward
                    ), getString(R.string.quick_permissions_dialog_submit), getString(
                        R.string.quick_permissions_dialog_cancel
                    )
                )
            }
            .request { allGranted: Boolean, grantedList: List<String>, deniedList: List<String?>? ->
                if (allGranted) {
                    onPermissionCallback.onSuccess()
                } else {
                    onPermissionCallback.onError()
                }
            }
    }

    // ==================== 提供的接口 ====================

    protected open fun initViewModel() {}

    /**
     * 加载布局
     */
    protected abstract fun getViewBinding(): V

    /**
     * 加载数据
     */
    protected abstract fun initEventAndData()

    /**
     * 监听器
     */
    protected open fun initListener() {}

    /**
     * 点击事件
     */
    protected open fun initClickListener() {}
}