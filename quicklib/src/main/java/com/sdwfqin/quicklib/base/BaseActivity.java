package com.sdwfqin.quicklib.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.utils.AppManager;
import com.sdwfqin.quicklib.utils.eventbus.Event;
import com.sdwfqin.quicklib.utils.eventbus.EventBusUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * 描述：Activity基类
 *
 * @author 张钦
 */
public abstract class BaseActivity extends SwipeBackActivity implements BaseView {

    protected Activity mContext;
    protected LinearLayout mRoot_view;
    /**
     * Rxjava 订阅管理
     */
    protected CompositeDisposable mCompositeDisposable;
    /**
     * 顶部标题栏
     */
    protected QMUITopBar mTopBar;
    /**
     * TipDialog
     */
    protected QMUITipDialog mQmuiTipDialog;
    /**
     * 侧滑关闭
     */
    protected SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.layout.activity_base);
        setContentView(getLayout());
        mTopBar = (QMUITopBar) findViewById(R.id.base_topbar);
        ButterKnife.bind(this);
        mContext = this;
        mSwipeBackLayout = getSwipeBackLayout();
        if (isStartSwipeBack()) {
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        } else {
            mSwipeBackLayout.setEnableGesture(false);
        }
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
        removePresenter();
        unSubscribe();
        AppManager.removeActivity(this);
        super.onDestroy();
    }

    private void initContentView(@LayoutRes int layoutResID) {
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        mRoot_view = new LinearLayout(this);
        mRoot_view.setOrientation(LinearLayout.VERTICAL);
        //  add mRoot_view in viewGroup
        viewGroup.addView(mRoot_view);
        //  add the layout of BaseActivity in mRoot_view
        LayoutInflater.from(this).inflate(layoutResID, mRoot_view, true);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        //  added the sub-activity layout id in mRoot_view
        LayoutInflater.from(this).inflate(layoutResID, mRoot_view, true);
    }

    /**
     * 开启侧滑关闭
     */
    public boolean isStartSwipeBack() {
        return true;
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

    /**
     * RxJava 添加订阅者
     */
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
    protected abstract int getLayout();

    /**
     * 加载数据
     */
    protected abstract void initEventAndData();
}