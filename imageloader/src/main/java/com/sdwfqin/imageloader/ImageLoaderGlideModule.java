package com.sdwfqin.imageloader;

import android.content.Context;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.sdwfqin.imageloader.progress.ProgressManager;

import java.io.InputStream;

/**
 * 描述：自定义模块
 *
 * @author zhangqin
 * @date 2018/6/20
 */
@GlideModule
public class ImageLoaderGlideModule extends AppGlideModule {

    /**
     * 网络请求更换为Okhttp
     */
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient()));
    }
}