package io.github.sdwfqin.samplecommonlibrary.utils

import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import com.blankj.utilcode.util.ScreenUtils

/**
 * 截屏管理器
 * <p>
 *
 * @author 张钦
 * @date 2021/11/30
 */
class ScreenCaptureManager {

    private lateinit var mMediaProjection: MediaProjection
    private lateinit var mVirtualDisplay: VirtualDisplay
    private lateinit var mImageReader: ImageReader

    private var mScreenDensityDpi = 0
    private var mScreenWidth = 0
    private var mScreenHeight = 0

    companion object {
        @JvmStatic
        val instance: ScreenCaptureManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ScreenCaptureManager()
        }
    }

    fun init(mediaProjection: MediaProjection) {
        mMediaProjection = mediaProjection

        //获取当前屏幕的像素点
        mScreenDensityDpi = ScreenUtils.getScreenDensityDpi()
        mScreenWidth = ScreenUtils.getScreenWidth()
        mScreenHeight = ScreenUtils.getScreenHeight()

        mImageReader =
            ImageReader.newInstance(mScreenWidth, mScreenHeight, PixelFormat.RGBA_8888, 1)
        initVirtualDisplay()
    }

    fun getScreenImage(): Image? {
        return mImageReader.acquireLatestImage()
    }

    fun release() {
        mMediaProjection.stop()
        mVirtualDisplay.release()
    }

    private fun initVirtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay(
            "screen-mirror",
            mScreenWidth,
            mScreenHeight,
            mScreenDensityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mImageReader.surface,
            null,
            null
        )
    }
}