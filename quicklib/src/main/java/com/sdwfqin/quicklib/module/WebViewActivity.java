package com.sdwfqin.quicklib.module;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.ChromeClientCallbackManager;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.base.BaseActivity;

/**
 * 描述：WebViewActivity
 *
 * @author 张钦
 * @date 2018/1/16
 */
public class WebViewActivity extends BaseActivity {

    QMUITopBarLayout mTopbar;
    LinearLayout mContainer;

    private String mUrl;
    private AgentWeb mAgentWeb;

    public static void launch(Context context, String url) {
        Intent i = new Intent(context, WebViewActivity.class);
        i.putExtra("url", url);
        context.startActivity(i);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initEventAndData() {

        mTopbar = findViewById(R.id.topbar);
        mContainer = findViewById(R.id.container);

        if (getIntent().getStringExtra("url") != null) {
            mUrl = getIntent().getStringExtra("url");
        } else {
            showMsg("未获取到url地址");
            finish();
        }

        mTopbar.addLeftBackImageButton().setOnClickListener(v -> {
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
                .defaultProgressBarColor() // 使用默认进度条颜色
                .setReceivedTitleCallback(getReceivedTitleCallback()) //设置 Web 页面的 title 回调
                .createAgentWeb()
                .ready()
                .go(mUrl);
    }

    private @Nullable
    ChromeClientCallbackManager.ReceivedTitleCallback getReceivedTitleCallback() {
        return (view, title) -> mTopbar.setTitle(title);
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
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

}
