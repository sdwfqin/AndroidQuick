package io.github.sdwfqin.imageloader.progress

/**
 * 描述：图片加载进度
 *
 * @author zhangqin
 * @date 2018/6/21
 */
interface OnProgressListener {
    /**
     * 加载中
     * @param progress
     */
    fun onLoading(progress: Int)

    /**
     * 加载成功
     */
    fun onLoadSuccess()
}