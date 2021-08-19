package io.github.sdwfqin.quicklib.webview;

import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebChromeClient;
import io.github.sdwfqin.quicklib.base.BaseActivity;
import io.github.sdwfqin.quicklib.databinding.QuickActivityWebViewBinding;

/**
 * 描述：WebViewActivity基类
 *
 * @author zhangqin
 * @date 2018/6/19
 */
public abstract class QuickBaseWebViewActivity extends BaseActivity<QuickActivityWebViewBinding> {

    protected String mUrl;
    protected AgentWeb mAgentWeb;

    @Override
    protected QuickActivityWebViewBinding getViewBinding() {
        return QuickActivityWebViewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {
        mNavBar.setTitle(getActivityTitle());

        mNavBar.addLeftBackImageButton().setOnClickListener(v -> {
            // true表示AgentWeb处理了该事件
            if (!mAgentWeb.back()) {
                finish();
            }
        });

        initWebView();
    }

    protected AgentWeb.CommonBuilder getWebViewCommonBuilder() {
        return AgentWeb.with(mContext)
                //传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .setAgentWebParent(mBinding.container, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()// 使用默认进度条
                .setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        if (isAutoSetTitle()) {
                            mNavBar.setTitle(title);
                        }
                    }
                });
    }

    protected void initWebView() {
        mAgentWeb = getWebViewCommonBuilder()
                //.defaultProgressBarColor() // 使用默认进度条颜色
                .createAgentWeb()
                .ready()
                .go(mUrl = getUrl());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb != null && mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();

    }

    @Override
    protected void onResume() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }

    /**
     * 设置url地址
     */
    public abstract String getUrl();

    /**
     * 设置标题
     */
    public String getActivityTitle() {
        return "";
    }

    /**
     * 是否监听标题变化
     */
    protected boolean isAutoSetTitle() {
        return true;
    }
}
