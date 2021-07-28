package io.github.sdwfqin.samplecommonlibrary.base;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import io.github.sdwfqin.quicklib.base.BaseActivity;

import io.github.sdwfqin.samplecommonlibrary.utils.skin.QMUISkinCustManager;

/**
 * 当前模块的BaseActivity
 * <p>
 *
 * @author 张钦
 * @date 2019-12-06
 */
public abstract class SampleBaseActivity<V extends ViewBinding> extends BaseActivity<V> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSkinManager(QMUISkinManager.defaultInstance(this));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
