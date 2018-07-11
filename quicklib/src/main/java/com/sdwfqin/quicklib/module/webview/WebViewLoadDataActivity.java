package com.sdwfqin.quicklib.module.webview;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.StringUtils;
import com.just.agentweb.AgentWeb;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quicklib.base.QuickConstants;

/**
 * 描述：WebViewActivity
 *
 * @author 张钦
 * @date 2018/1/16
 */
public class WebViewLoadDataActivity extends BaseActivity {

    LinearLayout mContainer;

    private String mTitle;
    private String mContent;
    private String mBaseUrl;

    public static void launch(Context context, String title, String content) {
        Intent i = new Intent(context, WebViewLoadDataActivity.class);
        i.putExtra("title", title);
        i.putExtra("content", content);
        context.startActivity(i);
    }

    public static void launch(Context context, String baseUrl, String title, String content) {
        Intent i = new Intent(context, WebViewLoadDataActivity.class);
        i.putExtra("title", title);
        i.putExtra("content", content);
        i.putExtra("baseUrl", baseUrl);
        context.startActivity(i);
    }

    @Override
    protected int getLayout() {
        return R.layout.quick_activity_web_view;
    }

    @Override
    protected void initEventAndData() {

        mContainer = (LinearLayout) findViewById(R.id.container);

        mTitle = getIntent().getStringExtra("title");
        mContent = getIntent().getStringExtra("content");
        mBaseUrl = getIntent().getStringExtra("baseUrl");

        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        mTopBar.setTitle(mTitle);

        initWebView();
    }

    private void initWebView() {
        AgentWeb agentWeb = AgentWeb.with(mContext)
                //传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .setAgentWebParent(mContainer, new LinearLayout.LayoutParams(-1, -1))
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
