package io.github.sdwfqin.quickseed.data.bean

import android.net.Uri
import io.github.sdwfqin.widget.pictureupload.PictureUploadModel

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/5/3
 */
class PictureModel(var imgPath: Uri) : PictureUploadModel {
    override fun toString(): String {
        return "PictureModel{" +
                "imgPath='" + imgPath + '\'' +
                ", image=" + imgPath +
                '}'
    }

    override val pictureImage: Any
        get() = imgPath
}