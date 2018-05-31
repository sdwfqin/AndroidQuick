package com.sdwfqin.quicklib.view.pictureupload;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.utils.ImageLoader;

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
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        ImageView ii_img = helper.getView(R.id.ii_img);

        if (item != null) {
            ImageLoader.loadCenterCropImage(mContext, ii_img, item.getImage());
        } else {
            ii_img.setImageResource(R.drawable.add_img);
        }

        // 点击事件
        helper.addOnClickListener(R.id.ii_img)
                .addOnClickListener(R.id.ii_del);

        if (item == null) {
            helper.setVisible(R.id.ii_del, false);
        } else {
            helper.setVisible(R.id.ii_del, true);
        }
    }
}
