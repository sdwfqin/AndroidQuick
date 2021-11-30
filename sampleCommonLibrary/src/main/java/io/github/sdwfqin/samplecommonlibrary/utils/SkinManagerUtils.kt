package io.github.sdwfqin.samplecommonlibrary.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode
import com.blankj.utilcode.util.SPUtils

/**
 * 换肤
 * <p>
 *
 * @author 张钦
 * @date 2021/11/19
 */
object SkinManagerUtils {

    fun initialize() {
        AppCompatDelegate.setDefaultNightMode(getCurrentSkin())
    }

    fun changeSkin(@NightMode skinMode: Int) {
        var mode = skinMode
        if (mode == 0) {
            mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        SPUtils.getInstance().put("skin", mode)
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun getCurrentSkin(): Int {
        var mode = SPUtils.getInstance().getInt("skin")
        if (mode < -1 || mode == 0 || mode > 2) {
            mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        return mode
    }

}