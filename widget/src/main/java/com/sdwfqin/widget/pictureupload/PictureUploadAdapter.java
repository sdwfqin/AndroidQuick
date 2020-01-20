package com.sdwfqin.widget.pictureupload;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sdwfqin.imageloader.ImageLoader;
import com.sdwfqin.widget.R;

import java.util.List;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/5/31
 */
public class PictureUploadAdapter<T extends PictureUpModel> extends BaseQuickAdapter<T, BaseViewHolder> {

    public PictureUploadAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        addChildClickViewIds(R.id.ii_img, R.id.ii_del);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        ImageView iiImg = helper.getView(R.id.ii_img);

        if (item != null) {
            ImageLoader imageLoader = new ImageLoader.Builder()
                    .setImagePath(item.getImage())
                    .setPlaceholder(R.mipmap.image_loading)
                    .setErrorImage(R.mipmap.image_load_err)
                    .build(iiImg);
            imageLoader
                    .loadDrawableImage()
                    .into(imageLoader.getImageView());
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
