package io.github.sdwfqin.samplecommonlibrary.view

import android.app.Application
import android.content.Intent
import io.github.sdwfqin.widget.WindowFloatView
import androidx.appcompat.widget.AppCompatButton
import android.media.projection.MediaProjection
import android.hardware.display.VirtualDisplay
import android.view.WindowManager
import android.util.DisplayMetrics
import android.graphics.PixelFormat
import com.blankj.utilcode.util.ConvertUtils
import android.graphics.Bitmap
import com.blankj.utilcode.util.ToastUtils
import android.hardware.display.DisplayManager
import android.media.projection.MediaProjectionManager
import android.app.Activity
import android.content.Context
import android.media.Image
import android.media.ImageReader
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.LogUtils
import io.github.sdwfqin.samplecommonlibrary.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

/**
 * 悬浮窗View Demo
 *
 * @author 张钦
 * @date 2020/4/10
 */
class QuickWindowFloatView(context: Application, data: Intent?) : WindowFloatView(context) {

    private lateinit var mBtnScreenshot: AppCompatButton
    private lateinit var mBtnClose: AppCompatButton
    private lateinit var mIvImg: ImageView
    private var mScreenWidth = 0
    private var mScreenHeight = 0
    private var mScreenDensity = 0
    private lateinit var mImageReader: ImageReader
    private var mMediaProjection: MediaProjection
    private lateinit var mVirtualDisplay: VirtualDisplay
    private var job: Job? = null

    init {
        val mediaProjectionManager =
            context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mMediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, data!!)
    }

    override fun onCreate(rootView: View, layoutParams: WindowManager.LayoutParams) {
        super.onCreate(rootView, layoutParams)
        mBtnScreenshot = findViewById(R.id.btn_screenshot)
        mBtnClose = findViewById(R.id.btn_close)
        mIvImg = findViewById(R.id.iv_img)

        //获取当前屏幕的像素点
        val metrics = DisplayMetrics()
        getWindowManager().defaultDisplay.getMetrics(metrics)
        mScreenDensity = metrics.densityDpi
        mScreenWidth = metrics.widthPixels
        mScreenHeight = metrics.heightPixels
        mImageReader =
            ImageReader.newInstance(mScreenWidth, mScreenHeight, PixelFormat.RGBA_8888, 1)
        // 获取VirtureDisplay对象
        getVirtualDisplay()
        initListener()
        /**
         * 允许拖动悬浮窗
         */
        setCanMove(true)
    }

    override fun getLayoutView(): Int {
        return R.layout.layout_float_quick
    }

    override fun getHeight(): Int {
        return ConvertUtils.dp2px(200f)
    }

    override fun getWidth(): Int {
        return ConvertUtils.dp2px(80f)
    }

    private fun initListener() {
        mBtnClose.setOnClickListener { dismiss() }
        mBtnScreenshot.setOnClickListener {
            hide()
            Handler(Looper.getMainLooper()).postDelayed({ startCapture() }, 100)
        }
    }

    private fun startCapture() {
        val image = mImageReader.acquireLatestImage()
        if (image == null) {
            //开始截屏
            getVirtualDisplay()
            LogUtils.e("getVirtualDisplay")
            show()
        } else {
            job = GlobalScope
                .launch(Dispatchers.Main) {
                    saveImage(image)
                        .onCompletion {
                            show()
                        }
                        .collect {
                            it?.let {
                                ToastUtils.showShort("截图已经成功保存到相册")
                                mIvImg.setImageBitmap(it)
                            }
                        }
                }
        }
    }

    private fun saveImage(image: Image): Flow<Bitmap?> {
        return flowOf(image)
            .map {
                val width = it.width
                val height = it.height
                val planes = it.planes
                val buffer = planes[0].buffer
                //每个像素的间距
                val pixelStride = planes[0].pixelStride
                //总的间距
                val rowStride = planes[0].rowStride
                val rowPadding = rowStride - pixelStride * width
                var bitmap = Bitmap.createBitmap(
                    width + rowPadding / pixelStride,
                    height,
                    Bitmap.Config.ARGB_8888
                )
                bitmap.copyPixelsFromBuffer(buffer)
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height)
                it.close()
                // 判断文件有没有
                var fileImage: File? = null
                // 保存图片
                if (bitmap != null) {
                    try {
                        fileImage = File(
                            getContext().externalMediaDirs[0],
                            System.currentTimeMillis().toString() + ".jpg"
                        )
                        if (!fileImage.exists()) {
                            fileImage.createNewFile()
                        }
                        val out = FileOutputStream(fileImage)
                        ToastUtils.showShort("正在保存中...")
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                        out.flush()
                        out.close()
                        // 发送广播给相册--更新相册图片
                        val media = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                        val contentUri = Uri.fromFile(fileImage)
                        media.data = contentUri
                        getContext().sendBroadcast(media)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                return@map bitmap
            }
            .flowOn(Dispatchers.IO)
    }

    private fun getVirtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay(
            "screen-mirror",
            mScreenWidth,
            mScreenHeight,
            mScreenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mImageReader.surface,
            null,
            null
        )
    }

    private fun tearDownMediaProjection() {
        mMediaProjection.stop()
    }

    private fun stopVirtual() {
        mVirtualDisplay.release()
    }

    override fun onStop() {
        job?.cancel()
        stopVirtual()
        tearDownMediaProjection()
    }
}