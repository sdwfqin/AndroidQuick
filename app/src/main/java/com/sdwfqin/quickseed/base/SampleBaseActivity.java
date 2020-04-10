package com.sdwfqin.quickseed.base;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.viewbinding.ViewBinding;

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
public abstract class SampleBaseActivity<V extends ViewBinding> extends BaseActivity<V> {

    private QMUISkinManager mSkinManager;

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
        if(mSkinManager != null){
            mSkinManager.register(this);
        }
        if(getSkinManager() != null){
            getSkinManager().addSkinChangeListener(mOnSkinChangeListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mSkinManager != null){
            mSkinManager.unRegister(this);
        }
        if(getSkinManager() != null){
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

    public void setSkinManager(@Nullable QMUISkinManager skinManager){
        if(mSkinManager != null){
            mSkinManager.unRegister(this);
        }
        mSkinManager = skinManager;
        if(skinManager != null){
            if(getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)){
                skinManager.register(this);
            }
        }
    }

}
