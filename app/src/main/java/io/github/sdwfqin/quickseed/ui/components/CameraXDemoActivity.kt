package io.github.sdwfqin.quickseed.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.ImageFormat
import android.graphics.Point
import android.net.Uri
import android.provider.MediaStore
import android.util.Size
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCapture.OutputFileResults
import androidx.camera.extensions.ExtensionMode
import androidx.camera.extensions.ExtensionsManager
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.common.util.concurrent.ListenableFuture
import com.google.zxing.BinaryBitmap
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import io.github.sdwfqin.imageloader.ImageLoaderManager
import io.github.sdwfqin.quicklib.base.BaseActivity
import io.github.sdwfqin.quickseed.R
import io.github.sdwfqin.quickseed.constants.ArouterConstants
import io.github.sdwfqin.quickseed.databinding.ActivityCameraxDemoBinding
import io.github.sdwfqin.samplecommonlibrary.utils.MediaStoreUtils
import io.github.sdwfqin.samplecommonlibrary.utils.qrbarscan.DecodeCodeTools
import io.github.sdwfqin.samplecommonlibrary.view.CameraXPreviewViewTouchListener
import io.github.sdwfqin.samplecommonlibrary.view.CameraXPreviewViewTouchListener.CustomTouchListener
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

/**
 * CameraX Demo
 *
 *
 * https://proandroiddev.com/android-camerax-tap-to-focus-pinch-to-zoom-zoom-slider-eb88f3aa6fc6
 *
 * @author 张钦
 * @date 2019-05-30
 */
@Route(path = ArouterConstants.COMPONENTS_CAMERAX)
class CameraXDemoActivity : BaseActivity<ActivityCameraxDemoBinding>(), CameraXConfig.Provider {
    private val executor by lazy { ContextCompat.getMainExecutor(this) }
    private lateinit var mCameraProvider: ProcessCameraProvider
    private lateinit var mImageCapture: ImageCapture
    private lateinit var mImageAnalysis: ImageAnalysis
    private lateinit var mCameraInfo: CameraInfo
    private lateinit var mCameraControl: CameraControl
    private lateinit var mPreview: Preview
    private lateinit var mCameraSelector: CameraSelector

    /**
     * 屏幕比例
     */
    private var mAspectRatioInt = AspectRatio.RATIO_16_9

    /**
     * 摄像头（前/后）
     */
    private var mCameraSelectorInt = CameraSelector.LENS_FACING_BACK

    /**
     * 左下角图片uri
     */
    private var mImagePathUri: Uri? = null

    /**
     * 是否分析下一张图片
     */
    private val mIsNextAnalysis = true

    /**
     * 二维码内容
     */
    private var mQrText = ""

    /**
     * 扩展
     */
    private lateinit var mExtensionsManager: ExtensionsManager
    private var isEnabledHdr = true

    override fun getViewBinding(): ActivityCameraxDemoBinding {
        return ActivityCameraxDemoBinding.inflate(layoutInflater)
    }

    override fun initEventAndData() {
        mStatusBar.visibility = View.GONE
        mNavBar.visibility = View.GONE
        val perms = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        initCheckPermissions(perms, object : OnPermissionCallback {
            override fun onSuccess() {
                initCamera()
            }

            override fun onError() {
                finish()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    override fun initClickListener() {
        /**
         * 保存图片
         */
        mBinding.captureButton.setOnClickListener { saveImage() }
        /**
         * 修改闪光灯模式
         */
        mBinding.btnLight.setOnClickListener {
            when (mImageCapture.flashMode) {
                ImageCapture.FLASH_MODE_AUTO -> {
                    mImageCapture.flashMode = ImageCapture.FLASH_MODE_ON
                    mBinding.btnLight.text = "闪光灯：开"
                }
                ImageCapture.FLASH_MODE_ON -> {
                    mImageCapture.flashMode = ImageCapture.FLASH_MODE_OFF
                    mBinding.btnLight.text = "闪光灯：关"
                }
                ImageCapture.FLASH_MODE_OFF -> {
                    mImageCapture.flashMode = ImageCapture.FLASH_MODE_AUTO
                    mBinding.btnLight.text = "闪光灯：自动"
                }
            }
        }
        /**
         * 修改比例
         */
        mBinding.btnAspect.setOnClickListener {
            val layoutParams = mBinding.viewFinder.layoutParams as ConstraintLayout.LayoutParams
            when (mAspectRatioInt) {
                AspectRatio.RATIO_16_9 -> {
                    layoutParams.dimensionRatio = "3:4"
                    mBinding.viewFinder.layoutParams = layoutParams
                    mBinding.btnAspect.text = "16:9"
                    mAspectRatioInt = AspectRatio.RATIO_4_3
                }
                AspectRatio.RATIO_4_3 -> {
                    layoutParams.dimensionRatio = "9:16"
                    mBinding.viewFinder.layoutParams = layoutParams
                    mBinding.btnAspect.text = "4:3"
                    mAspectRatioInt = AspectRatio.RATIO_16_9
                }
            }
            changeCameraConfig(CHANGE_TYPE_RATIO)
        }
        /**
         * 切换摄像头
         */
        mBinding.btnCameraSelector.setOnClickListener {
            when (mCameraSelectorInt) {
                CameraSelector.LENS_FACING_BACK -> {
                    mCameraSelectorInt = CameraSelector.LENS_FACING_FRONT
                    mBinding.btnCameraSelector.text = "后"
                }
                CameraSelector.LENS_FACING_FRONT -> {
                    mCameraSelectorInt = CameraSelector.LENS_FACING_BACK
                    mBinding.btnCameraSelector.text = "前"
                }
            }
            changeCameraConfig(CHANGE_TYPE_SELECTOR)
        }
        /**
         * 在相册打开图片
         */
        mBinding.ivPictures.setOnClickListener {
            mImagePathUri?.let {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(it, "image/*")
                startActivity(intent)
            }
        }

        mBinding.btnCameraHdr.setOnClickListener {
            isEnabledHdr = !isEnabledHdr
            changeCameraConfig(CHANGE_TYPE_HDR)
        }
    }

    private fun initCamera() {
        lifecycleScope.launch {
            mCameraProvider = ProcessCameraProvider.getInstance(this@CameraXDemoActivity).get()
            mExtensionsManager = ExtensionsManager.getInstance(this@CameraXDemoActivity).get()
            initUseCases()
            try {
                mCameraProvider.unbindAll()
                var cameraSelector = mCameraSelector
                if (isEnabledHdr) {
                    cameraSelector = mExtensionsManager.getExtensionEnabledCameraSelector(
                        mCameraProvider,
                        mCameraSelector,
                        ExtensionMode.HDR
                    )
                }
                val camera = mCameraProvider.bindToLifecycle(
                    this@CameraXDemoActivity,
                    cameraSelector,
                    mPreview,
                    mImageCapture,
                    mImageAnalysis
                )
                if (isEnabledHdr) {
                    changeCameraConfig(CHANGE_TYPE_HDR)
                }
                mCameraInfo = camera.cameraInfo
                mCameraControl = camera.cameraControl
                initCameraListener()
            } catch (e: ExecutionException) {
                LogUtils.e(e)
                // No errors need to be handled for this Future.
                // This should never be reached.
            } catch (e: InterruptedException) {
                LogUtils.e(e)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initCameraListener() {
        val zoomState = mCameraInfo.zoomState
        val cameraXPreviewViewTouchListener = CameraXPreviewViewTouchListener(mContext)

        cameraXPreviewViewTouchListener.setCustomTouchListener(object : CustomTouchListener {
            override fun zoom(delta: Float) {
                zoomState.value?.let {
                    val currentZoomRatio = it.zoomRatio
                    mCameraControl.setZoomRatio(currentZoomRatio * delta)
                }
            }

            override fun click(x: Float, y: Float) {
                val factory = mBinding.viewFinder.meteringPointFactory
                val point = factory.createPoint(x, y)
                val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                    // auto calling cancelFocusAndMetering in 3 seconds
                    .setAutoCancelDuration(3, TimeUnit.SECONDS)
                    .build()
                mBinding.focusView.startFocus(Point(x.toInt(), y.toInt()))
                val future: ListenableFuture<*> = mCameraControl.startFocusAndMetering(action)
                future.addListener({
                    try {
                        val result = future.get() as FocusMeteringResult
                        if (result.isFocusSuccessful) {
                            mBinding.focusView.onFocusSuccess()
                        } else {
                            mBinding.focusView.onFocusFailed()
                        }
                    } catch (e: Exception) {
                        LogUtils.e(e)
                    }
                }, executor)
            }

            override fun doubleClick(x: Float, y: Float) {
                // 双击放大缩小
                zoomState.value?.let {
                    val currentZoomRatio = it.zoomRatio
                    if (currentZoomRatio > it.minZoomRatio) {
                        mCameraControl.setLinearZoom(0f)
                    } else {
                        mCameraControl.setLinearZoom(0.5f)
                    }
                }
            }

            override fun longClick(x: Float, y: Float) {}
        })
        mBinding.viewFinder.setOnTouchListener(cameraXPreviewViewTouchListener)
    }

    /**
     * 初始化配置信息
     */
    private fun initUseCases() {
        initImageAnalysis()
        initImageCapture()
        // 视频：VideoCapture
        initPreview()
        initCameraSelector()
        initHdr()
    }

    /**
     * 图像分析
     */
    private fun initImageAnalysis() {
        mImageAnalysis = ImageAnalysis.Builder() // 分辨率
            .setTargetResolution(
                if (mAspectRatioInt == AspectRatio.RATIO_4_3) Size(
                    720,
                    960
                ) else Size(720, 1280)
            ) // 非阻塞模式
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        mImageAnalysis.setAnalyzer(executor, { image: ImageProxy ->

            // Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);

            /**
             * 扫描二维码
             *
             * https://stackoverflow.com/questions/58113159/how-to-use-zxing-with-android-camerax-to-decode-barcode-and-qr-codes
             */
            if ((image.format == ImageFormat.YUV_420_888 || image.format == ImageFormat.YUV_422_888 || image.format == ImageFormat.YUV_444_888)
                && image.planes.size == 3
            ) {
                val buffer = image.planes[0].buffer
                val data = ByteArray(buffer.remaining())
                buffer[data]
                val source = PlanarYUVLuminanceSource(
                    data,
                    image.width,
                    image.height,
                    0,
                    0,
                    image.width,
                    image.height,
                    false
                )
                val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
                try {
                    val result = DecodeCodeTools.getDefaultMultiFormatReader().decode(binaryBitmap)
                    if (result != null && (StringUtils.isEmpty(mQrText) || !StringUtils.equals(
                            mQrText,
                            result.text
                        ))
                    ) {
                        mQrText = result.text
                        LogUtils.e(result.toString())
                        ToastUtils.showShort(result.text)
                        // TODO 只扫描一张
                        // mIsNextAnalysis = false;
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (mIsNextAnalysis) {
                image.close()
            }
        })
    }

    /**
     * 构建图像捕获用例
     */
    private fun initImageCapture() {

        val imageCaptureBuilder = ImageCapture.Builder()
            .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
            .setTargetAspectRatio(mAspectRatioInt)
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)

        // 构建图像捕获用例
        mImageCapture = imageCaptureBuilder
            .build();


        // 旋转监听
        val orientationEventListener: OrientationEventListener =
            object : OrientationEventListener(mContext) {
                override fun onOrientationChanged(orientation: Int) {
                    // Monitors orientation values to determine the target rotation value
                    mImageCapture.targetRotation = when (orientation) {
                        in 45..134 -> {
                            Surface.ROTATION_270
                        }
                        in 135..224 -> {
                            Surface.ROTATION_180
                        }
                        in 225..314 -> {
                            Surface.ROTATION_90
                        }
                        else -> {
                            Surface.ROTATION_0
                        }
                    }
                }
            }
        orientationEventListener.enable()
    }

    /**
     * 构建图像预览
     */
    private fun initPreview() {
        val previewBuilder = Preview.Builder()
            .setTargetAspectRatio(mAspectRatioInt)
        mPreview = previewBuilder
            .build()

        mPreview.setSurfaceProvider(mBinding.viewFinder.surfaceProvider)
    }

    /**
     * 选择摄像头
     */
    private fun initCameraSelector() {
        mCameraSelector = CameraSelector.Builder()
            .requireLensFacing(mCameraSelectorInt)
            .build()
    }

    /**
     * 开启Hdr
     */
    private fun initHdr() {
        if (isEnabledHdr) {
            mBinding.btnCameraHdr.text = "HDR: 已开启"
            if (mExtensionsManager.isExtensionAvailable(
                    mCameraProvider,
                    mCameraSelector,
                    ExtensionMode.HDR
                )
            ) {
                mBinding.btnCameraHdr.isEnabled = true
            } else {
                mBinding.btnCameraHdr.text = "HDR: 不支持的扩展"
                mBinding.btnCameraHdr.isEnabled = false
                isEnabledHdr = false
            }
        } else {
            mBinding.btnCameraHdr.text = "HDR: 已关闭"
            isEnabledHdr = false
        }
    }

    /**
     * 更改相机参数
     *
     * @param changeType
     */
    private fun changeCameraConfig(changeType: Int) {
        when (changeType) {
            CHANGE_TYPE_RATIO -> {
                if (mCameraProvider.isBound(mImageCapture)) {
                    mCameraProvider.unbind(mImageCapture)
                }
                initImageCapture()
                initHdr()
                mCameraProvider.bindToLifecycle(this, mCameraSelector, mImageCapture)
            }
            CHANGE_TYPE_SELECTOR -> {
                mCameraProvider.unbindAll()
                initUseCases()
                mCameraProvider.bindToLifecycle(
                    this,
                    mCameraSelector,
                    mPreview,
                    mImageCapture,
                    mImageAnalysis
                )
            }
            CHANGE_TYPE_HDR -> {
                if (mCameraProvider.isBound(mPreview)) {
                    mCameraProvider.unbind(mPreview)
                }
                if (mCameraProvider.isBound(mImageCapture)) {
                    mCameraProvider.unbind(mImageCapture)
                }
                initPreview()
                initImageCapture()
                initHdr()
                if (isEnabledHdr) {
                    val hdrCameraSelector = mExtensionsManager.getExtensionEnabledCameraSelector(
                        mCameraProvider,
                        mCameraSelector,
                        ExtensionMode.HDR
                    )
                    mCameraProvider.bindToLifecycle(
                        this,
                        hdrCameraSelector,
                        mPreview,
                        mImageCapture
                    )
                } else {
                    mCameraProvider.bindToLifecycle(this, mCameraSelector, mPreview, mImageCapture)
                }
            }
        }
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }

    private fun saveImage() {
        val outputFileOptions = OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            MediaStoreUtils.getImageContentValues()
        ).build()
        mImageCapture.takePicture(outputFileOptions, executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: OutputFileResults) {
                    mImagePathUri = outputFileResults.savedUri
                    ImageLoaderManager.Builder()
                        .setImagePath(mImagePathUri)
                        .setPlaceholder(R.mipmap.image_loading)
                        .setErrorImage(R.mipmap.image_load_err)
                        .build(mBinding.ivPictures)
                        .loadImage()
                }

                override fun onError(exception: ImageCaptureException) {
                    val msg = "图片保存失败: " + exception.message
                    showMsg(msg)
                    LogUtils.e(msg)
                }
            }
        )
    }

    companion object {
        /**
         * 切换比例
         */
        private const val CHANGE_TYPE_RATIO = 0

        /**
         * 切换摄像头
         */
        private const val CHANGE_TYPE_SELECTOR = 1

        /**
         * HDR
         */
        private const val CHANGE_TYPE_HDR = 2
    }
}