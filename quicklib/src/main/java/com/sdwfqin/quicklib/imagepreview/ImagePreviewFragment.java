package com.sdwfqin.quicklib.imagepreview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.github.chrisbanes.photoview.PhotoView;
import com.sdwfqin.imageloader.ImageLoader;
import com.sdwfqin.imageloader.progress.CircleProgressView;
import com.sdwfqin.imageloader.progress.OnProgressListener;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.base.BaseFragment;
import com.sdwfqin.quicklib.base.QuickConstants;

import java.io.File;

import androidx.fragment.app.Fragment;

/**
 * 描述：显示图片
 *
 * @author zhangqin
 * @date 2017/8/7
 */
public class ImagePreviewFragment extends BaseFragment {

    private PhotoView mImage;
    private CircleProgressView mProgressBar;
    private String url;

    public static Fragment newInstance(String s) {

        Bundle bundle = new Bundle();
        bundle.putString("url", s);
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.quick_fragment_image_preview;
    }

    @Override
    protected void initEventAndData() {
        mImage = mView.findViewById(R.id.image);
        mProgressBar = mView.findViewById(R.id.progressBar);

        if (getArguments() != null) {
            url = getArguments().getString("url");
        }

        ImageLoader imageLoader = new ImageLoader.Builder()
                .setImagePath(url)
                .setPlaceholder(R.mipmap.image_loading)
                .setErrorImage(R.mipmap.image_load_err)
                .build(mImage);
        imageLoader
                .loadDrawableImage()
                .into(imageLoader.getImageView());
        imageLoader.setOnProgressListener(new OnProgressListener() {
            @Override
            public void onLoading(int progress) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(progress);
            }

            @Override
            public void onLoadSuccess() {
                mProgressBar.setVisibility(View.GONE);
            }
        });

        // setOnLongClickListener中return的值决定是否在长按后再加一个短按动作
        // true为不加短按,false为加入短按
        mImage.setOnLongClickListener(view -> {
            initSaveImageDialog();
            return false;
        });
        mImage.setOnClickListener(v -> {
            mActivity.finish();
        });
    }

    @Override
    protected void lazyLoadShow(boolean isLoad) {
    }

    private void initSaveImageDialog() {
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomDialogImagePreviewFragment
                .Builder()
                .setOnClickListener(new BottomDialogImagePreviewFragment.OnDialogClickListener() {
                    @Override
                    public void save() {
                        saveImage();
                    }

                    @Override
                    public void exit() {

                    }
                })
                .builder();
        bottomSheetDialogFragment.show(getChildFragmentManager(), "preview_image");
    }

    /**
     * 保存图片
     */
    public void saveImage() {
        if (SDCardUtils.isSDCardEnableByEnvironment()) {
            try {
                String filePath = QuickConstants.SAVE_REAL_PATH + url.substring(url.lastIndexOf("/"));
                if (FileUtils.createOrExistsFile(filePath)) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) mImage.getDrawable();
                    byte[] bitmap2Bytes = ImageUtils.bitmap2Bytes(bitmapDrawable.getBitmap(), Bitmap.CompressFormat.JPEG);
                    if (FileIOUtils.writeFileFromBytesByStream(filePath, bitmap2Bytes)) {
                        Snackbar.make(mView, "图片保存成功", Snackbar.LENGTH_SHORT).show();
                        Uri contentUri = Uri.fromFile(new File(filePath));
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
                        mContext.sendBroadcast(mediaScanIntent);
                    } else {
                        throw new Exception("图片保存失败");
                    }
                } else {
                    throw new Exception("图片保存失败");
                }
            } catch (Exception e) {
                LogUtils.e(e);
                showMsg(e.getMessage());
            }
        } else {
            showMsg("内存卡不可用");
        }
    }
}
