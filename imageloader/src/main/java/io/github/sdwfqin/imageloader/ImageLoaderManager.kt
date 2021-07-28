package io.github.sdwfqin.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.Transformation
import io.github.sdwfqin.imageloader.progress.OnProgressListener
import io.github.sdwfqin.imageloader.progress.ProgressManager
import io.github.sdwfqin.imageloader.transformation.RadiusTransformation

/**
 * 描述：Glide图片加载
 *
 * @author zhangqin
 * @date 2018/6/20
 */
class ImageLoaderManager private constructor(val imageView: ImageView, val builder: Builder) {

    /**
     * 获取图片地址
     */
    private lateinit var url: String

    /**
     * 创建GlideRequest
     */
    fun loadBaseImage(): GlideRequests {
        if (builder.image is String && (builder.image as String).lowercase().startsWith(HTTP)) {
            url = builder.image as String
        }
        return GlideApp.with(context)
    }

    /**
     * 创建GlideRequest<Drawable>
    </Drawable> */
    fun loadDrawableImage(): GlideRequest<Drawable> {
        var glideRequest = loadBaseImage().load(builder.image)
        if (builder.placeholder != 0) {
            glideRequest = glideRequest.placeholder(builder.placeholder)
        }
        if (builder.errorImage != 0) {
            glideRequest = glideRequest.error(builder.errorImage)
        }
        if (builder.transformation != null) {
            glideRequest = glideRequest.transform(builder.transformation)
        } else if (builder.imageRadius != 0) {
            glideRequest =
                glideRequest.transform(RadiusTransformation(context, builder.imageRadius))
        }
        return glideRequest
    }

    /**
     * 加载到控件
     */
    fun loadImage(): ImageLoaderManager {
        val glideRequest = loadDrawableImage()
        glideRequest.into(imageView)
        return this
    }

    /**
     * 获取上下文对象
     */
    val context: Context
        get() = imageView.context

    /**
     * 网络图片加载进度
     */
    fun setOnProgressListener(onProgressListener: OnProgressListener): ImageLoaderManager {
        if (!TextUtils.isEmpty(url)) {
            ProgressManager.addListener(url, onProgressListener)
        }
        return this
    }

    class Builder {
        // 图片地址/链接等
        var image: Any? = null
            private set

        // 占位图
        var placeholder = 0
            private set

        // 错误图
        var errorImage = 0
            private set

        // 图片角度
        var imageRadius = 0
            private set
        var transformation: Transformation<Bitmap>? = null
            private set

        fun setImagePath(image: Any?): Builder {
            this.image = image
            return this
        }

        fun setPlaceholder(@DrawableRes placeholder: Int): Builder {
            this.placeholder = placeholder
            return this
        }

        fun setErrorImage(@DrawableRes errorImage: Int): Builder {
            this.errorImage = errorImage
            return this
        }

        fun setImageRadius(imageRadius: Int): Builder {
            this.imageRadius = imageRadius
            return this
        }

        fun setTransformation(transformation: Transformation<Bitmap>): Builder {
            this.transformation = transformation
            return this
        }

        fun build(imageView: ImageView): ImageLoaderManager {
            return ImageLoaderManager(imageView, this)
        }
    }

    companion object {
        private const val HTTP = "http"
    }
}