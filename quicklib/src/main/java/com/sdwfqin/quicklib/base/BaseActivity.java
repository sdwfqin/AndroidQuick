package com.sdwfqin.quicklib.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.arch.QMUIActivity;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.utils.AppManager;
import com.sdwfqin.quicklib.utils.eventbus.Event;
import com.sdwfqin.quicklib.utils.eventbus.EventBusUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 描述：Activity基类
 *
 * @author 张钦
 */
public abstract class BaseActivity extends QMUIActivity implements BaseView, EasyPermissions.PermissionCallbacks {

    private static final int PERMS_REQUEST_CODE = 1122;

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
    /**
     * 权限相关
     */
    private String[] mPerms;
    private OnPermissionCallback mOnPermissionCallback;
    private boolean mShowPermissionsDialog;
    private boolean mLoopPermissionsDialog;

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
    public void initCheckPermissions(String[] perms, OnPermissionCallback onPermissionCallback) {
        initCheckPermissions(perms, false, false, onPermissionCallback);
    }

    /**
     * 初始化权限检查
     *
     * @param perms                 权限列表
     * @param showPermissionsDialog true：拒绝显示弹窗
     * @param loopPermissionsDialog true：弹窗关闭继续弹出弹窗
     * @param onPermissionCallback  权限回掉接口
     */
    public void initCheckPermissions(String[] perms, boolean showPermissionsDialog, boolean loopPermissionsDialog,
                                     OnPermissionCallback onPermissionCallback) {
        mPerms = perms;
        mShowPermissionsDialog = showPermissionsDialog;
        mLoopPermissionsDialog = loopPermissionsDialog;
        mOnPermissionCallback = onPermissionCallback;

        checkPermissions();
    }

    /**
     * 检查权限是否全部获取
     */
    @AfterPermissionGranted(PERMS_REQUEST_CODE)
    public void checkPermissions() {

        if (EasyPermissions.hasPermissions(this, mPerms)) {
            // Already have permission, do the thing
            mOnPermissionCallback.onSuccess();
        } else {
            // Do not have permissions, request them now
            if (mShowPermissionsDialog) {
                EasyPermissions.requestPermissions(this, getString(R.string.quick_permissions_check_error),
                        PERMS_REQUEST_CODE, mPerms);
            } else {
                mOnPermissionCallback.onError();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // Some permissions have been granted
        if (perms.size() >= mPerms.length) {
            mOnPermissionCallback.onSuccess();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // Some permissions have been denied
        if (mLoopPermissionsDialog) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                AppSettingsDialog.Builder builder = new AppSettingsDialog.Builder(this);
                AppSettingsDialog appSettingsDialog = builder.setTitle(R.string.quick_permissions_dialog_title_settings)
                        .setRationale(R.string.quick_permissions_dialog_rationale_ask_again)
                        .setNegativeButton(R.string.quick_permissions_dialog_cancel)
                        .setPositiveButton(R.string.quick_permissions_dialog_to_setting)
                        .build();
                appSettingsDialog.show();
            } else {
                checkPermissions();
            }
        } else {
            mOnPermissionCallback.onError();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            checkPermissions();
        }
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