package com.sdwfqin.quickseed.ui;

import com.luck.picture.lib.entity.LocalMedia;
import com.sdwfqin.widget.pictureupload.PictureUpModel;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/5/31
 */
public class PictureModel extends PictureUpModel {

    private LocalMedia mLocalMedia;

    public PictureModel(LocalMedia localMedia) {
        super(localMedia.getPath());
        mLocalMedia = localMedia;
    }

    public LocalMedia getLocalMedia() {
        return mLocalMedia;
    }

    public void setLocalMedia(LocalMedia localMedia) {
        mLocalMedia = localMedia;
    }
}
