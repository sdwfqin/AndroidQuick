package com.sdwfqin.quicklib.webview;

import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.StringUtils;
import com.just.agentweb.AgentWeb;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quicklib.base.QuickArouterConstants;
import com.sdwfqin.quicklib.base.QuickConstants;
import com.sdwfqin.quicklib.databinding.QuickActivityWebViewBinding;

/**
 * 描述：WebViewActivity
 *
 * @author 张钦
 * @date 2018/1/16
 */
@Route(path = QuickArouterConstants.QUICK_WEBVIEWLOADDATA)
public class QuickWebViewLoadDataActivity extends BaseActivity<QuickActivityWebViewBinding> {

    private String mTitle;
    private String mContent;
    private String mBaseUrl;

    public static void launch(String title, String content) {
        launch("", title, content);
    }

    public static void launch(String baseUrl, String title, String content) {
        ARouter
                .getInstance()
                .build(QuickArouterConstants.QUICK_WEBVIEW)
                .withString("title", title)
                .withString("content", content)
                .withString("baseUrl", baseUrl)
                .navigation();
    }

    @Override
    protected QuickActivityWebViewBinding getViewBinding() {
        return QuickActivityWebViewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {

        mTitle = getIntent().getStringExtra("title");
        mContent = getIntent().getStringExtra("content");
        mBaseUrl = getIntent().getStringExtra("baseUrl");

        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        mTopBar.setTitle(mTitle);

        initWebView();
    }

    @Override
    protected void initClickListener() {

    }

    private void initWebView() {
        AgentWeb agentWeb = AgentWeb.with(mContext)
                //传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .setAgentWebParent(mBinding.container, new LinearLayout.LayoutParams(-1, -1))
                // 使用默认进度条
                .useDefaultIndicator()
                // .defaultProgressBarColor()
                .createAgentWeb()
                .ready()
                .go(null);
        agentWeb.getWebCreator().getWebView().setLayerType(View.LAYER_TYPE_NONE, null);
        if (StringUtils.isEmpty(mBaseUrl)) {
            agentWeb
                    .getUrlLoader()
                    .loadData(QuickConstants.HEAD +
                                    mContent +
                                    QuickConstants.END
                            , "text/html", "UTF-8");
        } else {
            agentWeb
                    .getUrlLoader()
                    .loadDataWithBaseURL(QuickConstants.BASE_URL,
                            QuickConstants.HEAD +
                                    mContent +
                                    QuickConstants.END
                            , "text/html", "UTF-8", null);
        }

    }
}
