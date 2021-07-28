package io.github.sdwfqin.imageloader

import android.content.Context
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.caverock.androidsvg.SVG
import io.github.sdwfqin.imageloader.progress.ProgressManager
import io.github.sdwfqin.imageloader.svg.SvgDecoder
import io.github.sdwfqin.imageloader.svg.SvgDrawableTranscoder
import java.io.InputStream

/**
 * 描述：自定义模块
 *
 * @author zhangqin
 * @date 2018/6/20
 */
@GlideModule
class ImageLoaderGlideModule : AppGlideModule() {
    /**
     * 网络请求更换为Okhttp
     */
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(ProgressManager.okHttpClient)
        )
        // 支持svg
        registry.register(SVG::class.java, PictureDrawable::class.java, SvgDrawableTranscoder())
            .append(InputStream::class.java, SVG::class.java, SvgDecoder())
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}