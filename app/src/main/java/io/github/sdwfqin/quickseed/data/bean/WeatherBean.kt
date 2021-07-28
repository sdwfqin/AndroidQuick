package io.github.sdwfqin.quickseed.data.bean

import java.io.Serializable

/**
 * 天气bean
 *
 * @author 张钦
 * @date 2020/4/14
 */
data class WeatherBean(
        val errcode: Int,
        val errmsg: String,
        val cityid: String,
        val city: String,
        val update_time: String,
        val wea: String,
        val wea_img: String,
        val tem: String,
        val tem_day: String,
        val tem_night: String,
        val win: String,
        val win_speed: String,
        val win_meter: String,
        val air: String
) : Serializable