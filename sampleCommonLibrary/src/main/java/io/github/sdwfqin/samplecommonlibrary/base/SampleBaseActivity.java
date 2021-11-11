package io.github.sdwfqin.samplecommonlibrary.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import io.github.sdwfqin.quicklib.base.BaseActivity;

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
