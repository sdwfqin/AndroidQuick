package com.sdwfqin.quicklib.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.arch.QMUIActivity;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.utils.AppManager;
import com.sdwfqin.quicklib.utils.eventbus.Event;
import com.sdwfqin.quicklib.utils.eventbus.EventBusUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 描述：Activity基类
 *
 * @author 张钦
 */
public abstract class BaseActivity extends QMUIActivity implements BaseView {

    protected Activity mContext;
    protected LinearLayoutCompat mQuickBaseView;
    /**
     * Rxjava 订阅管理
     */
    protected CompositeDisposable mCompositeDisposable;
    /**
     * 顶部标题栏
     */
    protected QMUITopBarLayout mTopBar;
    /**
     * TipDialog
     */
    protected QMUITipDialog mQmuiTipDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(getLayout());
        mTopBar = findViewById(R.id.quick_base_topbar);
        ButterKnife.bind(this);
        mContext = this;
        AppManager.addActivity(this);
        initPresenter();
        initEventAndData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }

    @Override
    protected void onDestroy() {
        unSubscribe();
        removePresenter();
        AppManager.removeActivity(this);
        super.onDestroy();
    }

    private void initContentView(@LayoutRes int layoutResID) {
        View quickBaseViewGroup = this.getLayoutInflater().inflate(R.layout.quick_activity_base, null);
        mQuickBaseView = quickBaseViewGroup.findViewById(R.id.quick_base_view);
        LayoutInflater.from(this).inflate(layoutResID, mQuickBaseView, true);
        setContentView(quickBaseViewGroup);
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

    // ==================== RxJava订阅管理 ====================

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

    // ==================== 权限管理 ====================

    public interface OnPermissionCallback {

        void onSuccess();

        void onError();
    }

    /**
     * 检查权限是否全部获取
     *
     * @param perms
     * @param onPermissionCallback
     */
    public void checkPermissionsSample(String[] perms, OnPermissionCallback onPermissionCallback) {
        checkPermissions(perms, false, false, onPermissionCallback);
    }

    /**
     * 检查权限是否全部获取
     *
     * @param perms                权限列表
     * @param showDialog           true：拒绝显示弹窗
     * @param allDialog            true：弹窗关闭继续弹出弹窗
     * @param onPermissionCallback 权限回掉接口
     */
    public void checkPermissions(String[] perms, boolean showDialog, boolean allDialog,
                                 OnPermissionCallback onPermissionCallback) {

        addSubscribe(new RxPermissions(this)
                .requestEachCombined(perms)
                .subscribe(permission -> {
                    if (permission.granted) {
                        onPermissionCallback.onSuccess();
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        if (showDialog) {
                            showPermissionsDialog(perms, false, allDialog, onPermissionCallback);
                        } else {
                            onPermissionCallback.onError();
                        }
                    } else {
                        if (showDialog) {
                            showPermissionsDialog(perms, true, allDialog, onPermissionCallback);
                        } else {
                            onPermissionCallback.onError();
                        }
                    }
                }));
    }

    /**
     * 拒绝权限弹窗
     *
     * @param allDialog
     */
    private void showPermissionsDialog(String[] perms, boolean isNever, boolean allDialog, OnPermissionCallback onPermissionCallback) {
        QMUIDialog.MessageDialogBuilder messageDialogBuilder = new QMUIDialog.MessageDialogBuilder(mContext);
        messageDialogBuilder
                .setMessage("获取权限失败！")
                .addAction("开启权限", (dialog, index) -> {
                    if (!isNever) {
                        dialog.dismiss();
                        checkPermissions(perms, true, allDialog, onPermissionCallback);
                    } else {
                        if (PermissionUtils.isGranted(perms)) {
                            onPermissionCallback.onSuccess();
                            dialog.dismiss();
                        } else {
                            PermissionUtils.launchAppDetailsSettings();
                        }
                    }
                })
                .addAction("取消", (dialog, index) -> {
                    if (allDialog) {
                        showPermissionsDialog(perms, isNever, true, onPermissionCallback);
                    } else {
                        onPermissionCallback.onError();
                    }
                    dialog.dismiss();
                })
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .show();
    }

    // ==================== 提供的接口 ====================

    protected void initPresenter() {

    }

    protected void removePresenter() {

    }

    /**
     * 加载布局
     */
    protected abstract int getLayout();

    /**
     * 加载数据
     */
    protected abstract void initEventAndData();
}