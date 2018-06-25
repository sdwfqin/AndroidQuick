package com.sdwfqin.quicklib.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import com.sdwfqin.quicklib.base.QuickConstants;

import java.text.DecimalFormat;

/**
 * 描述：屏幕适配工具类
 * <p>
 * 通过修改系统参数来适配android设备
 * <p>
 * 参考：https://www.jianshu.com/p/4afc5c214a34
 *
 * @author zhangqin
 * @date 2018/6/25
 */
public class DensityUtils {

    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";

    private static float appDensity;
    private static float appScaledDensity;
    private static DisplayMetrics appDisplayMetrics;

    /**
     * 初始化
     */
    public static void init(@NonNull Application application) {
        //获取application的DisplayMetrics
        appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (appDensity == 0) {
            //初始化的时候赋值
            appDensity = appDisplayMetrics.density;
            appScaledDensity = appDisplayMetrics.scaledDensity;

            //添加字体变化的监听
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    //字体改变后,将appDisplayMetrics重新赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {
                }
            });
        }

        setAppOrientation(null, WIDTH);
    }

    /**
     * 在某一个Activity里面更改支配的方向
     *
     * @param activity
     * @param orientation
     */
    public static void setOrientation(Activity activity, String orientation) {
        setAppOrientation(activity, orientation);
    }

    /**
     * targetDensity
     * targetScaledDensity
     * targetDensityDpi
     * 这三个参数是统一修改过后的值
     */
    private static void setAppOrientation(@Nullable Activity activity, String orientation) {

        float targetDensity = 0;
        try {
            Double division;
            //根据带入参数选择不同的适配方向
            if (orientation.equals(HEIGHT)) {
                division = division(appDisplayMetrics.heightPixels, QuickConstants.UI_HEIGHT);
            } else {
                division = division(appDisplayMetrics.widthPixels, QuickConstants.UI_WIDTH);
            }
            //由于手机的长宽不尽相同,肯定会有除不尽的情况,有失精度,所以在这里把所得结果做了一个保留两位小数的操作
            DecimalFormat df = new DecimalFormat("0.00");
            String s = df.format(division);
            targetDensity = Float.parseFloat(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        float targetScaledDensity = targetDensity * (appScaledDensity / appDensity);
        int targetDensityDpi = (int) (160 * targetDensity);

        /**
         *
         * 最后在这里将修改过后的值赋给系统参数
         *
         * (因为最开始初始化的时候,activity为null,所以只设置application的值就可以了...
         * 但是!!!    在Activity里面修改方向之后,只设置application的值是不生效的,必须还要设置Activity的值,
         * 所以在这里判断了一下,如果传有activity的话,再设置Activity的值)
         */
        if (activity != null) {
            DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
            activityDisplayMetrics.density = targetDensity;
            activityDisplayMetrics.scaledDensity = targetScaledDensity;
            activityDisplayMetrics.densityDpi = targetDensityDpi;
        } else {
            appDisplayMetrics.density = targetDensity;
            appDisplayMetrics.scaledDensity = targetScaledDensity;
            appDisplayMetrics.densityDpi = targetDensityDpi;
        }
    }

    /**
     * 除法
     */
    private static Double division(double a, double b) {
        double div = 0;
        if (b != 0) {
            div = a / b;
        } else {
            div = 0;
        }
        return div;
    }
}
