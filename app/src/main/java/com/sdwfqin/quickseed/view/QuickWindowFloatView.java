package com.sdwfqin.quickseed.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sdwfqin.quicklib.utils.rx.RxSchedulersUtils;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.widget.WindowFloatView;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 悬浮窗View Demo
 * <p>
 *
 * @author 张钦
 * @date 2020/4/10
 */
public class QuickWindowFloatView extends WindowFloatView {

    private AppCompatButton mBtn_screenshot;
    private AppCompatButton mBtn_close;
    private ImageView mIv_img;

    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenDensity;

    private ImageReader mImageReader;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private Disposable mSubscribe;

    public QuickWindowFloatView(@NonNull Context context, Intent data) {
        super(context);

        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mMediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, data);
    }

    @Override
    protected void onCreate(View decor, WindowManager.LayoutParams layoutParams) {
        super.onCreate(decor, layoutParams);

        mBtn_screenshot = findViewById(R.id.btn_screenshot);
        mBtn_close = findViewById(R.id.btn_close);
        mIv_img = findViewById(R.id.iv_img);

        //获取当前屏幕的像素点
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        mImageReader = ImageReader.newInstance(mScreenWidth, mScreenHeight, PixelFormat.RGBA_8888, 1);
        // 获取VirtureDisplay对象
        getVirtualDisplay();

        initListener();

        /**
         * 允许拖动悬浮窗
         */
        setCanMove(true);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_float_quick;
    }

    @Override
    protected int getHeight() {
        return ConvertUtils.dp2px(200);
    }

    @Override
    protected int getWidth() {
        return ConvertUtils.dp2px(80);
    }

    private void initListener() {
        mBtn_close.setOnClickListener(v -> {
            dismiss();
        });
        mBtn_screenshot.setOnClickListener(v -> {
            hide();
            new Handler().postDelayed(this::startCapture, 100);
        });
    }

    private void startCapture() {
        Image image = mImageReader.acquireLatestImage();
        if (image == null) {
            //开始截屏
            getVirtualDisplay();
            LogUtils.e("getVirtualDisplay");
            show();
        } else {
            //保存截屏
            mSubscribe = Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
                int width = image.getWidth();
                int height = image.getHeight();
                final Image.Plane[] planes = image.getPlanes();
                final ByteBuffer buffer = planes[0].getBuffer();
                //每个像素的间距
                int pixelStride = planes[0].getPixelStride();
                //总的间距
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * width;
                Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(buffer);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
                image.close();
                // 判断文件有没有
                File fileImage = null;
                // 保存图片
                if (bitmap != null) {
                    try {
                        fileImage = new File(getContext().getExternalMediaDirs()[0], System.currentTimeMillis() + ".jpg");

                        if (!fileImage.exists()) {
                            fileImage.createNewFile();
                        }
                        FileOutputStream out = new FileOutputStream(fileImage);
                        if (out != null) {
                            ToastUtils.showShort("正在保存中...");
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                            out.flush();
                            out.close();
                            // 发送广播给相册--更新相册图片
                            Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            Uri contentUri = Uri.fromFile(fileImage);
                            media.setData(contentUri);
                            getContext().sendBroadcast(media);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        fileImage = null;
                    }
                }

                if (fileImage != null) {
                    emitter.onNext(bitmap);
                }
                emitter.onComplete();
            }).compose(RxSchedulersUtils.rxObservableSchedulerHelper())
                    .subscribe(bitmap -> {
                        //预览图片
                        if (bitmap != null) {
                            ToastUtils.showShort("截图已经成功保存到相册");
                            mIv_img.setImageBitmap(bitmap);
                        }
                    }, throwable -> {

                    }, this::show);
        }
    }

    private void getVirtualDisplay() {
        if (mMediaProjection != null) {
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                    mScreenWidth, mScreenHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mImageReader.getSurface(), null, null);
        }
    }

    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    private void stopVirtual() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
    }

    @Override
    protected void onStop() {
        if (mSubscribe != null) {
            mSubscribe.dispose();
        }
        stopVirtual();
        tearDownMediaProjection();
    }
}
