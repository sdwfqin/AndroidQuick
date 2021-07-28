package io.github.sdwfqin.widget.pictureupload;

import java.util.List;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/5/31
 */
public interface PictureUploadCallback<T extends PictureUploadModel> {

    /**
     * 移除某个图片
     */
    void remove(int position, List<T> list);

    /**
     * 点击某个图片
     */
    void click(int position, T t, List<T> list);

    /**
     * 添加图片按钮
     */
    void onAddPic(int maxPic, List<T> list);
}
