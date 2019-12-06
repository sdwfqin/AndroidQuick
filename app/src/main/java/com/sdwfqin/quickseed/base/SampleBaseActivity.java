package com.sdwfqin.quickseed.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.sdwfqin.quicklib.base.BaseActivity;

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

        // 设置状态栏黑色字体图标
        QMUIStatusBarHelper.setStatusBarLightMode(mContext);
    }
}
