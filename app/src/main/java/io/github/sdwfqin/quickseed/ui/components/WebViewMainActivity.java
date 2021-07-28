package io.github.sdwfqin.quickseed.ui.components;

import android.widget.ArrayAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import io.github.sdwfqin.quicklib.base.BaseActivity;
import io.github.sdwfqin.quicklib.webview.QuickWebViewActivity;
import io.github.sdwfqin.quicklib.webview.QuickWebViewLoadDataActivity;
import io.github.sdwfqin.quickseed.R;
import io.github.sdwfqin.quickseed.constants.ArouterConstants;
import io.github.sdwfqin.quickseed.databinding.ActivitySampleListBinding;
import io.github.sdwfqin.samplecommonlibrary.base.Constants;

/**
 * WebView示例
 * <p>
 *
 * @author 张钦
 * @date 2020/5/27
 */
@Route(path = ArouterConstants.COMPONENTS_WEBVIEW)
public class WebViewMainActivity extends BaseActivity<ActivitySampleListBinding> {

    private String[] mTitle = new String[]{
            "默认",
            "自定义",
            "加载本地html"
    };

    @Override
    protected ActivitySampleListBinding getViewBinding() {
        return ActivitySampleListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {
        mTopBar.setTitle("WebView Demo");
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());

        mBinding.list.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_list, R.id.tv_items, mTitle));
    }

    @Override
    protected void initClickListener() {
        mBinding.list.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i) {
                case 0:
                    QuickWebViewActivity.launch("https://www.baidu.com");
                    break;
                case 1:
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_CUSTOMWEBVIEW).navigation();
                    break;
                case 2:
                    QuickWebViewLoadDataActivity.launch(null, "测试标题", Constants.HEAD + "哈哈哈哈" + Constants.END, false);
                    break;
                default:
            }
        });
    }
}
