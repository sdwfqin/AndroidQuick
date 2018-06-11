package com.sdwfqin.quicklib.view.pictureupload;

import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
        ImageView iiImg = helper.getView(R.id.ii_img);

        if (item != null) {
            ImageLoader.loadCenterCropImage(mContext, iiImg, item.getImage());
        } else {
            iiImg.setImageResource(R.drawable.quick_add_img);
        }

        // 视图树的观察者，可以监听 View 的全局变化事件
        iiImg.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        iiImg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        ViewGroup.LayoutParams layoutParams = iiImg.getLayoutParams();
                        layoutParams.height = iiImg.getWidth();
                        iiImg.setLayoutParams(layoutParams);
                    }
                });

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
