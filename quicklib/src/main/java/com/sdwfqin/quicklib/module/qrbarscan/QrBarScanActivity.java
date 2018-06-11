package com.sdwfqin.quicklib.module.qrbarscan;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.google.zxing.Result;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quicklib.module.qrbarscan.decoding.InactivityTimer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述：二维码/条形码识别Activity
 *
 * @author 张钦
 * @date 2018/1/23
 */
public class QrBarScanActivity extends BaseActivity implements View.OnClickListener {

    public static final int GET_IMAGE_FROM_PHONE = 5002;

    ImageView mCaptureScanLine;
    SurfaceView mCapturePreview;
    RelativeLayout mCaptureCropLayout;
    RelativeLayout mCaptureContainter;

    private ImageView mTop_mask;
    private ImageView mTop_openpicture;
    private ImageView mTop_back;

    private InactivityTimer inactivityTimer;
    private CaptureActivityHandler handler;//扫描处理
    private int mCropWidth = 0;//扫描边界的宽度
    private int mCropHeight = 0;//扫描边界的高度
    private boolean hasSurface;//是否有预览
    private boolean mFlashing = true;//闪光灯开启状态

    @Override
    protected int getLayout() {
        return R.layout.quick_activity_qr_scan;
    }

    @Override
    protected void initEventAndData() {

        QMUIStatusBarHelper.translucent(mContext);

        mCaptureScanLine = findViewById(R.id.capture_scan_line);
        mCapturePreview = findViewById(R.id.capture_preview);
        mCaptureCropLayout = findViewById(R.id.capture_crop_layout);
        mCaptureContainter = findViewById(R.id.capture_containter);
        mCaptureContainter = findViewById(R.id.capture_containter);

        mTop_mask = findViewById(R.id.top_mask);
        mTop_openpicture = findViewById(R.id.top_openpicture);
        mTop_back = findViewById(R.id.top_back);

        initScanerAnimation();//扫描动画初始化
        CameraManager.init(mContext);//初始化 CameraManager
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        mTop_mask.setOnClickListener(this);
        mTop_openpicture.setOnClickListener(this);
        mTop_back.setOnClickListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = mCapturePreview.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);//Camera初始化
        } else {
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    if (!hasSurface) {
                        hasSurface = true;
                        initCamera(holder);
                    }
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    hasSurface = false;

                }
            });
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initScanerAnimation() {
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1200);
        mCaptureScanLine.startAnimation(animation);
    }

    public int getCropWidth() {
        return mCropWidth;
    }

    public void setCropWidth(int cropWidth) {
        mCropWidth = cropWidth;
        CameraManager.FRAME_WIDTH = mCropWidth;

    }

    public int getCropHeight() {
        return mCropHeight;
    }

    public void setCropHeight(int cropHeight) {
        this.mCropHeight = cropHeight;
        CameraManager.FRAME_HEIGHT = mCropHeight;
    }

    private void light() {
        if (mFlashing) {
            // 开闪光灯
            CameraManager.get().openLight();
        } else {
            // 关闪光灯
            CameraManager.get().offLight();
        }

        mFlashing = !mFlashing;

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
            Point point = CameraManager.get().getCameraResolution();
            AtomicInteger width = new AtomicInteger(point.y);
            AtomicInteger height = new AtomicInteger(point.x);
            int cropWidth = mCaptureCropLayout.getWidth() * width.get() / mCaptureContainter.getWidth();
            int cropHeight = mCaptureCropLayout.getHeight() * height.get() / mCaptureContainter.getHeight();
            setCropWidth(cropWidth);
            setCropHeight(cropHeight);
        } catch (IOException | RuntimeException ioe) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(QrBarScanActivity.this);
        }
    }
    //========================================打开本地图片识别二维码 end=================================

    //--------------------------------------打开本地图片识别二维码 start---------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ContentResolver resolver = getContentResolver();
            // 照片的原始资源地址
            Uri originalUri = data.getData();
            try {
                // 使用ContentProvider通过URI获取原始图片
                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);

                // 开始对图像资源解码
                Result rawResult = QrBarTool.decodeFromPhoto(photo);
                if (rawResult != null) {
                    initResultData(rawResult);
                } else {
                    showMsg("图片识别失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //========================解析结果 及 后续处理 end

    private void initResultData(Result result) {
        // BarcodeFormat type = result.getBarcodeFormat();
        String realContent = result.getText();

        Intent intent = new Intent();
        intent.putExtra("data", realContent);
        setResult(RESULT_OK, intent);
        finish();

        // showMsg(realContent);

//        if (BarcodeFormat.QR_CODE.equals(type)) {
//            rxDialogSure.setTitle("二维码扫描结果");
//        } else if (BarcodeFormat.EAN_13.equals(type)) {
//            rxDialogSure.setTitle("条形码扫描结果");
//        } else {
//            rxDialogSure.setTitle("扫描结果");
//        }
        // 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
        // handler.sendEmptyMessage(R.id.restart_preview);
    }

    public void handleDecode(Result result) {
        inactivityTimer.onActivity();

        String result1 = result.getText();
        LogUtils.v("二维码/条形码 扫描结果", result1);
        // showMsg(result1);
        initResultData(result);
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.top_mask) {
            light();
        } else if (i == R.id.top_openpicture) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, GET_IMAGE_FROM_PHONE);
        } else if (i == R.id.top_back) {
            finish();

        }
    }
}
