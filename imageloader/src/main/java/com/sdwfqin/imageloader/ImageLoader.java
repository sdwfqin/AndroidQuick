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
    private Builder builder;
    private String url;

    private ImageLoader(ImageView imageView, Builder builder) {
        imageViewWeakReference = new WeakReference<>(imageView);
        this.builder = builder;
    }

    /**
     * 创建GlideRequest
     */
    public GlideRequests loadBaseImage() {
        if (builder.image instanceof String && ((String) builder.image).toLowerCase().startsWith(HTTP)) {
            url = (String) builder.image;
        }
        return GlideApp.with(getContext());
    }

    /**
     * 创建GlideRequest<Drawable>
     */
    public GlideRequest<Drawable> loadDrawableImage() {
        if (builder.image instanceof String && ((String) builder.image).toLowerCase().startsWith(HTTP)) {
            url = (String) builder.image;
        }
        GlideRequest<Drawable> glideRequest = loadBaseImage().load(builder.image);
        if (builder.placeholder != 0) {
            glideRequest = glideRequest.placeholder(builder.placeholder);
        }
        if (builder.errorImage != 0) {
            glideRequest = glideRequest.error(builder.errorImage);
        }
        if (builder.transformation != null) {
            glideRequest = glideRequest.transform(builder.transformation);
        } else if (builder.imageRadius != 0) {
            glideRequest = glideRequest.transform(new RadiusTransformation(getContext(), builder.imageRadius));
        }
        return glideRequest;
    }

    /**
     * 加载到控件
     */
    public ImageLoader loadImage() {
        GlideRequest<Drawable> glideRequest = loadDrawableImage();
        glideRequest.into(getImageView());
        return this;
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
     * 网络图片加载进度
     */
    public ImageLoader setOnProgressListener(OnProgressListener onProgressListener) {
        if (!TextUtils.isEmpty(url)) {
            ProgressManager.addListener(url, onProgressListener);
        }
        return this;
    }

    public static class Builder {
        // 图片地址/链接等
        private Object image = null;
        // 占位图
        private int placeholder = 0;
        // 错误图
        private int errorImage = 0;
        // 图片角度
        private int imageRadius = 0;
        private Transformation<Bitmap> transformation = null;

        public Builder() {
        }

        public Builder setImagePath(Object image) {
            this.image = image;
            return this;
        }

        public Builder setPlaceholder(@DrawableRes int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Builder setErrorImage(@DrawableRes int errorImage) {
            this.errorImage = errorImage;
            return this;
        }

        public Builder setImageRadius(int imageRadius) {
            this.imageRadius = imageRadius;
            return this;
        }

        public Builder setTransformation(Transformation<Bitmap> transformation) {
            this.transformation = transformation;
            return this;
        }

        public ImageLoader build(ImageView imageView) {
            if (image == null) {
                throw new IllegalArgumentException("请检查图片地址是否有误");
            }
            return new ImageLoader(imageView, this);
        }
    }
}
