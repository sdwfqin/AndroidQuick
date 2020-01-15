package com.sdwfqin.quickseed.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

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

        if (QMUISkinCustManager.getCurrentSkin() == QMUISkinCustManager.SKIN_BLUE) {
            // 设置状态栏黑色字体图标
            QMUIStatusBarHelper.setStatusBarLightMode(mContext);
        } else if (QMUISkinCustManager.getCurrentSkin() == QMUISkinCustManager.SKIN_DARK) {
            // 设置状态栏白色字体图标
            QMUIStatusBarHelper.setStatusBarDarkMode(mContext);
        }
    }
}
