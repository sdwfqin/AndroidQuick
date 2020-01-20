package com.sdwfqin.quickseed.ui.components;

import android.content.Intent;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.lifecycle.LifecycleOwner;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quickseed.R;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;

/**
 * CameraxDemo
 * <p>
 *
 * @author 张钦
 * @date 2019-05-30
 */
public class CameraxDemoActivity extends BaseActivity implements LifecycleOwner {

    @BindView(R.id.view_finder)
    TextureView mViewFinder;
    @BindView(R.id.capture_button)
    ImageButton mCaptureButton;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected int getLayout() {
        return R.layout.activity_camerax_demo;
    }

    @Override
    protected void initEventAndData() {

        mTopBar.setVisibility(View.GONE);

        mViewFinder.post(this::startCamera);

        mViewFinder.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            updateTransform();
        });
    }

    private void startCamera() {

        PreviewConfig previewConfig = new PreviewConfig
                .Builder()
                // .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetResolution(new Size(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight()))
                .build();

        Preview preview = new Preview(previewConfig);

        preview.setOnPreviewOutputUpdateListener(output -> {
            // To update the SurfaceTexture, we have to remove it and re-add it
            ViewGroup parent = (ViewGroup) mViewFinder.getParent();
            parent.removeView(mViewFinder);
            parent.addView(mViewFinder, 0);

            mViewFinder.setSurfaceTexture(output.getSurfaceTexture());
            updateTransform();
        });

        // 捕获图像配置
        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder()
                // We don't set a resolution for image capture; instead, we
                // select a capture mode which will infer the appropriate
                // resolution based on aspect ration and requested mode
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .build();

        // 构建图像捕获用例
        ImageCapture imageCapture = new ImageCapture(imageCaptureConfig);
        mCaptureButton.setOnClickListener(v -> {
            File file = new File(getExternalMediaDirs()[0], System.currentTimeMillis() + ".jpg");
            imageCapture.takePicture(file, executor, new ImageCapture.OnImageSavedListener() {
                @Override
                public void onImageSaved(@NonNull File file) {
                    String msg = "图片保存成功: " + file.getAbsolutePath();
                    showMsg(msg);
                    LogUtils.d(msg);
                    Uri contentUri = Uri.fromFile(new File(file.getAbsolutePath()));
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
                    sendBroadcast(mediaScanIntent);
                }

                @Override
                public void onError(@NonNull ImageCapture.ImageCaptureError imageCaptureError, @NonNull String message, @Nullable Throwable cause) {
                    String msg = "图片保存失败: " + message;
                    showMsg(msg);
                    LogUtils.e(msg);
                }
            });
        });

        // appcompat 1.1.+
        CameraX.bindToLifecycle(this, preview, imageCapture);
    }

    private void updateTransform() {
        Matrix matrix = new Matrix();

        // Compute the center of the view finder
        float centerX = mViewFinder.getWidth() / 2f;
        float centerY = mViewFinder.getHeight() / 2f;

        // Correct preview output to account for display rotation
        int rotationDegrees = 0;
        switch (mViewFinder.getDisplay().getRotation()) {
            case Surface.ROTATION_0:
                rotationDegrees = 0;
                break;
            case Surface.ROTATION_90:
                rotationDegrees = 90;
                break;
            case Surface.ROTATION_180:
                rotationDegrees = 180;
                break;
            case Surface.ROTATION_270:
                rotationDegrees = 270;
                break;
        }

        matrix.postRotate(-rotationDegrees, centerX, centerY);

        // Finally, apply transformations to our TextureView
        mViewFinder.setTransform(matrix);
    }

    @Override
    protected void onDestroy() {
        CameraX.unbindAll();
        super.onDestroy();
    }
}
