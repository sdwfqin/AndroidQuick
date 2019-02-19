package com.sdwfqin.quickseed.utils.picture;

import android.app.Activity;

import com.sdwfqin.quickseed.base.Constants;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

/**
 * 描述：图片选择封装
 * <p>
 * 开源库地址：https://github.com/zhihu/Matisse
 *
 * @author 张钦
 * @date 2018/7/17
 */
public class PictureSelectUtils {

    /**
     * 选择图片
     */
    public static void SelectSystemPhoto(Activity activity, int requestCode, int imgCount) {
        Matisse.from(activity)
                .choose(MimeType.ofImage())
                // 图片计数（第几张选中的）
                .countable(false)
                // 开启拍照
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, Constants.FILE_PROVIDER))
                .maxSelectable(imgCount)
                // 原图按钮
                .originalEnable(true)
                .imageEngine(new MatisseGlideEngine())
                .forResult(requestCode);
    }
}
