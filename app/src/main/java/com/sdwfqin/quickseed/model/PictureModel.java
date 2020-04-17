package com.sdwfqin.quickseed.model;

import android.net.Uri;

import com.sdwfqin.widget.pictureupload.PictureUpModel;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/5/31
 */
public class PictureModel extends PictureUpModel {

    private Uri imgPath;

    public PictureModel(Uri imgPath) {
        super(imgPath);
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
                ", image=" + image +
                '}';
    }
}
