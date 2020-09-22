package com.sdwfqin.quickseed.ui.components;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Size;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.FocusMeteringResult;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.core.ZoomState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.sdwfqin.imageloader.ImageLoader;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.constants.ArouterConstants;
import com.sdwfqin.quickseed.databinding.ActivityCameraxDemoBinding;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.github.sdwfqin.samplecommonlibrary.utils.MediaStoreUtils;
import io.github.sdwfqin.samplecommonlibrary.utils.qrbarscan.DecodeCodeTools;
import io.github.sdwfqin.samplecommonlibrary.view.CameraXCustomPreviewView;

/**
 * CameraX Demo
 * <p>
 * https://proandroiddev.com/android-camerax-tap-to-focus-pinch-to-zoom-zoom-slider-eb88f3aa6fc6
 *
 * @author 张钦
 * @date 2019-05-30
 */
@Route(path = ArouterConstants.COMPONENTS_CAMERAX)
public class CameraXDemoActivity extends BaseActivity<ActivityCameraxDemoBinding> implements CameraXConfig.Provider {

    private static final int CHANGE_TYPE_RATIO = 0;
    private static final int CHANGE_TYPE_SELECTOR = 1;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture mImageCapture;
    private ImageAnalysis mImageAnalysis;
    private Executor executor;
    private CameraInfo mCameraInfo;
    private CameraControl mCameraControl;
    private Preview mPreview;
    private CameraSelector mCameraSelector;
    private int mAspectRatioInt = AspectRatio.RATIO_16_9;
    private int mCameraSelectorInt = CameraSelector.LENS_FACING_BACK;
    /**
     * 左下角图片uri
     */
    private Uri mImagePathUri;

    /**
     * 是否分析下一张图片
     */
    private boolean mIsNextAnalysis = true;
    private String mQrText = "";
    private ProcessCameraProvider mCameraProvider;

    @Override
    protected ActivityCameraxDemoBinding getViewBinding() {
        return ActivityCameraxDemoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {

        mTopBar.setVisibility(View.GONE);

        executor = ContextCompat.getMainExecutor(this);

        initCamera();
    }

    @SuppressLint({"SetTextI18n"})
    @Override
    protected void initClickListener() {
        /**
         * 保存图片
         */
        mBinding.captureButton.setOnClickListener(v -> saveImage());

        /**
         * 修改闪光灯模式
         */
        mBinding.btnLight.setOnClickListener(v -> {
            switch (mImageCapture.getFlashMode()) {
                case ImageCapture.FLASH_MODE_AUTO:
                    mImageCapture.setFlashMode(ImageCapture.FLASH_MODE_ON);
                    mBinding.btnLight.setText("闪光灯：开");
                    break;
                case ImageCapture.FLASH_MODE_ON:
                    mImageCapture.setFlashMode(ImageCapture.FLASH_MODE_OFF);
                    mBinding.btnLight.setText("闪光灯：关");
                    break;
                case ImageCapture.FLASH_MODE_OFF:
                    mImageCapture.setFlashMode(ImageCapture.FLASH_MODE_AUTO);
                    mBinding.btnLight.setText("闪光灯：自动");
                    break;
            }
        });

        /**
         * 修改比例
         */
        mBinding.btnAspect.setOnClickListener(v -> {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mBinding.viewFinder.getLayoutParams();
            switch (mAspectRatioInt) {
                case AspectRatio.RATIO_16_9:

                    layoutParams.dimensionRatio = "3:4";
                    mBinding.viewFinder.setLayoutParams(layoutParams);
                    mBinding.btnAspect.setText("16:9");

                    mAspectRatioInt = AspectRatio.RATIO_4_3;
                    break;
                case AspectRatio.RATIO_4_3:

                    layoutParams.dimensionRatio = "9:16";
                    mBinding.viewFinder.setLayoutParams(layoutParams);
                    mBinding.btnAspect.setText("4:3");

                    mAspectRatioInt = AspectRatio.RATIO_16_9;
                    break;
            }

            changeCameraConfig(CHANGE_TYPE_RATIO);
        });

        /**
         * 切换摄像头
         */
        mBinding.btnCameraSelector.setOnClickListener(v -> {
            switch (mCameraSelectorInt) {
                case CameraSelector.LENS_FACING_BACK:
                    mCameraSelectorInt = CameraSelector.LENS_FACING_FRONT;
                    mBinding.btnCameraSelector.setText("后");
                    break;
                case CameraSelector.LENS_FACING_FRONT:
                    mCameraSelectorInt = CameraSelector.LENS_FACING_BACK;
                    mBinding.btnCameraSelector.setText("前");
                    break;
            }
            changeCameraConfig(CHANGE_TYPE_SELECTOR);
        });

        /**
         * 在相册打开图片
         */
        mBinding.ivPictures.setOnClickListener(v -> {
            if (mImagePathUri != null) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(mImagePathUri, "image/*");
                startActivity(intent);
            }
        });
    }

    private void initCamera() {

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        initUseCases();

        cameraProviderFuture.addListener(() -> {
            try {
                mCameraProvider = cameraProviderFuture.get();
                mCameraProvider.unbindAll();
                mPreview.setSurfaceProvider(mBinding.viewFinder.getSurfaceProvider());
                Camera camera = mCameraProvider.bindToLifecycle(this, mCameraSelector, mPreview, mImageCapture, mImageAnalysis);
                mCameraInfo = camera.getCameraInfo();
                mCameraControl = camera.getCameraControl();

                initCameraListener();
            } catch (ExecutionException | InterruptedException e) {
                LogUtils.e(e);
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, executor);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCameraListener() {
        LiveData<ZoomState> zoomState = mCameraInfo.getZoomState();

        mBinding.viewFinder.setCustomTouchListener(new CameraXCustomPreviewView.CustomTouchListener() {

            @Override
            public void zoom(float delta) {
                float currentZoomRatio = zoomState.getValue().getZoomRatio();
                mCameraControl.setZoomRatio(currentZoomRatio * delta);
            }

            @Override
            public void click(float x, float y) {
                MeteringPointFactory factory = mBinding.viewFinder.getMeteringPointFactory();
                MeteringPoint point = factory.createPoint(x, y);
                FocusMeteringAction action = new FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                        // auto calling cancelFocusAndMetering in 3 seconds
                        .setAutoCancelDuration(3, TimeUnit.SECONDS)
                        .build();

                mBinding.focusView.startFocus(new Point((int) x, (int) y));
                ListenableFuture future = mCameraControl.startFocusAndMetering(action);
                future.addListener(() -> {
                    try {
                        FocusMeteringResult result = (FocusMeteringResult) future.get();
                        if (result.isFocusSuccessful()) {
                            mBinding.focusView.onFocusSuccess();
                        } else {
                            mBinding.focusView.onFocusFailed();
                        }
                    } catch (Exception e) {
                        LogUtils.e(e);
                    }
                }, executor);
            }

            @Override
            public void doubleClick(float x, float y) {
                // 双击放大缩小
                float currentZoomRatio = zoomState.getValue().getZoomRatio();
                if (currentZoomRatio > zoomState.getValue().getMinZoomRatio()) {
                    mCameraControl.setLinearZoom(0f);
                } else {
                    mCameraControl.setLinearZoom(0.5f);
                }
            }

            @Override
            public void longClick(float x, float y) {

            }
        });
    }

    /**
     * 初始化配置信息
     */
    private void initUseCases() {
        initImageAnalysis();
        initImageCapture();
        // 视频：VideoCapture
        initPreview();
        initCameraSelector();
    }

    /**
     * 图像分析
     */
    private void initImageAnalysis() {

        mImageAnalysis = new ImageAnalysis.Builder()
                // 分辨率
                .setTargetResolution(mAspectRatioInt == AspectRatio.RATIO_4_3 ? new Size(720, 960) : new Size(720, 1280))
                // 非阻塞模式
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        mImageAnalysis.setAnalyzer(executor, image -> {

            /**
             * 扫描二维码
             *
             * https://stackoverflow.com/questions/58113159/how-to-use-zxing-with-android-camerax-to-decode-barcode-and-qr-codes
             */
            if ((image.getFormat() == ImageFormat.YUV_420_888
                    || image.getFormat() == ImageFormat.YUV_422_888
                    || image.getFormat() == ImageFormat.YUV_444_888)
                    && image.getPlanes().length == 3) {

                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);

                PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, image.getWidth(), image.getHeight(), 0, 0, image.getWidth(), image.getHeight(), false);
                BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
                try {
                    Result result = DecodeCodeTools.getDefaultMultiFormatReader().decode(binaryBitmap);
                    if (result != null && (StringUtils.isEmpty(mQrText) || !StringUtils.equals(mQrText, result.getText()))) {
                        mQrText = result.getText();
                        LogUtils.e(result.toString());
                        ToastUtils.showShort(result.getText());
                        // TODO 只扫描一张
                        // mIsNextAnalysis = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (mIsNextAnalysis) {
                image.close();
            }
        });
    }

    /**
     * 构建图像捕获用例
     */
    private void initImageCapture() {

        // 构建图像捕获用例
        mImageCapture = new ImageCapture.Builder()
                .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
                .setTargetAspectRatio(mAspectRatioInt)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build();

        // 旋转监听
        OrientationEventListener orientationEventListener = new OrientationEventListener(mContext) {
            @Override
            public void onOrientationChanged(int orientation) {
                int rotation;

                // Monitors orientation values to determine the target rotation value
                if (orientation >= 45 && orientation < 135) {
                    rotation = Surface.ROTATION_270;
                } else if (orientation >= 135 && orientation < 225) {
                    rotation = Surface.ROTATION_180;
                } else if (orientation >= 225 && orientation < 315) {
                    rotation = Surface.ROTATION_90;
                } else {
                    rotation = Surface.ROTATION_0;
                }

                mImageCapture.setTargetRotation(rotation);
            }
        };

        orientationEventListener.enable();
    }

    /**
     * 构建图像预览
     */
    private void initPreview() {
        mPreview = new Preview.Builder()
                .setTargetAspectRatio(mAspectRatioInt)
                .build();
    }

    /**
     * 选择摄像头
     */
    private void initCameraSelector() {
        mCameraSelector = new CameraSelector.Builder()
                .requireLensFacing(mCameraSelectorInt)
                .build();
    }

    private void changeCameraConfig(int changeType) {
        if (changeType == CHANGE_TYPE_RATIO) {
            if (mCameraProvider.isBound(mImageCapture)) {
                mCameraProvider.unbind(mImageCapture);
            }
            initImageCapture();
            mCameraProvider.bindToLifecycle(this, mCameraSelector, mImageCapture);
        } else if (changeType == CHANGE_TYPE_SELECTOR) {
            initCamera();
        }
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }

    public void saveImage() {

        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStoreUtils.getImageContentValues()).build();
        mImageCapture.takePicture(outputFileOptions, executor,
                new ImageCapture.OnImageSavedCallback() {

                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        mImagePathUri = outputFileResults.getSavedUri();
                        new ImageLoader.Builder()
                                .setImagePath(mImagePathUri)
                                .setPlaceholder(R.mipmap.image_loading)
                                .setErrorImage(R.mipmap.image_load_err)
                                .build(mBinding.ivPictures)
                                .loadImage();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        String msg = "图片保存失败: " + exception.getMessage();
                        showMsg(msg);
                        LogUtils.e(exception);
                    }
                }
        );
    }
}
