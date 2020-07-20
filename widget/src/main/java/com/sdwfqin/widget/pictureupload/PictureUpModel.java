package com.sdwfqin.widget.pictureupload;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/5/31
 */
public class PictureUpModel<T> implements Serializable {

    protected T image;

    public PictureUpModel() {
    }

    public PictureUpModel(T image) {
        this.image = image;
    }

    public T getImage() {
        return image;
    }

    public void setImage(T image) {
        this.image = image;
    }
}
