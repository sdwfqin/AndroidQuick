package com.sdwfqin.quickseed.base;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quickseed.utils.skin.QMUISkinCustManager;

/**
 * 当前模块的BaseActivity
 * <p>
 *
 * @author 张钦
 * @date 2019-12-06
 */
public abstract class SampleBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
        QMUISkinManager.defaultInstance(this).addSkinChangeListener(mOnSkinChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        QMUISkinManager.defaultInstance(this).removeSkinChangeListener(mOnSkinChangeListener);
    }

    private QMUISkinManager.OnSkinChangeListener mOnSkinChangeListener = (oldSkin, newSkin) -> {
        if (newSkin == QMUISkinCustManager.SKIN_BLUE) {
            QMUIStatusBarHelper.setStatusBarLightMode(mContext);
        } else {
            QMUIStatusBarHelper.setStatusBarDarkMode(mContext);
        }
    };

}
