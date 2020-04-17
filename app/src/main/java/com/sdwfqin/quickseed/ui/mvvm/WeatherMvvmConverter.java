package com.sdwfqin.quickseed.ui.mvvm;

import android.widget.EditText;

import androidx.databinding.InverseMethod;

import com.blankj.utilcode.util.TimeUtils;

/**
 * 转换器
 * <p>
 *
 * @author 张钦
 * @date 2020/4/17
 */
public class WeatherMvvmConverter {

    @InverseMethod("stringToDate")
    public static String dateToString(EditText view, long value) {
        return TimeUtils.millis2String(value);
    }

    public static long stringToDate(EditText view, String value) {
        return TimeUtils.string2Millis(value);
    }

}
