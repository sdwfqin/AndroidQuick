package com.sdwfqin.quickseed.ui.components;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.sdwfqin.quicklib.webview.BaseWebView;
import com.sdwfqin.quickseed.base.ArouterConstants;
import com.sdwfqin.quickseed.utils.skin.QMUISkinCustManager;

/**
 * 描述：自定义Webview
 *
 * @author zhangqin
 * @date 2018/6/19
 */
@Route(path = ArouterConstants.COMPONENTS_CUSTOMWEBVIEW)
public class CustomWebviewActivity extends BaseWebView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSkinManager(QMUISkinManager.defaultInstance(this));
    }

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
        if (getSkinManager() != null) {
            getSkinManager().addSkinChangeListener(mOnSkinChangeListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getSkinManager() != null) {
            getSkinManager().removeSkinChangeListener(mOnSkinChangeListener);
        }
    }

    private QMUISkinManager.OnSkinChangeListener mOnSkinChangeListener = (skinManager, oldSkin, newSkin) -> {
        if (newSkin == QMUISkinCustManager.SKIN_BLUE) {
            QMUIStatusBarHelper.setStatusBarLightMode(mContext);
        } else {
            QMUIStatusBarHelper.setStatusBarDarkMode(mContext);
        }
    };
}
