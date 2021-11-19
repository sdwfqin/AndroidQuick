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
        SPUtils.getInstance().put("skin", skinMode)
        AppCompatDelegate.setDefaultNightMode(skinMode)
    }

    private fun getCurrentSkin(): Int {
        var skinMode = SPUtils.getInstance().getInt("skin")
        if (skinMode < 0 || skinMode > 2) {
            skinMode = 0
        }
        return skinMode
    }

}