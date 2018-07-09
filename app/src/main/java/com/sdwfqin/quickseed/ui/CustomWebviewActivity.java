package com.sdwfqin.quickseed.ui;

import com.sdwfqin.quicklib.module.webview.BaseWebView;

/**
 * 描述：自定义Webview
 *
 * @author zhangqin
 * @date 2018/6/19
 */
public class CustomWebviewActivity extends BaseWebView {

    @Override
    public String getUrl() {
        return "https://www.sdwfqin.com";
    }

    @Override
    public String getActivityTitle() {
        return "sdwfqin.com";
    }
}
