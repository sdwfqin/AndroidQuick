package io.github.sdwfqin.quickseed.data.bean;

import android.net.Uri;

import io.github.sdwfqin.widget.pictureupload.PictureUploadModel;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/5/3，1
 */
public class PictureModel implements PictureUploadModel {

    private Uri imgPath;

    public PictureModel(Uri imgPath) {
        this.imgPath = imgPath;
    }

    public Uri getImgPath() {
        return imgPath;
    }

    public void setImgPath(Uri imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public String toString() {
        return "PictureModel{" +
                "imgPath='" + imgPath + '\'' +
                ", image=" + imgPath +
                '}';
    }

    @Override
    public Object getPictureImage() {
        return imgPath;
    }
}
