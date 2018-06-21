package com.sdwfqin.imageloader.progress;

/**
 * 描述：图片加载进度
 *
 * @author zhangqin
 * @date 2018/6/21
 */
public interface OnProgressListener {

    /**
     * 加载中
     * @param progress
     */
    void onLoading(int progress);

    /**
     * 加载成功
     */
    void onLoadSuccess();
}
