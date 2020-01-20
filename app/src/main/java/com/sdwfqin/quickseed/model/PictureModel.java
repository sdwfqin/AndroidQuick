package com.sdwfqin.quickseed.model;

import com.sdwfqin.widget.pictureupload.PictureUpModel;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/5/31
 */
public class PictureModel extends PictureUpModel {

    private String imgPath;

    public PictureModel(String imgPath) {
        super(imgPath);
        this.imgPath = imgPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
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
