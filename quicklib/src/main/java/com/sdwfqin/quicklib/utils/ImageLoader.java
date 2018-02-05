package com.sdwfqin.quicklib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.sdwfqin.quicklib.GlideApp;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.base.Constants;

/**
 * 描述：Glide 4.x 工具类
 *
 * @author 张钦
 * @date 2018/1/23
 */
public class ImageLoader {

    /**
     * 简单加载图片
     */
    public static void loadImage(Context context, ImageView imageView, Object model) {
        GlideApp
                .with(context)
                .load(model)
                .into(imageView);
    }

    /**
     * 简单加载图片+CenterCrop缩放
     */
    public static void loadCenterCropImage(Context context, ImageView imageView, Object model) {
        GlideApp
                .with(context)
                .load(model)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 简单加载图片+CenterCrop缩放
     */
    public static void loadBaseCropImage(Context context, ImageView imageView, Object model) {
        GlideApp
                .with(context)
                .load(Constants.BASE_URL + model)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 默认占位图
     */
    public static void loadPlaceImage(Context context, ImageView imageView, Object model) {
        GlideApp
                .with(context)
                .load(model)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
    }

    /**
     * 占位图可修改
     */
    public static void loadPlaceImage(Context context, ImageView imageView, @DrawableRes int placeholder, Object model) {
        GlideApp
                .with(context)
                .load(model)
                .placeholder(placeholder)
                .into(imageView);
    }

    /**
     * 占位图可修改+缓存处理
     */
    public static void loadPlaceCacheImage(Context context, ImageView imageView, @DrawableRes int placeholder, DiskCacheStrategy strategy, Object model) {
        GlideApp
                .with(context)
                .load(model)
                .placeholder(placeholder)
                .diskCacheStrategy(strategy)
                .into(imageView);
    }

    /**
     * Bitmap
     */
    public static void loadBitmapImage(Context context, Object model, SimpleTarget<Bitmap> target) {
        GlideApp
                .with(context)
                .asBitmap()
                .load(model)
                .into(target);
    }
}
