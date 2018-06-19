package com.sdwfqin.quicklib.module.webview;

import android.content.Context;
import android.content.Intent;

/**
 * 描述：WebViewActivity
 *
 * @author 张钦
 * @date 2018/1/16
 */
public class WebViewActivity extends BaseWebView {

    /**
     * 加载网页
     *
     * @param context
     * @param url     页面地址
     */
    public static void launch(Context context, String url) {
        Intent i = new Intent(context, WebViewActivity.class);
        i.putExtra("url", url);
        context.startActivity(i);
    }

    /**
     * 加载网页，带有默认标题
     *
     * @param context
     * @param url     页面地址
     * @param title   默认标题
     */
    public static void launch(Context context, String url, String title) {
        Intent i = new Intent(context, WebViewActivity.class);
        i.putExtra("url", url);
        i.putExtra("title", title);
        context.startActivity(i);
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
