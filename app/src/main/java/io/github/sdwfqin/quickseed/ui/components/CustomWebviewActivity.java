package io.github.sdwfqin.quickseed.ui.components;

import com.alibaba.android.arouter.facade.annotation.Route;

import io.github.sdwfqin.quicklib.webview.QuickBaseWebViewActivity;
import io.github.sdwfqin.quickseed.constants.ArouterConstants;

/**
 * 描述：自定义Webview
 *
 * @author zhangqin
 * @date 2018/6/19
 */
@Route(path = ArouterConstants.COMPONENTS_CUSTOMWEBVIEW)
public class CustomWebviewActivity extends QuickBaseWebViewActivity {

    @Override
    public String getUrl() {
        return "https://www.baidu.com";
    }

    @Override
    public String getActivityTitle() {
        return "默认自定义标题";
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
