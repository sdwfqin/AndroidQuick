package io.github.sdwfqin.imageloader.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

object DisplayUtil {

    private fun getWindowManager(context: Context): WindowManager {
        return context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    fun getDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    fun getFontDensity(context: Context): Float {
        return context.resources.displayMetrics.scaledDensity
    }

    fun getDisplayMetrics(context: Context): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        getWindowManager(context).defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    @JvmStatic
    fun dp2px(context: Context, dp: Float): Int {
        return (getDensity(context) * dp + 0.5f).toInt()
    }

    fun px2dp(context: Context, px: Float): Int {
        return (px / getDensity(context) + 0.5f).toInt()
    }

    fun sp2px(context: Context, sp: Float): Int {
        return (getFontDensity(context) * sp + 0.5f).toInt()
    }

    fun px2sp(context: Context, px: Float): Int {
        return (px / getFontDensity(context) + 0.5f).toInt()
    }

    fun getWindowWidth(context: Context): Int {
        return getDisplayMetrics(context).widthPixels
    }

    fun getWindowHeight(context: Context): Int {
        return getDisplayMetrics(context).heightPixels
    }
}