package io.github.sdwfqin.widget.pictureupload

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/5/31
 */
interface PictureUploadCallback<T : PictureUploadModel> {
    /**
     * 移除某个图片
     */
    fun remove(position: Int, list: List<T>): Boolean

    /**
     * 点击某个图片
     */
    fun click(position: Int, item: T, list: List<T>)

    /**
     * 添加图片按钮
     */
    fun onAddPic(maxPic: Int, list: List<T>)
}