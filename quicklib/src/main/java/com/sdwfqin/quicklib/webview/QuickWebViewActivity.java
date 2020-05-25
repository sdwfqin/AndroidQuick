package com.sdwfqin.quicklib.webview;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sdwfqin.quicklib.base.QuickArouterConstants;

/**
 * 描述：WebViewActivity
 *
 * @author 张钦
 * @date 2018/1/16
 */
@Route(path = QuickArouterConstants.QUICK_WEBVIEW)
public class QuickWebViewActivity extends QuickBaseWebViewActivity {

    /**
     * 加载网页
     *
     * @param url 页面地址
     */
    public static void launch(String url) {
        launch(url, null);
    }

    /**
     * 加载网页，带有默认标题
     *
     * @param url   页面地址
     * @param title 默认标题
     */
    public static void launch(String url, String title) {
        ARouter
                .getInstance()
                .build(QuickArouterConstants.QUICK_WEBVIEW)
                .withString("url", url)
                .withString("title", title)
                .navigation();
    }

    @Override
    public String getUrl() {
        return getIntent().getStringExtra("url");
    }

    @Override
    public String getActivityTitle() {
        if (getIntent().getStringExtra("title") != null) {
            return getIntent().getStringExtra("title");
        }
        return super.getActivityTitle();
    }
}
