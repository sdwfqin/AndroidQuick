package io.github.sdwfqin.widget.utils

import android.graphics.drawable.GradientDrawable
import java.util.ArrayList

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/6/20
 */
object WidgetUtils {
    /**
     * 分割字符串
     *
     * @param str
     * @return
     */
    @JvmStatic
    fun subStringToList(str: String): List<String> {
        val result: MutableList<String> = ArrayList()
        if (str.isNotEmpty()) {
            val len = str.length
            if (len <= 1) {
                result.add(str)
                return result
            } else {
                for (i in 0 until len) {
                    result.add(str.substring(i, i + 1))
                }
            }
        }
        return result
    }

    /**
     * 获取shape图形
     *
     * @param solidColor  实心
     * @param strokeColor 边框颜色
     * @param strokeWidth 边框宽度
     * @param radius      半径角度
     * @return
     */
    fun getDrawable(
        solidColor: Int,
        strokeColor: Int,
        strokeWidth: Int,
        radius: Float
    ): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.setColor(solidColor)
        drawable.setStroke(strokeWidth, strokeColor)
        drawable.cornerRadius = radius
        return drawable
    }
}