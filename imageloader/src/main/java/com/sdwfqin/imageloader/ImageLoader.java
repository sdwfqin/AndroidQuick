package com.sdwfqin.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.load.Transformation;
import com.sdwfqin.imageloader.progress.OnProgressListener;
import com.sdwfqin.imageloader.progress.ProgressManager;
import com.sdwfqin.imageloader.transformation.RadiusTransformation;

import java.lang.ref.WeakReference;

/**
 * 描述：Glide图片加载
 *
 * @author zhangqin
 * @date 2018/6/20
 */
public class ImageLoader {

    protected static final String ANDROID_RESOURCE = "android.resource://";
    protected static final String FILE = "file://";
    protected static final String SEPARATOR = "/";
    protected static final String HTTP = "http";

    private WeakReference<ImageView> imageViewWeakReference;
    private String url;

    /**
     * 初始化
     */
    public static ImageLoader init(ImageView imageView) {
        return new ImageLoader(imageView);
    }

    private ImageLoader(ImageView imageView) {
        imageViewWeakReference = new WeakReference<>(imageView);
    }

    /**
     * 获取图片控件
     */
    public ImageView getImageView() {
        if (imageViewWeakReference != null) {
            return imageViewWeakReference.get();
        }
        return null;
    }

    /**
     * 获取上下文对象
     */
    public Context getContext() {
        if (getImageView() != null) {
            return getImageView().getContext();
        }
        return null;
    }

    /**
     * 获取图片地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 简单
     */
    public ImageLoader load(Object obj) {
        return load(obj, 0);
    }

    /**
     * 带有占位图
     */
    public ImageLoader load(Object obj, @DrawableRes int placeholder) {
        return load(obj, placeholder, 0);
    }

    /**
     * 占位图+错误图
     */
    public ImageLoader load(Object obj, @DrawableRes int placeholder, @DrawableRes int error) {
        return loadImage(obj, placeholder, error, null);
    }

    /**
     * 加载图片
     */
    public ImageLoader load(Object obj, @DrawableRes int placeholder, @DrawableRes int error, int radius) {
        return loadImage(obj, placeholder, error, new RadiusTransformation(getContext(), radius));
    }

    /**
     * 加载图片
     */
    public ImageLoader load(Object obj, @DrawableRes int placeholder, @DrawableRes int error, Transformation<Bitmap> transformation) {
        return loadImage(obj, placeholder, error, transformation);
    }

    /**
     * 创建GlideRequest
     */
    public GlideRequest<Drawable> loadImage(Object obj) {
        if (obj instanceof String && ((String) obj).toLowerCase().startsWith(HTTP)) {
            url = (String) obj;
        }
        return GlideApp.with(getContext()).load(obj);
    }

    /**
     * 加载到控件
     */
    public ImageLoader loadImage(Object obj, @DrawableRes int placeholder, @DrawableRes int error, Transformation<Bitmap> transformation) {
        GlideRequest<Drawable> glideRequest = loadImage(obj);
        if (placeholder != 0) {
            glideRequest = glideRequest.placeholder(placeholder).error(error);
        }

        if (transformation != null) {
            glideRequest = glideRequest.transform(transformation);
        }
        glideRequest.centerCrop().into(getImageView());
        return this;
    }

    /**
     * 网络图片加载进度
     */
    public ImageLoader setOnProgressListener(OnProgressListener onProgressListener) {
        if (!TextUtils.isEmpty(url)) {
            ProgressManager.addListener(url, onProgressListener);
        }
        return this;
    }
}
