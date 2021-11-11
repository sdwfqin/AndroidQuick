package io.github.sdwfqin.imageloader.transformation

import android.content.Context
import android.graphics.*
import android.os.Build
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import io.github.sdwfqin.imageloader.util.DisplayUtil
import java.security.MessageDigest

/**
 * @author by sunfusheng on 2017/6/6.
 */
class RadiusTransformation internal constructor(context: Context, var radius: Int) :
    BitmapTransformation() {

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return radiusCrop(pool, toTransform)!!
    }

    private fun radiusCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) {
            return null
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val size = source.width.coerceAtMost(source.height)
            val bitmap = pool[size, size, Bitmap.Config.ARGB_8888]
            val canvas = Canvas(bitmap)
            val paint = Paint()
            paint.shader =
                BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.isAntiAlias = true
            canvas.drawRoundRect(
                0f,
                0f,
                size.toFloat(),
                size.toFloat(),
                radius.toFloat(),
                radius.toFloat(),
                paint
            )
            return bitmap
        }
        return source
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}

    init {
        this.radius = DisplayUtil.dp2px(radius.toFloat())
    }
}