package io.github.sdwfqin.quicklib.imagepreview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import io.github.sdwfqin.imageloader.ImageLoaderManager;
import io.github.sdwfqin.imageloader.progress.OnProgressListener;
import io.github.sdwfqin.quicklib.R;
import io.github.sdwfqin.quicklib.base.BaseFragment;
import io.github.sdwfqin.quicklib.base.QuickConstants;
import io.github.sdwfqin.quicklib.databinding.QuickFragmentImagePreviewBinding;

/**
 * 描述：显示图片
 *
 * @author zhangqin
 * @date 2017/8/7
 */
public class ImagePreviewFragment extends BaseFragment<QuickFragmentImagePreviewBinding> {

    private String url;

    public static Fragment newInstance(String url) {

        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected QuickFragmentImagePreviewBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return QuickFragmentImagePreviewBinding.inflate(inflater);
    }

    @Override
    protected void initEventAndData() {

        if (getArguments() != null) {
            url = getArguments().getString("url");
        }

        new ImageLoaderManager.Builder()
                .setImagePath(url)
                .setPlaceholder(R.mipmap.image_loading)
                .setErrorImage(R.mipmap.image_load_err)
                .build(mBinding.image)
                .loadImage()
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onLoading(int progress) {
                        mBinding.progressBar.setVisibility(View.VISIBLE);
                        mBinding.progressBar.setProgress(progress);
                    }

                    @Override
                    public void onLoadSuccess() {
                        mBinding.progressBar.setVisibility(View.GONE);
                    }
                });

        // setOnLongClickListener中return的值决定是否在长按后再加一个短按动作
        // true为不加短按,false为加入短按
        mBinding.image.setOnLongClickListener(view -> {
            initSaveImageDialog();
            return false;
        });
        mBinding.image.setOnClickListener(v -> {
            mBaseActivity.getActivity().finish();
        });
    }

    @Override
    protected void initClickListener() {

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
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) mBinding.image.getDrawable();
                    byte[] bitmap2Bytes = ImageUtils.bitmap2Bytes(bitmapDrawable.getBitmap(), Bitmap.CompressFormat.JPEG, 100);
                    if (FileIOUtils.writeFileFromBytesByStream(filePath, bitmap2Bytes)) {
                        Snackbar.make(mBinding.getRoot(), R.string.quick_img_save_success, Snackbar.LENGTH_SHORT).show();
                        Uri contentUri = Uri.fromFile(new File(filePath));
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
                        mContext.sendBroadcast(mediaScanIntent);
                    } else {
                        throw new Exception(getString(R.string.quick_img_save_error));
                    }
                } else {
                    throw new Exception(getString(R.string.quick_img_save_error));
                }
            } catch (Exception e) {
                LogUtils.e(e);
                mBaseActivity.showMsg(e.getMessage());
            }
        } else {
            mBaseActivity.showMsg(getString(R.string.quick_sd_not_found));
        }
    }
}
