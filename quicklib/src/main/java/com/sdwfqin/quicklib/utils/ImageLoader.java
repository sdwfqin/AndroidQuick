package com.sdwfqin.quicklib.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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
        Glide
                .with(context)
                .load(model)
                .into(imageView);
    }

    /**
     * 简单加载图片+CenterCrop缩放
     */
    public static void loadCenterCropImage(Context context, ImageView imageView, Object model) {

        RequestOptions options = new RequestOptions()
                .centerCrop();

        Glide
                .with(context)
                .load(model)
                .apply(options)
                .into(imageView);
    }

    /**
     * 简单加载图片+CenterCrop缩放
     */
    public static void loadBaseCropImage(Context context, ImageView imageView, Object model) {

        RequestOptions options = new RequestOptions()
                .centerCrop();

        Glide
                .with(context)
                .load(Constants.BASE_URL + model)
                .apply(options)
                .into(imageView);
    }

    /**
     * 默认占位图
     */
    public static void loadPlaceImage(Context context, ImageView imageView, Object model) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher);

        Glide
                .with(context)
                .load(model)
                .apply(options)
                .into(imageView);
    }

    /**
     * 占位图可修改
     */
    public static void loadPlaceImage(Context context, ImageView imageView, @DrawableRes int placeholder, Object model) {

        RequestOptions options = new RequestOptions()
                .placeholder(placeholder);

        Glide
                .with(context)
                .load(model)
                .apply(options)
                .into(imageView);
    }

    /**
     * 占位图可修改+缓存处理
     */
    public static void loadPlaceCacheImage(Context context, ImageView imageView, @DrawableRes int placeholder, DiskCacheStrategy strategy, Object model) {

        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .diskCacheStrategy(strategy);

        Glide
                .with(context)
                .load(model)
                .apply(options)
                .into(imageView);
    }
}
