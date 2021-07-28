package io.github.sdwfqin.widget.pictureupload;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import io.github.sdwfqin.imageloader.ImageLoaderManager;
import io.github.sdwfqin.widget.R;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/5/31
 */
public class PictureUploadAdapter<T extends PictureUploadModel> extends BaseQuickAdapter<T, BaseViewHolder> {

    public PictureUploadAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        addChildClickViewIds(R.id.ii_img, R.id.ii_del);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        ImageView iiImg = helper.getView(R.id.ii_img);

        if (item != null) {
            ImageLoaderManager imageLoaderManager = new ImageLoaderManager.Builder()
                    .setImagePath(item.getPictureImage())
                    .setPlaceholder(R.mipmap.image_loading)
                    .setErrorImage(R.mipmap.image_load_err)
                    .build(iiImg);
            imageLoaderManager
                    .loadDrawableImage()
                    .into(imageLoaderManager.getImageView());
        } else {
            iiImg.setImageResource(R.drawable.quick_add_img);
        }

        if (item == null) {
            helper.setVisible(R.id.ii_del, false);
        } else {
            helper.setVisible(R.id.ii_del, true);
        }
    }
}
