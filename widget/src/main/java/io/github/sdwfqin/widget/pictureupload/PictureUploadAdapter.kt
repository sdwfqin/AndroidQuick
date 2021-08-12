package io.github.sdwfqin.widget.pictureupload

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.github.sdwfqin.imageloader.ImageLoaderManager
import io.github.sdwfqin.widget.R

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/5/31
 */
class PictureUploadAdapter<T : PictureUploadModel>(layoutResId: Int, data: MutableList<T?>?) :
    BaseQuickAdapter<T?, BaseViewHolder>(layoutResId, data) {

    init {
        addChildClickViewIds(R.id.ii_img, R.id.ii_del)
    }

    override fun convert(holder: BaseViewHolder, item: T?) {
        val iiImg = holder.getView<ImageView>(R.id.ii_img)
        item?.let {
            holder.setVisible(R.id.ii_del, true)
            val imageLoaderManager = ImageLoaderManager.Builder()
                .setImagePath(item.pictureImage)
                .setPlaceholder(R.mipmap.image_loading)
                .setErrorImage(R.mipmap.image_load_err)
                .build(iiImg)
            imageLoaderManager
                .loadDrawableImage()
                .into(imageLoaderManager.imageView)
        } ?: let {
            holder.setVisible(R.id.ii_del, false)
            iiImg.setImageResource(R.drawable.quick_add_img)
        }
    }
}