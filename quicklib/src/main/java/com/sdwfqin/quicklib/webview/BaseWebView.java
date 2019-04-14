package com.sdwfqin.quicklib.webview;

import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.StringUtils;
import com.just.agentweb.AgentWeb;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.base.BaseActivity;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/6/19
 */
public abstract class BaseWebView extends BaseActivity {
    protected LinearLayout mContainer;

    protected String mUrl;
    protected AgentWeb mAgentWeb;

    @Override
    protected int getLayout() {
        return R.layout.quick_activity_web_view;
    }

    @Override
    protected void initEventAndData() {

        mContainer = (LinearLayout) findViewById(R.id.container);

        if (!StringUtils.isEmpty(getUrl())) {
            mUrl = getUrl();
        } else {
            showMsg("未获取到url地址");
            finish();
        }

        mTopBar.setTitle(getActivityTitle());

        mTopBar.addLeftBackImageButton().setOnClickListener(v -> {
            // true表示AgentWeb处理了该事件
            if (!mAgentWeb.back()) {
                finish();
            }
        });

        initWebView();
    }

    private void initWebView() {
        mAgentWeb = AgentWeb.with(mContext)
                //传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .setAgentWebParent(mContainer, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()// 使用默认进度条
                .setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        mTopBar.setTitle(title);
                    }
                })
                //.defaultProgressBarColor() // 使用默认进度条颜色
                .createAgentWeb()
                .ready()
                .go(mUrl);
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
     *
     * @return
     */
    public abstract String getUrl();

    /**
     * 设置标题
     *
     * @return
     */
    public String getActivityTitle() {
        return "";
    }
}
