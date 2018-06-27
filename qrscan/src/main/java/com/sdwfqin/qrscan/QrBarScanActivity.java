package com.sdwfqin.qrscan;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.google.zxing.Result;
import com.otaliastudios.cameraview.AspectRatio;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.SizeSelector;
import com.otaliastudios.cameraview.SizeSelectors;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述：二维码/条形码识别Activity
 *
 * @author 张钦
 * @date 2018/1/23
 */
public class QrBarScanActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int GET_IMAGE_FROM_PHONE = 5002;
    private static final String TAG = "QrBarScanActivity";

    private Context mContext;

    private ImageView mCaptureScanLine;
    private CameraView mCameraView;
    /**
     * 中间剪裁框
     */
    private RelativeLayout mCaptureCropLayout;
    /**
     * root_view
     */
    private RelativeLayout mCaptureContainter;
    private ImageView mTop_mask;
    private ImageView mTop_openpicture;
    private ImageView mTop_back;
    /**
     * 闪光灯开启状态
     */
    private boolean mFlashing = true;

    private CompositeDisposable mCompositeDisposable;
    private CameraListener mCameraListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_activity_qr_scan);

        mContext = this;

        BarUtils.setStatusBarAlpha(this);

        mCaptureScanLine = findViewById(R.id.capture_scan_line);
        mCameraView = findViewById(R.id.capture_camera);
        mCaptureCropLayout = findViewById(R.id.capture_crop_layout);
        mCaptureContainter = findViewById(R.id.capture_containter);
        mCaptureContainter = findViewById(R.id.capture_containter);

        mTop_mask = findViewById(R.id.top_mask);
        mTop_openpicture = findViewById(R.id.top_openpicture);
        mTop_back = findViewById(R.id.top_back);

        //扫描动画初始化
        initScanerAnimation();

        mTop_mask.setOnClickListener(this);
        mTop_openpicture.setOnClickListener(this);
        mTop_back.setOnClickListener(this);

        initCamera();
    }

    /**
     * 初始化Camera
     */
    private void initCamera() {

        mCameraView.setPlaySounds(false);

        SizeSelector width = SizeSelectors.maxWidth(ScreenUtils.getScreenWidth());
        SizeSelector height = SizeSelectors.maxWidth(ScreenUtils.getScreenHeight());
        SizeSelector dimensions = SizeSelectors.and(width, height);
        final SizeSelector ratio = SizeSelectors.aspectRatio(AspectRatio.of(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight()), 0);

        mCameraView.setPictureSize(SizeSelectors.and(ratio, dimensions));

        Observable<Result> observable = Observable.create((ObservableOnSubscribe<Result>) e ->
                mCameraView.addCameraListener(new CameraListener() {
                    @Override
                    public void onPictureTaken(byte[] picture) {
                        Bitmap bitmap = ImageUtils.bytes2Bitmap(picture);
                        Bitmap clip = ImageUtils.clip(bitmap,
                                mCaptureCropLayout.getLeft(),
                                mCaptureCropLayout.getTop(),
                                mCaptureCropLayout.getRight() - mCaptureCropLayout.getLeft(),
                                mCaptureCropLayout.getBottom() - mCaptureCropLayout.getTop(),
                                true);
                        Result rawResult = QrBarTool.decodeFromPhoto(clip);
                        if (rawResult != null) {
                            e.onNext(rawResult);
                        } else {
                            mCameraView.captureSnapshot();
                        }
                    }

                    @Override
                    public void onCameraOpened(CameraOptions options) {
                        mCameraView.captureSnapshot();
                    }
                })).subscribeOn(Schedulers.io());
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::initResultData, throwable -> {
                    Log.e(TAG, "onError: ", throwable);
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        mCameraView.destroy();
    }

    /**
     * 扫描动画
     */
    private void initScanerAnimation() {
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1200);
        mCaptureScanLine.startAnimation(animation);
    }

    /**
     * 闪光灯
     */
    private void flash() {
        if (mFlashing) {
            // 开闪光灯
            mCameraView.setFlash(Flash.TORCH);
        } else {
            // 关闪光灯
            mCameraView.setFlash(Flash.OFF);
        }

        mFlashing = !mFlashing;

    }

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
                    Toast.makeText(mContext, "图片识别失败", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initResultData(Result result) {
        String realContent = result.getText();

        Intent intent = new Intent();
        intent.putExtra("data", realContent);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.top_mask) {
            flash();
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
