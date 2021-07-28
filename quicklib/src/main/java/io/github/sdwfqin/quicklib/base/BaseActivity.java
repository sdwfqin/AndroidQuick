package io.github.sdwfqin.quicklib.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewbinding.ViewBinding;

import com.blankj.utilcode.util.ToastUtils;
import com.permissionx.guolindev.PermissionX;
import com.qmuiteam.qmui.arch.QMUIActivity;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.github.sdwfqin.quicklib.R;
import io.github.sdwfqin.quicklib.utils.eventbus.Event;
import io.github.sdwfqin.quicklib.utils.eventbus.EventBusUtils;
import io.github.sdwfqin.quicklib.utils.rx.RxJavaLifecycleManager;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 描述：Activity基类
 *
 * @author 张钦
 */
public abstract class BaseActivity<V extends ViewBinding> extends QMUIActivity implements IBaseActivity {

    protected Activity mContext;
    protected V mBinding;
    protected LinearLayoutCompat mQuickBaseView;
    /**
     * 顶部标题栏
     */
    protected QMUITopBarLayout mTopBar;
    /**
     * TipDialog
     */
    protected QMUITipDialog mQmuiTipDialog;
    private RxJavaLifecycleManager mRxJavaLifecycleManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = getViewBinding();
        initContentView();
        mTopBar = findViewById(R.id.quick_base_topbar);
        mContext = this;
        mRxJavaLifecycleManager = new RxJavaLifecycleManager(this);
        initViewModel();
        initEventAndData();
        initListener();
        initClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isRegisterEventBus()) {
            EventBusUtils.register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isRegisterEventBus()) {
            EventBusUtils.unregister(this);
        }
    }

    private void initContentView() {
        View quickBaseViewGroup = this.getLayoutInflater().inflate(R.layout.quick_activity_base, null);
        mQuickBaseView = quickBaseViewGroup.findViewById(R.id.quick_base_view);
        mBinding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mQuickBaseView.addView(mBinding.getRoot());
        setContentView(quickBaseViewGroup);
    }

    @Override
    protected void onDestroy() {
        mRxJavaLifecycleManager.dispose();
        super.onDestroy();
    }

    protected void addSubscribe(Disposable disposable) {
        mRxJavaLifecycleManager.addDisposable(disposable);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    // ==================== EventBus事件 ====================

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

    // ==================== Toast ====================

    /**
     * Toast
     *
     * @param msg
     */
    @Override
    public void showMsg(String msg) {
        ToastUtils.showShort(msg);
    }

    // ==================== QmuiTip(加载动画) ====================

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
        if (mQmuiTipDialog != null) {
            if (mQmuiTipDialog.isShowing()) {
                mQmuiTipDialog.dismiss();
            }
        }
    }

    @Override
    public void startActivitySample(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
    }

    // ==================== 权限管理 ====================

    public interface OnPermissionCallback {

        void onSuccess();

        void onError();
    }

    /**
     * 初始化权限检查
     *
     * @param perms                权限列表
     * @param onPermissionCallback 权限回掉接口
     */
    public void initCheckPermissions(String[] perms, OnPermissionCallback onPermissionCallback) {
        PermissionX.init(this)
                .permissions(perms)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> {
                    scope.showRequestReasonDialog(deniedList, getString(R.string.quick_permissions_title, getString(R.string.app_name)), getString(R.string.quick_permissions_dialog_submit), getString(R.string.quick_permissions_dialog_cancel));
                })
                .onForwardToSettings((scope, deniedList) -> {
                    scope.showForwardToSettingsDialog(deniedList, getString(R.string.quick_permissions_forward), getString(R.string.quick_permissions_dialog_submit), getString(R.string.quick_permissions_dialog_cancel));
                })
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        onPermissionCallback.onSuccess();
                    } else {
                        onPermissionCallback.onError();
                    }
                });
    }
    // ==================== 提供的接口 ====================

    protected void initViewModel() {

    }

    /**
     * 加载布局
     */
    protected abstract V getViewBinding();

    /**
     * 加载数据
     */
    protected abstract void initEventAndData();

    /**
     * 监听器
     */
    protected void initListener() {

    }

    /**
     * 点击事件
     */
    protected void initClickListener() {

    }
}