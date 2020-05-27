package com.sdwfqin.quicklib.webview;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.StringUtils;
import com.sdwfqin.quicklib.base.QuickArouterConstants;
import com.sdwfqin.quicklib.base.QuickConstants;

/**
 * 描述：加载htmlData的WebViewActivity
 *
 * @author 张钦
 * @date 2018/1/16
 */
@Route(path = QuickArouterConstants.QUICK_WEBVIEWLOADDATA)
public class QuickWebViewLoadDataActivity extends QuickBaseWebViewActivity {

    private String mContent;
    private String mBaseUrl;

    /**
     * @param title   标题
     * @param content html文本
     */
    public static void launch(String title, String content) {
        launch("", title, content);
    }

    /**
     * @param baseUrl
     * @param title   标题
     * @param content html文本
     */
    public static void launch(String baseUrl, String title, String content) {
        launch("", title, content, true);
    }

    /**
     * @param baseUrl
     * @param title        标题
     * @param content      html文本
     * @param autoSetTitle 是否自动监听设置标题
     */
    public static void launch(String baseUrl, String title, String content, boolean autoSetTitle) {
        ARouter
                .getInstance()
                .build(QuickArouterConstants.QUICK_WEBVIEWLOADDATA)
                .withString("title", title)
                .withString("content", content)
                .withString("baseUrl", baseUrl)
                .withBoolean("autoSetTitle", autoSetTitle)
                .navigation();
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();

        mContent = getIntent().getStringExtra("content");
        mBaseUrl = getIntent().getStringExtra("baseUrl");

        loadDataHtml();
    }

    private void loadDataHtml() {
        mAgentWeb.getWebCreator().getWebView().setLayerType(View.LAYER_TYPE_NONE, null);
        if (StringUtils.isEmpty(mBaseUrl)) {
            mAgentWeb
                    .getUrlLoader()
                    .loadData(mContent, "text/html", "UTF-8");
        } else {
            mAgentWeb
                    .getUrlLoader()
                    .loadDataWithBaseURL(QuickConstants.BASE_URL, mContent, "text/html", "UTF-8", null);
        }
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public String getActivityTitle() {
        return getIntent().getStringExtra("title");
    }

    @Override
    protected boolean isAutoSetTitle() {
        return getIntent().getBooleanExtra("autoSetTitle", true);
    }
}
